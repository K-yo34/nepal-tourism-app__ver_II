package com.nepaltourism.controller;

import com.nepaltourism.model.*;
import com.nepaltourism.service.DataService;
import com.nepaltourism.util.CSVExporter;
import com.nepaltourism.util.ResourceBundleManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportController {
    @FXML private TabPane reportsTabPane;
    @FXML private PieChart nationalityPieChart;
    @FXML private BarChart<String, Number> popularAttractionsChart;
    @FXML private LineChart<String, Number> revenueChart;
    @FXML private TableView<Map.Entry<String, Long>> nationalityTable;
    @FXML private TableColumn<Map.Entry<String, Long>, String> nationalityColumn;
    @FXML private TableColumn<Map.Entry<String, Long>, Number> countColumn;
    @FXML private TableView<String> popularAttractionsTable;
    @FXML private TableColumn<String, String> attractionNameColumn;
    @FXML private TableColumn<String, Number> bookingCountColumn;
    @FXML private Button exportTouristsBtn;
    @FXML private Button exportGuidesBtn;
    @FXML private Button exportAttractionsBtn;
    @FXML private Button exportBookingsBtn;
    @FXML private Button exportNationalityReportBtn;
    @FXML private Button exportPopularAttractionsBtn;
    @FXML private Button exportRevenueReportBtn;
    @FXML private Button exportFestivalImpactBtn;
    @FXML private Label totalRevenueLabel;
    @FXML private Label totalBookingsLabel;
    @FXML private Label averageBookingValueLabel;
    @FXML private Label activeFestivalsLabel;
    
    private DataService dataService;
    private ResourceBundleManager resourceManager;
    
    @FXML
    private void initialize() {
        dataService = DataService.getInstance();
        resourceManager = ResourceBundleManager.getInstance();
        
        setupCharts();
        setupTables();
        loadReportData();
        updateStatistics();
    }
    
    private void setupCharts() {
        // Setup nationality pie chart
        nationalityPieChart.setTitle("Tourist Distribution by Nationality");
        
        // Setup popular attractions bar chart
        popularAttractionsChart.setTitle("Most Popular Attractions");
        popularAttractionsChart.getXAxis().setLabel("Attractions");
        popularAttractionsChart.getYAxis().setLabel("Number of Bookings");
        
        // Setup revenue line chart
        revenueChart.setTitle("Monthly Revenue Trends");
        revenueChart.getXAxis().setLabel("Month");
        revenueChart.getYAxis().setLabel("Revenue (NPR)");
    }
    
    private void setupTables() {
        // Setup nationality table
        nationalityColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKey()));
        countColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getValue()));
        
        // Setup popular attractions table
        attractionNameColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue()));
        bookingCountColumn.setCellValueFactory(cellData -> {
            // Extract booking count from the string (assuming format: "Name - X bookings")
            String value = cellData.getValue();
            if (value.contains(" - ") && value.contains(" bookings")) {
                String countStr = value.substring(value.lastIndexOf(" - ") + 3, value.lastIndexOf(" bookings"));
                try {
                    return new javafx.beans.property.SimpleObjectProperty<>(Integer.parseInt(countStr));
                } catch (NumberFormatException e) {
                    return new javafx.beans.property.SimpleObjectProperty<>(0);
                }
            }
            return new javafx.beans.property.SimpleObjectProperty<>(0);
        });
    }
    
    private void loadReportData() {
        loadNationalityData();
        loadPopularAttractionsData();
        loadRevenueData();
    }
    
    private void loadNationalityData() {
        List<Tourist> tourists = dataService.getAllTourists();
        Map<String, Long> nationalityCount = tourists.stream()
                .collect(Collectors.groupingBy(Tourist::getNationality, Collectors.counting()));
        
        // Update pie chart
        nationalityPieChart.setData(FXCollections.observableArrayList(
                nationalityCount.entrySet().stream()
                        .map(entry -> new PieChart.Data(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList())
        ));
        
        // Update table
        nationalityTable.setItems(FXCollections.observableArrayList(nationalityCount.entrySet()));
    }
    
    private void loadPopularAttractionsData() {
        List<Booking> bookings = dataService.getAllBookings();
        Map<String, Long> attractionBookingCount = bookings.stream()
                .collect(Collectors.groupingBy(Booking::getAttractionId, Collectors.counting()));
        
        // Update bar chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Bookings");
        
        attractionBookingCount.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .limit(10) // Top 10 attractions
                .forEach(entry -> {
                    Attraction attraction = dataService.getAttraction(entry.getKey());
                    if (attraction != null) {
                        series.getData().add(new XYChart.Data<>(attraction.getName(), entry.getValue()));
                    }
                });
        
        popularAttractionsChart.getData().clear();
        popularAttractionsChart.getData().add(series);
        
        // Update table
        List<String> attractionData = attractionBookingCount.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .map(entry -> {
                    Attraction attraction = dataService.getAttraction(entry.getKey());
                    return attraction != null ? 
                           attraction.getName() + " - " + entry.getValue() + " bookings" : 
                           "Unknown - " + entry.getValue() + " bookings";
                })
                .collect(Collectors.toList());
        
        popularAttractionsTable.setItems(FXCollections.observableArrayList(attractionData));
    }
    
    private void loadRevenueData() {
        List<Booking> completedBookings = dataService.getAllBookings().stream()
                .filter(booking -> booking.getStatus() == Booking.BookingStatus.COMPLETED)
                .collect(Collectors.toList());
        
        Map<String, Double> monthlyRevenue = completedBookings.stream()
                .collect(Collectors.groupingBy(
                    booking -> booking.getBookingDate().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                    Collectors.summingDouble(Booking::getFinalAmount)
                ));
        
        // Update line chart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue");

        monthlyRevenue.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())  // <-- note the () to call the method, no args for natural order
                .forEach(entry -> series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue())));


        revenueChart.getData().clear();
        revenueChart.getData().add(series);
    }
    
    private void updateStatistics() {
        List<Booking> completedBookings = dataService.getAllBookings().stream()
                .filter(booking -> booking.getStatus() == Booking.BookingStatus.COMPLETED)
                .collect(Collectors.toList());
        
        double totalRevenue = completedBookings.stream()
                .mapToDouble(Booking::getFinalAmount)
                .sum();
        
        int totalBookings = completedBookings.size();
        double averageBookingValue = totalBookings > 0 ? totalRevenue / totalBookings : 0;
        
        long activeFestivals = dataService.getAllFestivals().stream()
                .filter(Festival::isCurrentlyActive)
                .count();
        
        totalRevenueLabel.setText(String.format("NPR %.2f", totalRevenue));
        totalBookingsLabel.setText(String.valueOf(totalBookings));
        averageBookingValueLabel.setText(String.format("NPR %.2f", averageBookingValue));
        activeFestivalsLabel.setText(String.valueOf(activeFestivals));
    }
    
    @FXML
    private void exportTourists() {
        exportToCSV("Export Tourists", "tourists.csv", () -> CSVExporter.exportTourists(getSelectedFile().getAbsolutePath()));
    }
    
    @FXML
    private void exportGuides() {
        exportToCSV("Export Guides", "guides.csv", () -> CSVExporter.exportGuides(getSelectedFile().getAbsolutePath()));
    }
    
    @FXML
    private void exportAttractions() {
        exportToCSV("Export Attractions", "attractions.csv", () -> CSVExporter.exportAttractions(getSelectedFile().getAbsolutePath()));
    }
    
    @FXML
    private void exportBookings() {
        exportToCSV("Export Bookings", "bookings.csv", () -> CSVExporter.exportBookings(getSelectedFile().getAbsolutePath()));
    }
    
    @FXML
    private void exportNationalityReport() {
        exportToCSV("Export Nationality Report", "nationality_report.csv", 
                   () -> CSVExporter.exportTouristNationalityReport(getSelectedFile().getAbsolutePath()));
    }
    
    @FXML
    private void exportPopularAttractions() {
        exportToCSV("Export Popular Attractions", "popular_attractions.csv", 
                   () -> CSVExporter.exportPopularAttractionsReport(getSelectedFile().getAbsolutePath()));
    }
    
    @FXML
    private void exportRevenueReport() {
        exportToCSV("Export Revenue Report", "revenue_report.csv", 
                   () -> CSVExporter.exportRevenueReport(getSelectedFile().getAbsolutePath()));
    }
    
    @FXML
    private void exportFestivalImpact() {
        exportToCSV("Export Festival Impact", "festival_impact.csv", 
                   () -> CSVExporter.exportFestivalImpactReport(getSelectedFile().getAbsolutePath()));
    }
    
    private File selectedFile;
    
    private File getSelectedFile() {
        return selectedFile;
    }
    
    private void exportToCSV(String title, String defaultFileName, CSVExportTask task) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialFileName(defaultFileName);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        
        Stage stage = (Stage) exportTouristsBtn.getScene().getWindow();
        selectedFile = fileChooser.showSaveDialog(stage);
        
        if (selectedFile != null) {
            try {
                task.export();
                showSuccessAlert("Export Successful", 
                               "Data has been successfully exported to:\n" + selectedFile.getAbsolutePath());
            } catch (Exception e) {
                showErrorAlert("Export Failed", 
                             "Failed to export data: " + e.getMessage());
            }
        }
    }
    
    @FunctionalInterface
    private interface CSVExportTask {
        void export() throws Exception;
    }
    
    @FXML
    private void refreshReports() {
        loadReportData();
        updateStatistics();
        showInfoAlert("Reports Refreshed", "All reports have been updated with the latest data.");
    }
    
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

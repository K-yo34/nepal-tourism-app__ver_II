package com.nepaltourism.controller;

import com.nepaltourism.model.*;
import com.nepaltourism.service.DataService;
import com.nepaltourism.util.ResourceBundleManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class DashboardController {
    @FXML private Label welcomeLabel;
    @FXML private Label totalTouristsLabel;
    @FXML private Label totalGuidesLabel;
    @FXML private Label totalBookingsLabel;
    @FXML private Label totalAttractionsLabel;
    @FXML private PieChart nationalityChart;
    @FXML private ListView<String> recentBookingsListView;
    @FXML private ListView<String> activeFestivalsListView;
    @FXML private Button touristsButton;
    @FXML private Button guidesButton;
    @FXML private Button attractionsButton;
    @FXML private Button bookingsButton;
    @FXML private Button festivalsButton;
    @FXML private Button reportsButton;
    @FXML private Button safetyButton;
    @FXML private ComboBox<String> languageComboBox;
    @FXML private Button logoutButton; // Add logout button
    
    private DataService dataService;
    private ResourceBundleManager resourceManager;
    private Admin currentAdmin;
    
    @FXML
    private void initialize() {
        dataService = DataService.getInstance();
        resourceManager = ResourceBundleManager.getInstance();
        
        setupLanguageComboBox();
        setupLogoutButton();
        refreshDashboard();
    }
    
    public void setCurrentAdmin(Admin admin) {
        this.currentAdmin = admin;
        welcomeLabel.setText("Welcome, " + admin.getFullName());
    }
    
    private void setupLanguageComboBox() {
        languageComboBox.getItems().addAll("English", "नेपाली");
        languageComboBox.setValue("English");
        languageComboBox.setOnAction(e -> changeLanguage());
    }
    
    private void setupLogoutButton() {
        if (logoutButton != null) {
            logoutButton.setText("Logout");
            logoutButton.setOnAction(e -> logout());
        }
    }
    
    private void changeLanguage() {
        String selected = languageComboBox.getValue();
        if ("नेपाली".equals(selected)) {
            resourceManager.setLocale("ne", "NP");
        } else {
            resourceManager.setLocale("en", "US");
        }
        updateLabels();
    }
    
    private void updateLabels() {
        touristsButton.setText(resourceManager.getString("dashboard.tourists"));
        guidesButton.setText(resourceManager.getString("dashboard.guides"));
        attractionsButton.setText(resourceManager.getString("dashboard.attractions"));
        bookingsButton.setText(resourceManager.getString("dashboard.bookings"));
        festivalsButton.setText(resourceManager.getString("dashboard.festivals"));
        reportsButton.setText(resourceManager.getString("dashboard.reports"));
        safetyButton.setText(resourceManager.getString("dashboard.safety"));
    }
    
    private void refreshDashboard() {
        updateStatistics();
        updateNationalityChart();
        updateRecentBookings();
        updateActiveFestivals();
    }
    
    private void updateStatistics() {
        totalTouristsLabel.setText(String.valueOf(dataService.getAllTourists().size()));
        totalGuidesLabel.setText(String.valueOf(dataService.getAllGuides().size()));
        totalBookingsLabel.setText(String.valueOf(dataService.getAllBookings().size()));
        totalAttractionsLabel.setText(String.valueOf(dataService.getAllAttractions().size()));
    }
    
    private void updateNationalityChart() {
        Map<String, Long> nationalityCount = dataService.getAllTourists().stream()
                .collect(Collectors.groupingBy(Tourist::getNationality, Collectors.counting()));
        
        nationalityChart.setData(FXCollections.observableArrayList(
                nationalityCount.entrySet().stream()
                        .map(entry -> new PieChart.Data(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList())
        ));
    }
    
    private void updateRecentBookings() {
        recentBookingsListView.getItems().clear();
        dataService.getAllBookings().stream()
                .sorted((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()))
                .limit(5)
                .forEach(booking -> {
                    Tourist tourist = dataService.getTourist(booking.getTouristId());
                    Attraction attraction = dataService.getAttraction(booking.getAttractionId());
                    String item = String.format("%s - %s (%s)", 
                            booking.getBookingId(),
                            tourist != null ? tourist.getName() : "Unknown",
                            attraction != null ? attraction.getName() : "Unknown");
                    recentBookingsListView.getItems().add(item);
                });
    }
    
    private void updateActiveFestivals() {
        activeFestivalsListView.getItems().clear();
        dataService.getAllFestivals().stream()
                .filter(Festival::isCurrentlyActive)
                .forEach(festival -> {
                    String item = String.format("%s - %.1f%% discount", 
                            festival.getName(), festival.getDiscountPercentage());
                    activeFestivalsListView.getItems().add(item);
                });
    }
    
    @FXML
    private void openTourists() {
        openTouristManagement();
    }
    
    @FXML
    private void openGuides() {
        openGuideManagement();
    }
    
    @FXML
    private void openAttractions() {
        openAttractionManagement();
    }
    
    @FXML
    private void openBookings() {
        openBookingManagement();
    }
    
    @FXML
    private void openFestivals() {
        openFestivalManagement();
    }
    
    @FXML
    private void openReports() {
        openWindow("/fxml/reports.fxml", "Reports & Analytics", 900, 700);
    }
    
    @FXML
    private void openSafety() {
        openSafetyManagement();
    }
    
    @FXML
    private void logout() {
        // Confirm logout
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Logout Confirmation");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Load login screen
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
                Parent root = loader.load();
                
                // Get current stage
                Stage currentStage = (Stage) welcomeLabel.getScene().getWindow();
                
                // Set login scene
                Scene loginScene = new Scene(root, 400, 500);
                currentStage.setScene(loginScene);
                currentStage.setTitle("Nepal Tourism - Login");
                currentStage.centerOnScreen();
                
                showAlert("Logged Out", "You have been successfully logged out.");
            } catch (IOException e) {
                showAlert("Error", "Could not return to login screen: " + e.getMessage());
            }
        }
    }
    
    // Create simple management windows without separate FXML files
    private void openTouristManagement() {
        Stage stage = new Stage();
        stage.setTitle("Tourist Management");
        stage.initModality(Modality.APPLICATION_MODAL);
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        
        Label title = new Label("Tourist Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Tourist table
        TableView<Tourist> table = new TableView<>();
        
        TableColumn<Tourist, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTouristId()));
        
        TableColumn<Tourist, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        
        TableColumn<Tourist, String> nationalityCol = new TableColumn<>("Nationality");
        nationalityCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNationality()));
        
        TableColumn<Tourist, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getContact()));
        
        table.getColumns().addAll(idCol, nameCol, nationalityCol, contactCol);
        table.getItems().addAll(dataService.getAllTourists());
        
        // Add form
        HBox addForm = new HBox(10);
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField nationalityField = new TextField();
        nationalityField.setPromptText("Nationality");
        TextField contactField = new TextField();
        contactField.setPromptText("Contact");
        
        Button addBtn = new Button("Add Tourist");
        addBtn.setOnAction(e -> {
            if (!nameField.getText().isEmpty() && !nationalityField.getText().isEmpty()) {
                Tourist tourist = new Tourist();
                tourist.setTouristId("T" + (dataService.getAllTourists().size() + 1));
                tourist.setName(nameField.getText());
                tourist.setNationality(nationalityField.getText());
                tourist.setContact(contactField.getText());
                
                dataService.addTourist(tourist);
                table.getItems().add(tourist);
                
                nameField.clear();
                nationalityField.clear();
                contactField.clear();
                
                refreshDashboard();
            }
        });
        
        addForm.getChildren().addAll(nameField, nationalityField, contactField, addBtn);
        
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> stage.close());
        
        root.getChildren().addAll(title, addForm, table, closeBtn);
        
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.showAndWait();
        
        refreshDashboard();
    }
    
    private void openGuideManagement() {
        Stage stage = new Stage();
        stage.setTitle("Guide Management");
        stage.initModality(Modality.APPLICATION_MODAL);
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        
        Label title = new Label("Guide Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Guide table
        TableView<Guide> table = new TableView<>();
        
        TableColumn<Guide, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getGuideId()));
        
        TableColumn<Guide, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        
        TableColumn<Guide, String> languagesCol = new TableColumn<>("Languages");
        languagesCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.join(", ", data.getValue().getLanguages())));

        TableColumn<Guide, String> experienceCol = new TableColumn<>("Experience");
        experienceCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getExperienceYears() + " years"));


        table.getColumns().addAll(idCol, nameCol, languagesCol, experienceCol);
        table.getItems().addAll(dataService.getAllGuides());
        
        // Add form
        HBox addForm = new HBox(10);
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField languagesField = new TextField();
        languagesField.setPromptText("Languages (comma separated)");
        TextField experienceField = new TextField();
        experienceField.setPromptText("Experience (years)");
        
        Button addBtn = new Button("Add Guide");
        addBtn.setOnAction(e -> {
            if (!nameField.getText().isEmpty()) {
                try {
                    Guide guide = new Guide();
                    guide.setGuideId("G" + (dataService.getAllGuides().size() + 1));
                    guide.setName(nameField.getText());
                    guide.setLanguages(Arrays.asList(languagesField.getText().split(",")));
                    guide.setExperienceYears(Integer.parseInt(experienceField.getText().isEmpty() ? "0" : experienceField.getText()));
                    
                    dataService.addGuide(guide);
                    table.getItems().add(guide);
                    
                    nameField.clear();
                    languagesField.clear();
                    experienceField.clear();
                    
                    refreshDashboard();
                } catch (NumberFormatException ex) {
                    showAlert("Error", "Please enter a valid number for experience years.");
                }
            }
        });
        
        addForm.getChildren().addAll(nameField, languagesField, experienceField, addBtn);
        
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> stage.close());
        
        root.getChildren().addAll(title, addForm, table, closeBtn);
        
        Scene scene = new Scene(root, 700, 400);
        stage.setScene(scene);
        stage.showAndWait();
        
        refreshDashboard();
    }

    private void openAttractionManagement() {
        Stage stage = new Stage();
        stage.setTitle("Attraction Management");
        stage.initModality(Modality.APPLICATION_MODAL);

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label title = new Label("Attraction Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Attraction table
        TableView<Attraction> table = new TableView<>();

        TableColumn<Attraction, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAttractionId()));

        TableColumn<Attraction, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        TableColumn<Attraction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getType().toString()));

        TableColumn<Attraction, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLocation()));

        TableColumn<Attraction, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                "NPR " + String.format("%.2f", data.getValue().getPrice())));

        table.getColumns().addAll(idCol, nameCol, typeCol, locationCol, priceCol);
        table.getItems().addAll(dataService.getAllAttractions());

        // Add form
        HBox addForm = new HBox(10);
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        ComboBox<String> typeField = new ComboBox<>();
        typeField.getItems().addAll("Trek", "Heritage", "Adventure", "Cultural");
        typeField.setPromptText("Type");
        TextField locationField = new TextField();
        locationField.setPromptText("Location");
        TextField priceField = new TextField();
        priceField.setPromptText("Price (NPR)");

        Button addBtn = new Button("Add Attraction");
        addBtn.setOnAction(e -> {
            if (!nameField.getText().isEmpty() && typeField.getValue() != null) {
                try {
                    Attraction attraction = new Attraction();
                    attraction.setAttractionId("A" + (dataService.getAllAttractions().size() + 1));
                    attraction.setName(nameField.getText());

                    // Convert String to enum
                    Attraction.AttractionType selectedType = Attraction.AttractionType.valueOf(typeField.getValue());
                    attraction.setType(selectedType);

                    attraction.setLocation(locationField.getText());
                    attraction.setPrice(Double.parseDouble(priceField.getText().isEmpty() ? "0" : priceField.getText()));

                    dataService.addAttraction(attraction);
                    table.getItems().add(attraction);

                    nameField.clear();
                    typeField.setValue(null);
                    locationField.clear();
                    priceField.clear();

                    refreshDashboard();
                } catch (NumberFormatException ex) {
                    showAlert("Error", "Please enter a valid number for price.");
                } catch (IllegalArgumentException ex) {
                    showAlert("Error", "Please select a valid attraction type.");
                }
            } else {
                showAlert("Error", "Name and Type are required.");
            }
        });

        // Add the form controls only once here
        addForm.getChildren().addAll(nameField, typeField, locationField, priceField, addBtn);

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> stage.close());

        root.getChildren().addAll(title, addForm, table, closeBtn);

        Scene scene = new Scene(root, 800, 400);
        stage.setScene(scene);
        stage.showAndWait();

        refreshDashboard();
    }


    private void openBookingManagement() {
        Stage stage = new Stage();
        stage.setTitle("Booking Management");
        stage.initModality(Modality.APPLICATION_MODAL);
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        
        Label title = new Label("Booking Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Booking table
        TableView<Booking> table = new TableView<>();
        
        TableColumn<Booking, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getBookingId()));
        
        TableColumn<Booking, String> touristCol = new TableColumn<>("Tourist");
        touristCol.setCellValueFactory(data -> {
            Tourist tourist = dataService.getTourist(data.getValue().getTouristId());
            return new javafx.beans.property.SimpleStringProperty(tourist != null ? tourist.getName() : "Unknown");
        });
        
        TableColumn<Booking, String> attractionCol = new TableColumn<>("Attraction");
        attractionCol.setCellValueFactory(data -> {
            Attraction attraction = dataService.getAttraction(data.getValue().getAttractionId());
            return new javafx.beans.property.SimpleStringProperty(attraction != null ? attraction.getName() : "Unknown");
        });
        
        TableColumn<Booking, String> dateCol = new TableColumn<>("Travel Date");
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTravelDate().toString()));
        
        table.getColumns().addAll(idCol, touristCol, attractionCol, dateCol);
        table.getItems().addAll(dataService.getAllBookings());
        
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> stage.close());
        
        root.getChildren().addAll(title, table, closeBtn);
        
        Scene scene = new Scene(root, 700, 400);
        stage.setScene(scene);
        stage.showAndWait();
        
        refreshDashboard();
    }
    
    private void openFestivalManagement() {
        Stage stage = new Stage();
        stage.setTitle("Festival Management");
        stage.initModality(Modality.APPLICATION_MODAL);
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        
        Label title = new Label("Festival Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Festival table
        TableView<Festival> table = new TableView<>();
        
        TableColumn<Festival, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFestivalId()));
        
        TableColumn<Festival, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        
        TableColumn<Festival, String> discountCol = new TableColumn<>("Discount %");
        discountCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.format("%.1f%%", data.getValue().getDiscountPercentage())));
        
        TableColumn<Festival, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().isCurrentlyActive() ? "Active" : "Inactive"));
        
        table.getColumns().addAll(idCol, nameCol, discountCol, statusCol);
        table.getItems().addAll(dataService.getAllFestivals());
        
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> stage.close());
        
        root.getChildren().addAll(title, table, closeBtn);
        
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.showAndWait();
        
        refreshDashboard();
    }
    
    private void openSafetyManagement() {
        Stage stage = new Stage();
        stage.setTitle("Safety Management");
        stage.initModality(Modality.APPLICATION_MODAL);
        
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        
        Label title = new Label("Safety Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        TextArea safetyInfo = new TextArea();
        safetyInfo.setText("Emergency Contacts:\n\n" +
                          "Tourist Police: 1144\n" +
                          "Nepal Police: 100\n" +
                          "Fire Brigade: 101\n" +
                          "Ambulance: 102\n\n" +
                          "Safety Guidelines:\n" +
                          "• Always inform someone about your travel plans\n" +
                          "• Carry emergency contact information\n" +
                          "• Check weather conditions before trekking\n" +
                          "• Use licensed guides for high-altitude treks");
        safetyInfo.setEditable(false);
        safetyInfo.setPrefRowCount(15);
        
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> stage.close());
        
        root.getChildren().addAll(title, safetyInfo, closeBtn);
        
        Scene scene = new Scene(root, 500, 400);
        stage.setScene(scene);
        stage.showAndWait();
    }
    
    private void openWindow(String fxmlPath, String title, int width, int height) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            // Refresh dashboard after window closes
            refreshDashboard();
        } catch (Exception e) {
            showAlert("Error", "Could not open " + title + ": " + e.getMessage());
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

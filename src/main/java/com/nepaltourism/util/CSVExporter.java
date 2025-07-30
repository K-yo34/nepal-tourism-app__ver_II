package com.nepaltourism.util;

import com.nepaltourism.model.*;
import com.nepaltourism.service.DataService;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CSVExporter {
    private static final String CSV_SEPARATOR = ",";
    private static final String CSV_HEADER_SEPARATOR = "\n";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public static void exportTourists(String filePath) throws IOException {
        DataService dataService = DataService.getInstance();
        List<Tourist> tourists = dataService.getAllTourists();
        
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append("Tourist ID,Name,Nationality,Contact,Emergency Contact,Email,Passport Number,Registration Date");
            writer.append(CSV_HEADER_SEPARATOR);
            
            // Write data
            for (Tourist tourist : tourists) {
                writer.append(escapeCSV(tourist.getTouristId()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(tourist.getName()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(tourist.getNationality()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(tourist.getContact()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(tourist.getEmergencyContact()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(tourist.getEmail()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(tourist.getPassportNumber()));
                writer.append(CSV_SEPARATOR);
                writer.append(tourist.getRegistrationDate().format(DATE_FORMATTER));
                writer.append(CSV_HEADER_SEPARATOR);
            }
        }
    }
    
    public static void exportGuides(String filePath) throws IOException {
        DataService dataService = DataService.getInstance();
        List<Guide> guides = dataService.getAllGuides();
        
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append("Guide ID,Name,Languages,Experience Years,Contact,Email,Specialization,License Number,Rating,Available");
            writer.append(CSV_HEADER_SEPARATOR);
            
            // Write data
            for (Guide guide : guides) {
                writer.append(escapeCSV(guide.getGuideId()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(guide.getName()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(String.join(";", guide.getLanguages())));
                writer.append(CSV_SEPARATOR);
                writer.append(String.valueOf(guide.getExperienceYears()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(guide.getContact()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(guide.getEmail()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(guide.getSpecialization()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(guide.getLicenseNumber()));
                writer.append(CSV_SEPARATOR);
                writer.append(String.valueOf(guide.getRating()));
                writer.append(CSV_SEPARATOR);
                writer.append(String.valueOf(guide.isAvailable()));
                writer.append(CSV_HEADER_SEPARATOR);
            }
        }
    }
    
    public static void exportAttractions(String filePath) throws IOException {
        DataService dataService = DataService.getInstance();
        List<Attraction> attractions = dataService.getAllAttractions();
        
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append("Attraction ID,Name,Type,Location,Difficulty,Description,Price,Altitude,Duration Days,Active");
            writer.append(CSV_HEADER_SEPARATOR);
            
            // Write data
            for (Attraction attraction : attractions) {
                writer.append(escapeCSV(attraction.getAttractionId()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(attraction.getName()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(attraction.getType().toString()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(attraction.getLocation()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(attraction.getDifficulty().toString()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(attraction.getDescription()));
                writer.append(CSV_SEPARATOR);
                writer.append(String.valueOf(attraction.getPrice()));
                writer.append(CSV_SEPARATOR);
                writer.append(String.valueOf(attraction.getAltitude()));
                writer.append(CSV_SEPARATOR);
                writer.append(String.valueOf(attraction.getDurationDays()));
                writer.append(CSV_SEPARATOR);
                writer.append(String.valueOf(attraction.isActive()));
                writer.append(CSV_HEADER_SEPARATOR);
            }
        }
    }
    
    public static void exportBookings(String filePath) throws IOException {
        DataService dataService = DataService.getInstance();
        List<Booking> bookings = dataService.getAllBookings();
        
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append("Booking ID,Tourist ID,Tourist Name,Guide ID,Guide Name,Attraction ID,Attraction Name,Booking Date,Travel Date,Status,Total Amount,Discount Amount,Final Amount,Notes,Created At,Emergency Reported");
            writer.append(CSV_HEADER_SEPARATOR);
            
            // Write data
            for (Booking booking : bookings) {
                Tourist tourist = dataService.getTourist(booking.getTouristId());
                Guide guide = dataService.getGuide(booking.getGuideId());
                Attraction attraction = dataService.getAttraction(booking.getAttractionId());
                
                writer.append(escapeCSV(booking.getBookingId()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(booking.getTouristId()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(tourist != null ? tourist.getName() : "Unknown"));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(booking.getGuideId()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(guide != null ? guide.getName() : "Unknown"));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(booking.getAttractionId()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(attraction != null ? attraction.getName() : "Unknown"));
                writer.append(CSV_SEPARATOR);
                writer.append(booking.getBookingDate().format(DATE_FORMATTER));
                writer.append(CSV_SEPARATOR);
                writer.append(booking.getTravelDate().format(DATE_FORMATTER));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(booking.getStatus().toString()));
                writer.append(CSV_SEPARATOR);
                writer.append(String.valueOf(booking.getTotalAmount()));
                writer.append(CSV_SEPARATOR);
                writer.append(String.valueOf(booking.getDiscountAmount()));
                writer.append(CSV_SEPARATOR);
                writer.append(String.valueOf(booking.getFinalAmount()));
                writer.append(CSV_SEPARATOR);
                writer.append(escapeCSV(booking.getNotes()));
                writer.append(CSV_SEPARATOR);
                writer.append(booking.getCreatedAt().format(DATETIME_FORMATTER));
                writer.append(CSV_SEPARATOR);
                writer.append(String.valueOf(booking.isEmergencyReported()));
                writer.append(CSV_HEADER_SEPARATOR);
            }
        }
    }
    
    public static void exportTouristNationalityReport(String filePath) throws IOException {
        DataService dataService = DataService.getInstance();
        List<Tourist> tourists = dataService.getAllTourists();
        
        Map<String, Long> nationalityCount = tourists.stream()
                .collect(Collectors.groupingBy(Tourist::getNationality, Collectors.counting()));
        
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append("Nationality,Tourist Count,Percentage");
            writer.append(CSV_HEADER_SEPARATOR);
            
            long totalTourists = tourists.size();
            
            // Write data
            for (Map.Entry<String, Long> entry : nationalityCount.entrySet()) {
                writer.append(escapeCSV(entry.getKey()));
                writer.append(CSV_SEPARATOR);
                writer.append(String.valueOf(entry.getValue()));
                writer.append(CSV_SEPARATOR);
                double percentage = (entry.getValue() * 100.0) / totalTourists;
                writer.append(String.format("%.2f%%", percentage));
                writer.append(CSV_HEADER_SEPARATOR);
            }
        }
    }
    
    public static void exportPopularAttractionsReport(String filePath) throws IOException {
        DataService dataService = DataService.getInstance();
        List<Booking> bookings = dataService.getAllBookings();
        
        Map<String, Long> attractionBookingCount = bookings.stream()
                .collect(Collectors.groupingBy(Booking::getAttractionId, Collectors.counting()));
        
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append("Attraction ID,Attraction Name,Booking Count,Type,Location,Price");
            writer.append(CSV_HEADER_SEPARATOR);
            
            // Write data sorted by booking count
            attractionBookingCount.entrySet().stream()
                    .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                    .forEach(entry -> {
                        try {
                            Attraction attraction = dataService.getAttraction(entry.getKey());
                            if (attraction != null) {
                                writer.append(escapeCSV(attraction.getAttractionId()));
                                writer.append(CSV_SEPARATOR);
                                writer.append(escapeCSV(attraction.getName()));
                                writer.append(CSV_SEPARATOR);
                                writer.append(String.valueOf(entry.getValue()));
                                writer.append(CSV_SEPARATOR);
                                writer.append(escapeCSV(attraction.getType().toString()));
                                writer.append(CSV_SEPARATOR);
                                writer.append(escapeCSV(attraction.getLocation()));
                                writer.append(CSV_SEPARATOR);
                                writer.append(String.valueOf(attraction.getPrice()));
                                writer.append(CSV_HEADER_SEPARATOR);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }
    
    public static void exportRevenueReport(String filePath) throws IOException {
        DataService dataService = DataService.getInstance();
        List<Booking> bookings = dataService.getAllBookings();
        
        Map<String, Double> monthlyRevenue = bookings.stream()
                .filter(booking -> booking.getStatus() == Booking.BookingStatus.COMPLETED)
                .collect(Collectors.groupingBy(
                    booking -> booking.getBookingDate().getYear() + "-" + 
                              String.format("%02d", booking.getBookingDate().getMonthValue()),
                    Collectors.summingDouble(Booking::getFinalAmount)
                ));
        
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append("Month,Revenue,Bookings Count,Average Booking Value");
            writer.append(CSV_HEADER_SEPARATOR);
            
            // Write data
            for (Map.Entry<String, Double> entry : monthlyRevenue.entrySet()) {
                String month = entry.getKey();
                double revenue = entry.getValue();
                
                long bookingCount = bookings.stream()
                        .filter(booking -> booking.getStatus() == Booking.BookingStatus.COMPLETED)
                        .filter(booking -> {
                            String bookingMonth = booking.getBookingDate().getYear() + "-" + 
                                                String.format("%02d", booking.getBookingDate().getMonthValue());
                            return bookingMonth.equals(month);
                        })
                        .count();
                
                double averageValue = bookingCount > 0 ? revenue / bookingCount : 0;
                
                writer.append(escapeCSV(month));
                writer.append(CSV_SEPARATOR);
                writer.append(String.format("%.2f", revenue));
                writer.append(CSV_SEPARATOR);
                writer.append(String.valueOf(bookingCount));
                writer.append(CSV_SEPARATOR);
                writer.append(String.format("%.2f", averageValue));
                writer.append(CSV_HEADER_SEPARATOR);
            }
        }
    }
    
    public static void exportFestivalImpactReport(String filePath) throws IOException {
        DataService dataService = DataService.getInstance();
        List<Booking> bookings = dataService.getAllBookings();
        List<Festival> festivals = dataService.getAllFestivals();
        
        try (FileWriter writer = new FileWriter(filePath)) {
            // Write header
            writer.append("Festival Name,Start Date,End Date,Discount %,Bookings During Festival,Total Discount Given,Revenue Impact");
            writer.append(CSV_HEADER_SEPARATOR);
            
            // Write data for each festival
            for (Festival festival : festivals) {
                long festivalBookings = bookings.stream()
                        .filter(booking -> !booking.getTravelDate().isBefore(festival.getStartDate()) && 
                                         !booking.getTravelDate().isAfter(festival.getEndDate()))
                        .count();
                
                double totalDiscount = bookings.stream()
                        .filter(booking -> !booking.getTravelDate().isBefore(festival.getStartDate()) && 
                                         !booking.getTravelDate().isAfter(festival.getEndDate()))
                        .mapToDouble(Booking::getDiscountAmount)
                        .sum();
                
                double revenueImpact = bookings.stream()
                        .filter(booking -> !booking.getTravelDate().isBefore(festival.getStartDate()) && 
                                         !booking.getTravelDate().isAfter(festival.getEndDate()))
                        .mapToDouble(Booking::getFinalAmount)
                        .sum();
                
                writer.append(escapeCSV(festival.getName()));
                writer.append(CSV_SEPARATOR);
                writer.append(festival.getStartDate().format(DATE_FORMATTER));
                writer.append(CSV_SEPARATOR);
                writer.append(festival.getEndDate().format(DATE_FORMATTER));
                writer.append(CSV_SEPARATOR);
                writer.append(String.valueOf(festival.getDiscountPercentage()));
                writer.append(CSV_SEPARATOR);
                writer.append(String.valueOf(festivalBookings));
                writer.append(CSV_SEPARATOR);
                writer.append(String.format("%.2f", totalDiscount));
                writer.append(CSV_SEPARATOR);
                writer.append(String.format("%.2f", revenueImpact));
                writer.append(CSV_HEADER_SEPARATOR);
            }
        }
    }
    
    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        
        // Escape quotes and wrap in quotes if contains comma, quote, or newline
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        
        return value;
    }
}

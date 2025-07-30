package com.nepaltourism.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum BookingStatus {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }
    
    private String bookingId;
    private String touristId;
    private String guideId;
    private String attractionId;
    private LocalDate bookingDate;
    private LocalDate travelDate;
    private BookingStatus status;
    private double totalAmount;
    private double discountAmount;
    private String notes;
    private LocalDateTime createdAt;
    private boolean emergencyReported;
    
    public Booking() {
        this.createdAt = LocalDateTime.now();
        this.status = BookingStatus.PENDING;
        this.emergencyReported = false;
    }
    
    public Booking(String bookingId, String touristId, String guideId, String attractionId,
                   LocalDate bookingDate, LocalDate travelDate, double totalAmount) {
        this.bookingId = bookingId;
        this.touristId = touristId;
        this.guideId = guideId;
        this.attractionId = attractionId;
        this.bookingDate = bookingDate;
        this.travelDate = travelDate;
        this.totalAmount = totalAmount;
        this.status = BookingStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.emergencyReported = false;
        this.discountAmount = 0.0;
    }
    
    // Getters and Setters
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    
    public String getTouristId() { return touristId; }
    public void setTouristId(String touristId) { this.touristId = touristId; }
    
    public String getGuideId() { return guideId; }
    public void setGuideId(String guideId) { this.guideId = guideId; }
    
    public String getAttractionId() { return attractionId; }
    public void setAttractionId(String attractionId) { this.attractionId = attractionId; }
    
    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    
    public LocalDate getTravelDate() { return travelDate; }
    public void setTravelDate(LocalDate travelDate) { this.travelDate = travelDate; }
    
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public double getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public boolean isEmergencyReported() { return emergencyReported; }
    public void setEmergencyReported(boolean emergencyReported) { this.emergencyReported = emergencyReported; }
    
    public double getFinalAmount() {
        return totalAmount - discountAmount;
    }
    
    @Override
    public String toString() {
        return bookingId + " - " + status;
    }
}

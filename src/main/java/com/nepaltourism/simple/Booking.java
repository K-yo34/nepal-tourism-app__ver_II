package com.nepaltourism.simple;

import java.io.Serializable;
import java.time.LocalDate;

public class Booking implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String bookingId;
    private Tourist tourist;
    private Guide guide;
    private Attraction attraction;
    private LocalDate travelDate;
    private double price;
    private double discount;
    
    public Booking(String bookingId, Tourist tourist, Guide guide, Attraction attraction, 
                   LocalDate travelDate, double price, double discount) {
        this.bookingId = bookingId;
        this.tourist = tourist;
        this.guide = guide;
        this.attraction = attraction;
        this.travelDate = travelDate;
        this.price = price;
        this.discount = discount;
    }
    
    // Getters and Setters
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    
    public Tourist getTourist() { return tourist; }
    public void setTourist(Tourist tourist) { this.tourist = tourist; }
    
    public Guide getGuide() { return guide; }
    public void setGuide(Guide guide) { this.guide = guide; }
    
    public Attraction getAttraction() { return attraction; }
    public void setAttraction(Attraction attraction) { this.attraction = attraction; }
    
    public LocalDate getTravelDate() { return travelDate; }
    public void setTravelDate(LocalDate travelDate) { this.travelDate = travelDate; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
    
    @Override
    public String toString() {
        return bookingId + " - " + tourist.getName() + " to " + attraction.getName();
    }
}

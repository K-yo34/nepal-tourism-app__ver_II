package com.nepaltourism.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Festival implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String festivalId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private double discountPercentage;
    private String description;
    private boolean isActive;
    
    public Festival() {
        this.isActive = true;
    }
    
    public Festival(String festivalId, String name, LocalDate startDate, LocalDate endDate,
                   double discountPercentage, String description) {
        this.festivalId = festivalId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountPercentage = discountPercentage;
        this.description = description;
        this.isActive = true;
    }
    
    // Getters and Setters
    public String getFestivalId() { return festivalId; }
    public void setFestivalId(String festivalId) { this.festivalId = festivalId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public double getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(double discountPercentage) { this.discountPercentage = discountPercentage; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public boolean isCurrentlyActive() {
        LocalDate today = LocalDate.now();
        return isActive && !today.isBefore(startDate) && !today.isAfter(endDate);
    }
    
    @Override
    public String toString() {
        return name + " (" + discountPercentage + "% off)";
    }
}

package com.nepaltourism.model;

import java.io.Serializable;

public class Attraction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum AttractionType {
        TREK, HERITAGE, ADVENTURE, CULTURAL, RELIGIOUS, NATURAL
    }
    
    public enum DifficultyLevel {
        EASY, MODERATE, DIFFICULT, EXTREME
    }
    
    private String attractionId;
    private String name;
    private AttractionType type;
    private String location;
    private DifficultyLevel difficulty;
    private String description;
    private double price;
    private int altitude; // in meters
    private int durationDays;
    private boolean isActive;
    
    public Attraction() {
        this.isActive = true;
    }
    
    public Attraction(String attractionId, String name, AttractionType type, String location,
                     DifficultyLevel difficulty, String description, double price, 
                     int altitude, int durationDays) {
        this.attractionId = attractionId;
        this.name = name;
        this.type = type;
        this.location = location;
        this.difficulty = difficulty;
        this.description = description;
        this.price = price;
        this.altitude = altitude;
        this.durationDays = durationDays;
        this.isActive = true;
    }
    
    // Getters and Setters
    public String getAttractionId() { return attractionId; }
    public void setAttractionId(String attractionId) { this.attractionId = attractionId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public AttractionType getType() { return type; }
    public void setType(AttractionType type) { this.type = type; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public DifficultyLevel getDifficulty() { return difficulty; }
    public void setDifficulty(DifficultyLevel difficulty) { this.difficulty = difficulty; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public int getAltitude() { return altitude; }
    public void setAltitude(int altitude) { this.altitude = altitude; }
    
    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public boolean isHighAltitude() {
        return altitude > 3000;
    }
    
    @Override
    public String toString() {
        return name + " (" + location + ")";
    }
}

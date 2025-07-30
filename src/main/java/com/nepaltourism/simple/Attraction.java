package com.nepaltourism.simple;

import java.io.Serializable;

public class Attraction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String name;
    private String type;
    private String location;
    private double price;
    private int altitude;
    
    public Attraction(String id, String name, String type, String location, double price, int altitude) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;
        this.price = price;
        this.altitude = altitude;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public int getAltitude() { return altitude; }
    public void setAltitude(int altitude) { this.altitude = altitude; }
    
    @Override
    public String toString() {
        return name + " (" + location + ")";
    }
}

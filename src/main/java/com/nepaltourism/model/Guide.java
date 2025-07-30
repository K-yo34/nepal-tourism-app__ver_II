package com.nepaltourism.model;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Guide implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String guideId;
    private String name;
    private List<String> languages;
    private int experienceYears;
    private String contact;
    private String email;
    private String specialization;
    private double rating;
    private boolean isAvailable;
    private String licenseNumber;
    
    public Guide() {
        this.languages = new ArrayList<>();
        this.isAvailable = true;
        this.rating = 0.0;
    }
    
    public Guide(String guideId, String name, List<String> languages, int experienceYears,
                 String contact, String email, String specialization, String licenseNumber) {
        this.guideId = guideId;
        this.name = name;
        this.languages = languages != null ? languages : new ArrayList<>();
        this.experienceYears = experienceYears;
        this.contact = contact;
        this.email = email;
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.isAvailable = true;
        this.rating = 0.0;
    }
    
    // Getters and Setters
    public String getGuideId() { return guideId; }
    public void setGuideId(String guideId) { this.guideId = guideId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public List<String> getLanguages() { return languages; }
    public void setLanguages(List<String> languages) { this.languages = languages; }
    
    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    
    @Override
    public String toString() {
        return name + " (" + specialization + ")";
    }
}

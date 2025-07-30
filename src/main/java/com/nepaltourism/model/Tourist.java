package com.nepaltourism.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Tourist implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String touristId;
    private String name;
    private String nationality;
    private String contact;
    private String emergencyContact;
    private String email;
    private LocalDate registrationDate;
    private String passportNumber;
    
    public Tourist() {
        this.registrationDate = LocalDate.now();
    }
    
    public Tourist(String touristId, String name, String nationality, String contact, 
                   String emergencyContact, String email, String passportNumber) {
        this.touristId = touristId;
        this.name = name;
        this.nationality = nationality;
        this.contact = contact;
        this.emergencyContact = emergencyContact;
        this.email = email;
        this.passportNumber = passportNumber;
        this.registrationDate = LocalDate.now();
    }
    
    // Getters and Setters
    public String getTouristId() { return touristId; }
    public void setTouristId(String touristId) { this.touristId = touristId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    
    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }
    
    @Override
    public String toString() {
        return name + " (" + nationality + ")";
    }
}

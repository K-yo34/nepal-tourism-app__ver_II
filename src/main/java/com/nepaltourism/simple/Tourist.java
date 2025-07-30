package com.nepaltourism.simple;

import java.io.Serializable;

public class Tourist implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String name;
    private String nationality;
    private String contact;
    private String emergencyContact;
    
    public Tourist(String id, String name, String nationality, String contact, String emergencyContact) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.contact = contact;
        this.emergencyContact = emergencyContact;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    
    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    
    @Override
    public String toString() {
        return name + " (" + nationality + ")";
    }
}

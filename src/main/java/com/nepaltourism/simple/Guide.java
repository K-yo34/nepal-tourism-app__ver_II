package com.nepaltourism.simple;

import java.io.Serializable;
import java.util.List;

public class Guide implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String name;
    private List<String> languages;
    private int experience;
    private String specialization;
    
    public Guide(String id, String name, List<String> languages, int experience, String specialization) {
        this.id = id;
        this.name = name;
        this.languages = languages;
        this.experience = experience;
        this.specialization = specialization;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public List<String> getLanguages() { return languages; }
    public void setLanguages(List<String> languages) { this.languages = languages; }
    
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    @Override
    public String toString() {
        return name + " (" + specialization + ")";
    }
}

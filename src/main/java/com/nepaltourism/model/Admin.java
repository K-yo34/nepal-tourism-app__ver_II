package com.nepaltourism.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String adminId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String role;
    private LocalDateTime lastLogin;
    private boolean isActive;
    
    public Admin() {
        this.isActive = true;
    }
    
    public Admin(String adminId, String username, String password, String fullName, 
                 String email, String role) {
        this.adminId = adminId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.isActive = true;
    }
    
    // Getters and Setters
    public String getAdminId() { return adminId; }
    public void setAdminId(String adminId) { this.adminId = adminId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    @Override
    public String toString() {
        return fullName + " (" + role + ")";
    }
}

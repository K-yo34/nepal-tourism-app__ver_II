package com.nepaltourism.controller;

import com.nepaltourism.model.Admin;
import com.nepaltourism.service.DataService;
import com.nepaltourism.util.ResourceBundleManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDateTime;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label titleLabel;
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private ComboBox<String> languageComboBox;
    @FXML private Label errorLabel;
    
    private DataService dataService;
    private ResourceBundleManager resourceManager;
    
    @FXML
    private void initialize() {
        dataService = DataService.getInstance();
        resourceManager = ResourceBundleManager.getInstance();
        
        setupLanguageComboBox();
        updateLabels();
        
        // Set default credentials hint
        usernameField.setPromptText("admin");
        passwordField.setPromptText("admin123");
    }
    
    private void setupLanguageComboBox() {
        languageComboBox.getItems().addAll("English", "नेपाली");
        languageComboBox.setValue("English");
        languageComboBox.setOnAction(e -> changeLanguage());
    }
    
    private void changeLanguage() {
        String selected = languageComboBox.getValue();
        if ("नेपाली".equals(selected)) {
            resourceManager.setLocale("ne", "NP");
        } else {
            resourceManager.setLocale("en", "US");
        }
        updateLabels();
    }
    
    private void updateLabels() {
        titleLabel.setText(resourceManager.getString("login.title"));
        usernameLabel.setText(resourceManager.getString("login.username"));
        passwordLabel.setText(resourceManager.getString("login.password"));
        loginButton.setText(resourceManager.getString("login.button"));
    }
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError(resourceManager.getString("login.error.empty"));
            return;
        }
        
        try {
            Admin admin = dataService.authenticateAdmin(username, password);
            if (admin != null) {
                admin.setLastLogin(LocalDateTime.now());
                dataService.updateAdmin(admin);
                
                openDashboard(admin);
            } else {
                showError(resourceManager.getString("login.error.invalid"));
            }
        } catch (Exception e) {
            showError("Login error: " + e.getMessage());
        }
    }
    
    private void openDashboard(Admin admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
            Parent root = loader.load();
            
            DashboardController controller = loader.getController();
            controller.setCurrentAdmin(admin);
            
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 800));
            stage.setTitle("Nepal Tourism Management System - Dashboard");
            stage.centerOnScreen();
        } catch (Exception e) {
            showError("Error opening dashboard: " + e.getMessage());
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}

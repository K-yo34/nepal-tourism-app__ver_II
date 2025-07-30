package com.nepaltourism.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleManager {
    private static ResourceBundleManager instance;
    private ResourceBundle resourceBundle;
    private Locale currentLocale;
    
    private ResourceBundleManager() {
        currentLocale = new Locale("en", "US");
        loadResourceBundle();
    }
    
    public static synchronized ResourceBundleManager getInstance() {
        if (instance == null) {
            instance = new ResourceBundleManager();
        }
        return instance;
    }
    
    public void setLocale(String language, String country) {
        currentLocale = new Locale(language, country);
        loadResourceBundle();
    }
    
    private void loadResourceBundle() {
        try {
            resourceBundle = ResourceBundle.getBundle("messages", currentLocale);
        } catch (Exception e) {
            // Fallback to default English bundle
            resourceBundle = ResourceBundle.getBundle("messages", new Locale("en", "US"));
        }
    }
    
    public String getString(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (Exception e) {
            return key; // Return key if translation not found
        }
    }
    
    public Locale getCurrentLocale() {
        return currentLocale;
    }
}

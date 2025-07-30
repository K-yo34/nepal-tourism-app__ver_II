package com.nepaltourism.simple;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.Optional;
import java.time.LocalDateTime;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public class SimpleTourismApp extends Application {
    
    // Simple data storage
    private List<Tourist> tourists = new ArrayList<>();
    private List<Guide> guides = new ArrayList<>();
    private List<Attraction> attractions = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();
    
    // UI Components
    private TabPane mainTabPane;
    private TableView<Tourist> touristTable;
    private TableView<Guide> guideTable;
    private TableView<Attraction> attractionTable;
    private TableView<Booking> bookingTable;

    // Add these fields at the top of the class
    private Stage primaryStage;
    private Scene loginScene;
    private Scene mainScene;
    private List<Admin> admins = new ArrayList<>();
    private Admin currentUser;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Nepal Tourism - Simple Version");

        // Create login scene first
        createLoginScene();

        // Initialize sample data
        initializeSampleData();

        // Show login screen
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    private void createLoginScene() {
        VBox loginRoot = new VBox(20);
        loginRoot.setAlignment(Pos.CENTER);
        loginRoot.setPadding(new Insets(50));
        loginRoot.setStyle("-fx-background-color: #f8f9fa;");

        // Header
        Label titleLabel = new Label("🏔️ Nepal Tourism Management");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #dc143c;");

        Label subtitleLabel = new Label("Simple Version");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #6c757d;");

        // Add time display to login screen
        HBox loginTimeBox = new HBox();
        loginTimeBox.setAlignment(Pos.CENTER);
        loginTimeBox.setPadding(new Insets(10));

        Label loginTimeLabel = new Label();
        loginTimeLabel.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 14px;");

        Label loginDateLabel = new Label();
        loginDateLabel.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 12px;");

        VBox loginTimeDisplay = new VBox(2);
        loginTimeDisplay.setAlignment(Pos.CENTER);
        loginTimeDisplay.getChildren().addAll(loginTimeLabel, loginDateLabel);

        loginTimeBox.getChildren().add(loginTimeDisplay);

        // Create timeline for login screen time
        Timeline loginTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTime(loginTimeLabel, loginDateLabel)));
        loginTimeline.setCycleCount(Animation.INDEFINITE);
        loginTimeline.play();

        // Initial time update for login
        updateTime(loginTimeLabel, loginDateLabel);

        // Login form
        VBox loginForm = new VBox(15);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setMaxWidth(300);
        loginForm.setStyle("-fx-background-color: white; -fx-padding: 30px; -fx-background-radius: 10px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label loginTitle = new Label("Login to Continue");
        loginTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #495057;");

        // Username field
        VBox usernameBox = new VBox(5);
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #495057;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setStyle("-fx-padding: 10px; -fx-font-size: 14px;");
        usernameBox.getChildren().addAll(usernameLabel, usernameField);

        // Password field
        VBox passwordBox = new VBox(5);
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #495057;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setStyle("-fx-padding: 10px; -fx-font-size: 14px;");
        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #dc143c; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-padding: 12px 30px; -fx-font-size: 14px; -fx-cursor: hand;");
        loginButton.setMaxWidth(Double.MAX_VALUE);

        // Error label
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #dc3545; -fx-font-size: 12px;");
        errorLabel.setVisible(false);

        // Default credentials info
        VBox credentialsInfo = new VBox(5);
        credentialsInfo.setAlignment(Pos.CENTER);
        credentialsInfo.setStyle("-fx-background-color: #e3f2fd; -fx-padding: 15px; -fx-background-radius: 5px;");

        Label infoTitle = new Label("Default Login Credentials:");
        infoTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #1976d2;");
        Label infoUsername = new Label("Username: admin");
        Label infoPassword = new Label("Password: admin123");
        infoUsername.setStyle("-fx-text-fill: #1976d2;");
        infoPassword.setStyle("-fx-text-fill: #1976d2;");

        credentialsInfo.getChildren().addAll(infoTitle, infoUsername, infoPassword);

        // Language selection
        HBox languageBox = new HBox(10);
        languageBox.setAlignment(Pos.CENTER);
        Label langLabel = new Label("Language:");
        langLabel.setStyle("-fx-text-fill: #6c757d;");
        ComboBox<String> languageCombo = new ComboBox<>();
        languageCombo.getItems().addAll("English", "नेपाली (Nepali)");
        languageCombo.setValue("English");
        languageCombo.setStyle("-fx-font-size: 12px;");
        languageBox.getChildren().addAll(langLabel, languageCombo);

        loginForm.getChildren().addAll(loginTitle, usernameBox, passwordBox, loginButton, errorLabel);

        // Login button action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                showLoginError(errorLabel, "Please enter both username and password");
                return;
            }

            // Simple authentication
            if (authenticateUser(username, password)) {
                // Create main scene if not already created
                if (mainScene == null) {
                    createMainScene();
                }

                // Switch to main application
                primaryStage.setScene(mainScene);
                primaryStage.setMaximized(true);

                // Load data after successful login
                loadData();

                // Show welcome message
                showTimeBasedWelcome();

            } else {
                showLoginError(errorLabel, "Invalid username or password");
            }
        });

        // Enter key support
        passwordField.setOnAction(e -> loginButton.fire());
        usernameField.setOnAction(e -> passwordField.requestFocus());

        loginRoot.getChildren().addAll(titleLabel, subtitleLabel, loginTimeBox, loginForm, credentialsInfo, languageBox);

        loginScene = new Scene(loginRoot, 500, 600);
    }

    private void createMainScene() {
        // Create main layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        
        // Header with logout and clock
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #dc143c;");
        
        Label headerTitle = new Label("🏔️ Nepal Tourism Management System");
        headerTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Time and date display
        VBox timeBox = new VBox(2);
        timeBox.setAlignment(Pos.CENTER_RIGHT);
        
        Label timeLabel = new Label();
        timeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        
        Label dateLabel = new Label();
        dateLabel.setStyle("-fx-text-fill: #ffcccc; -fx-font-size: 12px;");
        
        Label timezoneLabel = new Label("Nepal Time (NPT)");
        timezoneLabel.setStyle("-fx-text-fill: #ffcccc; -fx-font-size: 10px;");
        
        timeBox.getChildren().addAll(timeLabel, dateLabel, timezoneLabel);
        
        // User info
        String welcomeText = currentUser != null ? 
            "👤 " + currentUser.getFullName() + " (" + currentUser.getRole() + ")" : 
            "👤 User";
        Label userLabel = new Label(welcomeText);
        userLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 0 15 0 15;");
        
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white; -fx-font-weight: bold; " +
                             "-fx-padding: 8px 15px; -fx-cursor: hand;");
        logoutButton.setOnAction(e -> logout());
        
        header.getChildren().addAll(headerTitle, spacer, timeBox, userLabel, logoutButton);
        
        // Create timeline for updating time
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTime(timeLabel, dateLabel)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        
        // Initial time update
        updateTime(timeLabel, dateLabel);
        
        // Create tabs
        mainTabPane = new TabPane();
        mainTabPane.getTabs().addAll(
            createTouristTab(),
            createGuideTab(),
            createAttractionTab(),
            createBookingTab(),
            createReportsTab()
        );
        
        root.getChildren().addAll(header, mainTabPane);
        
        mainScene = new Scene(root, 1200, 800);
    }

    private boolean authenticateUser(String username, String password) {
        for (Admin admin : admins) {
            if (admin.getUsername().equals(username) && 
                admin.getPassword().equals(password) && 
                admin.isActive()) {
                
                currentUser = admin;
                admin.setLastLogin(LocalDateTime.now());
                saveData(); // Save the updated login time
                return true;
            }
        }
        return false;
    }

    private void showLoginError(Label errorLabel, String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);

        // Hide error after 3 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> errorLabel.setVisible(false)));
        timeline.play();
    }

    private void logout() {
        // Confirm logout
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Logout Confirmation");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Save data before logout
            saveData();

            // Switch back to login scene
            primaryStage.setScene(loginScene);
            primaryStage.setMaximized(false);
            primaryStage.centerOnScreen();

            // Clear any sensitive data if needed
            // Reset form fields, etc.
            currentUser = null;

            showAlert("Logged Out", "You have been successfully logged out.");
        }
    }
    
    private Tab createTouristTab() {
        Tab tab = new Tab("👥 Tourists");
        tab.setClosable(false);
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        // Add time display to tourist tab
        HBox tabHeader = new HBox();
        tabHeader.setAlignment(Pos.CENTER_LEFT);
        tabHeader.setPadding(new Insets(5));

        Label tabTitle = new Label("👥 Tourist Management");
        tabTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #dc143c;");

        Region tabSpacer = new Region();
        HBox.setHgrow(tabSpacer, Priority.ALWAYS);

        // Add logout button to tab
        Button tabLogoutBtn = new Button("Logout");
        tabLogoutBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5px 10px;");
        tabLogoutBtn.setOnAction(e -> logout());

        HBox tabTimeDisplay = createTabTimeDisplay();

        tabHeader.getChildren().addAll(tabTitle, tabSpacer, tabLogoutBtn, tabTimeDisplay);
        
        // Add tabHeader as the first child of content
        content.getChildren().add(0, tabHeader);
        
        // Add tourist form
        HBox addForm = new HBox(10);
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField nationalityField = new TextField();
        nationalityField.setPromptText("Nationality");
        TextField contactField = new TextField();
        contactField.setPromptText("Contact");
        TextField emergencyField = new TextField();
        emergencyField.setPromptText("Emergency Contact");
        
        Button addBtn = new Button("Add Tourist");
        addBtn.setStyle("-fx-background-color: #dc143c; -fx-text-fill: white;");
        addBtn.setOnAction(e -> {
            if (!nameField.getText().isEmpty() && !nationalityField.getText().isEmpty()) {
                Tourist tourist = new Tourist(
                    "T" + (tourists.size() + 1),
                    nameField.getText(),
                    nationalityField.getText(),
                    contactField.getText(),
                    emergencyField.getText()
                );
                tourists.add(tourist);
                refreshTouristTable();
                clearFields(nameField, nationalityField, contactField, emergencyField);
                saveData();
            }
        });
        
        addForm.getChildren().addAll(nameField, nationalityField, contactField, emergencyField, addBtn);
        
        // Tourist table
        touristTable = new TableView<>();
        setupTouristTable();
        
        // Action buttons
        HBox actionButtons = new HBox(10);
        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> {
            Tourist selected = touristTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                tourists.remove(selected);
                refreshTouristTable();
                saveData();
            }
        });
        
        Button exportBtn = new Button("Export CSV");
        exportBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        exportBtn.setOnAction(e -> exportTouristsCSV());
        
        actionButtons.getChildren().addAll(deleteBtn, exportBtn);
        
        content.getChildren().addAll(addForm, touristTable, actionButtons);
        tab.setContent(content);
        return tab;
    }
    
    private Tab createGuideTab() {
        Tab tab = new Tab("🗺️ Guides");
        tab.setClosable(false);
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        // Add time display to guide tab
        HBox tabHeader = new HBox();
        tabHeader.setAlignment(Pos.CENTER_LEFT);
        tabHeader.setPadding(new Insets(5));

        Label tabTitle = new Label("🗺️ Guide Management");
        tabTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #dc143c;");

        Region tabSpacer = new Region();
        HBox.setHgrow(tabSpacer, Priority.ALWAYS);

        // Add logout button to tab
        Button tabLogoutBtn = new Button("Logout");
        tabLogoutBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5px 10px;");
        tabLogoutBtn.setOnAction(e -> logout());

        HBox tabTimeDisplay = createTabTimeDisplay();

        tabHeader.getChildren().addAll(tabTitle, tabSpacer, tabLogoutBtn, tabTimeDisplay);
        
        // Add tabHeader as the first child of content
        content.getChildren().add(0, tabHeader);
        
        // Add guide form
        HBox addForm = new HBox(10);
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField languagesField = new TextField();
        languagesField.setPromptText("Languages (comma separated)");
        TextField experienceField = new TextField();
        experienceField.setPromptText("Experience (years)");
        TextField specializationField = new TextField();
        specializationField.setPromptText("Specialization");
        
        Button addBtn = new Button("Add Guide");
        addBtn.setStyle("-fx-background-color: #dc143c; -fx-text-fill: white;");
        addBtn.setOnAction(e -> {
            if (!nameField.getText().isEmpty()) {
                try {
                    Guide guide = new Guide(
                        "G" + (guides.size() + 1),
                        nameField.getText(),
                        Arrays.asList(languagesField.getText().split(",")),
                        Integer.parseInt(experienceField.getText().isEmpty() ? "0" : experienceField.getText()),
                        specializationField.getText()
                    );
                    guides.add(guide);
                    refreshGuideTable();
                    clearFields(nameField, languagesField, experienceField, specializationField);
                    saveData();
                } catch (NumberFormatException ex) {
                    showAlert("Error", "Please enter a valid number for experience years.");
                }
            }
        });
        
        addForm.getChildren().addAll(nameField, languagesField, experienceField, specializationField, addBtn);
        
        // Guide table
        guideTable = new TableView<>();
        setupGuideTable();
        
        // Action buttons
        HBox actionButtons = new HBox(10);
        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> {
            Guide selected = guideTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                guides.remove(selected);
                refreshGuideTable();
                saveData();
            }
        });
        
        Button exportBtn = new Button("Export CSV");
        exportBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        exportBtn.setOnAction(e -> exportGuidesCSV());
        
        actionButtons.getChildren().addAll(deleteBtn, exportBtn);
        
        content.getChildren().addAll(addForm, guideTable, actionButtons);
        tab.setContent(content);
        return tab;
    }
    
    private Tab createAttractionTab() {
        Tab tab = new Tab("🏔️ Attractions");
        tab.setClosable(false);
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        // Add time display to attraction tab
        HBox tabHeader = new HBox();
        tabHeader.setAlignment(Pos.CENTER_LEFT);
        tabHeader.setPadding(new Insets(5));

        Label tabTitle = new Label("🏔️ Attraction Management");
        tabTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #dc143c;");

        Region tabSpacer = new Region();
        HBox.setHgrow(tabSpacer, Priority.ALWAYS);

        // Add logout button to tab
        Button tabLogoutBtn = new Button("Logout");
        tabLogoutBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5px 10px;");
        tabLogoutBtn.setOnAction(e -> logout());

        HBox tabTimeDisplay = createTabTimeDisplay();

        tabHeader.getChildren().addAll(tabTitle, tabSpacer, tabLogoutBtn, tabTimeDisplay);
        
        // Add tabHeader as the first child of content
        content.getChildren().add(0, tabHeader);
        
        // Add attraction form
        HBox addForm = new HBox(10);
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        ComboBox<String> typeField = new ComboBox<>();
        typeField.getItems().addAll("Trek", "Heritage", "Adventure", "Cultural");
        typeField.setPromptText("Type");
        TextField locationField = new TextField();
        locationField.setPromptText("Location");
        TextField priceField = new TextField();
        priceField.setPromptText("Price (NPR)");
        TextField altitudeField = new TextField();
        altitudeField.setPromptText("Altitude (m)");
        
        Button addBtn = new Button("Add Attraction");
        addBtn.setStyle("-fx-background-color: #dc143c; -fx-text-fill: white;");
        addBtn.setOnAction(e -> {
            if (!nameField.getText().isEmpty() && typeField.getValue() != null) {
                try {
                    Attraction attraction = new Attraction(
                        "A" + (attractions.size() + 1),
                        nameField.getText(),
                        typeField.getValue(),
                        locationField.getText(),
                        Double.parseDouble(priceField.getText().isEmpty() ? "0" : priceField.getText()),
                        Integer.parseInt(altitudeField.getText().isEmpty() ? "0" : altitudeField.getText())
                    );
                    attractions.add(attraction);
                    refreshAttractionTable();
                    clearFields(nameField, locationField, priceField, altitudeField);
                    typeField.setValue(null);
                    saveData();
                } catch (NumberFormatException ex) {
                    showAlert("Error", "Please enter valid numbers for price and altitude.");
                }
            }
        });
        
        addForm.getChildren().addAll(nameField, typeField, locationField, priceField, altitudeField, addBtn);
        
        // Attraction table
        attractionTable = new TableView<>();
        setupAttractionTable();
        
        // Action buttons
        HBox actionButtons = new HBox(10);
        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> {
            Attraction selected = attractionTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                attractions.remove(selected);
                refreshAttractionTable();
                saveData();
            }
        });
        
        Button exportBtn = new Button("Export CSV");
        exportBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        exportBtn.setOnAction(e -> exportAttractionsCSV());
        
        actionButtons.getChildren().addAll(deleteBtn, exportBtn);
        
        content.getChildren().addAll(addForm, attractionTable, actionButtons);
        tab.setContent(content);
        return tab;
    }
    
    private Tab createBookingTab() {
        Tab tab = new Tab("📅 Bookings");
        tab.setClosable(false);
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        
        // Add time display to booking tab
        HBox tabHeader = new HBox();
        tabHeader.setAlignment(Pos.CENTER_LEFT);
        tabHeader.setPadding(new Insets(5));

        Label tabTitle = new Label("📅 Booking Management");
        tabTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #dc143c;");

        Region tabSpacer = new Region();
        HBox.setHgrow(tabSpacer, Priority.ALWAYS);

        // Add logout button to tab
        Button tabLogoutBtn = new Button("Logout");
        tabLogoutBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5px 10px;");
        tabLogoutBtn.setOnAction(e -> logout());

        HBox tabTimeDisplay = createTabTimeDisplay();

        tabHeader.getChildren().addAll(tabTitle, tabSpacer, tabLogoutBtn, tabTimeDisplay);
        
        // Add tabHeader as the first child of content
        content.getChildren().add(0, tabHeader);
        
        // Add booking form
        HBox addForm = new HBox(10);
        ComboBox<Tourist> touristField = new ComboBox<>();
        touristField.setPromptText("Select Tourist");
        ComboBox<Guide> guideField = new ComboBox<>();
        guideField.setPromptText("Select Guide");
        ComboBox<Attraction> attractionField = new ComboBox<>();
        attractionField.setPromptText("Select Attraction");
        DatePicker dateField = new DatePicker();
        dateField.setPromptText("Travel Date");
        
        Button addBtn = new Button("Create Booking");
        addBtn.setStyle("-fx-background-color: #dc143c; -fx-text-fill: white;");
        addBtn.setOnAction(e -> {
            if (touristField.getValue() != null && guideField.getValue() != null && 
                attractionField.getValue() != null && dateField.getValue() != null) {
        
        LocalDate travelDate = dateField.getValue();
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Kathmandu"));
        
        // Check if travel date is in the past
        if (travelDate.isBefore(today)) {
            showAlert("Invalid Date", "❌ Cannot book for past dates!\n\nCurrent Nepal Date: " + 
                     today.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
            return;
        }
        
        // Check if booking is too far in advance (more than 1 year)
        if (travelDate.isAfter(today.plusYears(1))) {
            showAlert("Booking Limit", "⚠️ Cannot book more than 1 year in advance!\n\nMaximum booking date: " + 
                     today.plusYears(1).format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
            return;
        }
        
        Attraction selectedAttraction = attractionField.getValue();
        double price = selectedAttraction.getPrice();
        
        // Apply festival discount (simple check for October/November)
        double discount = 0;
        if (travelDate.getMonthValue() == 10 || travelDate.getMonthValue() == 11) {
            discount = price * 0.15; // 15% festival discount
        }
        
        // Safety warning for high altitude
        if (selectedAttraction.getAltitude() > 3000) {
            showAlert("Safety Warning", 
                "⚠️ HIGH ALTITUDE WARNING: This trek goes above 3000m. " +
                "Risk of altitude sickness. Proper acclimatization required.\n\n" +
                "Current time: " + getNepalTimeString() + " NPT");
        }
        
        Booking booking = new Booking(
            "B" + (bookings.size() + 1),
            touristField.getValue(),
            guideField.getValue(),
            selectedAttraction,
            travelDate,
            price,
            discount
        );
        bookings.add(booking);
        refreshBookingTable();
        touristField.setValue(null);
        guideField.setValue(null);
        attractionField.setValue(null);
        dateField.setValue(null);
        saveData();
        
        if (discount > 0) {
            showAlert("Festival Discount Applied!", 
                String.format("🎉 Festival discount of NPR %.2f applied!\nFinal amount: NPR %.2f\n\nBooked at: %s NPT", 
                discount, price - discount, getNepalTimeString()));
        } else {
            showAlert("Booking Confirmed!", 
                String.format("✅ Booking created successfully!\nAmount: NPR %.2f\n\nBooked at: %s NPT", 
                price, getNepalTimeString()));
        }
    }
});
        
        addForm.getChildren().addAll(touristField, guideField, attractionField, dateField, addBtn);
        
        // Booking table
        bookingTable = new TableView<>();
        setupBookingTable();
        
        // Action buttons
        HBox actionButtons = new HBox(10);
        Button deleteBtn = new Button("Delete Selected");
        deleteBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
        deleteBtn.setOnAction(e -> {
            Booking selected = bookingTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                bookings.remove(selected);
                refreshBookingTable();
                saveData();
            }
        });
        
        Button exportBtn = new Button("Export CSV");
        exportBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        exportBtn.setOnAction(e -> exportBookingsCSV());
        
        Button emergencyBtn = new Button("Report Emergency");
        emergencyBtn.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;");
        emergencyBtn.setOnAction(e -> {
            Booking selected = bookingTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showAlert("Emergency Reported", 
                    "🚨 Emergency reported for booking: " + selected.getBookingId() + 
                    "\n\nEmergency Contacts:\n" +
                    "Tourist Police: 1144\n" +
                    "Nepal Police: 100\n" +
                    "Ambulance: 102");
            }
        });
        
        actionButtons.getChildren().addAll(deleteBtn, exportBtn, emergencyBtn);
        
        content.getChildren().addAll(addForm, bookingTable, actionButtons);
        tab.setContent(content);
        return tab;
    }
    
    private Tab createReportsTab() {
        Tab tab = new Tab("📊 Reports & Users");
        tab.setClosable(false);
        
        TabPane reportsTabPane = new TabPane();
        reportsTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Statistics Tab
        Tab statsTab = new Tab("📈 Statistics");
        VBox statsContent = new VBox(15);
        statsContent.setPadding(new Insets(10));

        // Add time display to reports tab
        HBox tabHeader = new HBox();
        tabHeader.setAlignment(Pos.CENTER_LEFT);
        tabHeader.setPadding(new Insets(5));

        Label tabTitle = new Label("📊 Reports & User Management");
        tabTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #dc143c;");

        Region tabSpacer = new Region();
        HBox.setHgrow(tabSpacer, Priority.ALWAYS);

        // Add logout button to tab
        Button tabLogoutBtn = new Button("Logout");
        tabLogoutBtn.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5px 10px;");
        tabLogoutBtn.setOnAction(e -> logout());

        HBox tabTimeDisplay = createTabTimeDisplay();

        tabHeader.getChildren().addAll(tabTitle, tabSpacer, tabLogoutBtn, tabTimeDisplay);

        // Add tabHeader as the first child of content
        statsContent.getChildren().add(0, tabHeader);

        // Time Widget at the top
        HBox timeWidget = new HBox();
        timeWidget.setAlignment(Pos.CENTER);
        timeWidget.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 15px; -fx-background-radius: 10px; " +
                           "-fx-border-color: #dc143c; -fx-border-width: 2px; -fx-border-radius: 10px;");

        VBox timeDisplay = new VBox(5);
        timeDisplay.setAlignment(Pos.CENTER);

        Label bigTimeLabel = new Label();
        bigTimeLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #dc143c;");

        Label bigDateLabel = new Label();
        bigDateLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #6c757d;");

        Label nepalTimeZoneLabel = new Label("🇳🇵 Nepal Standard Time (NPT) - UTC+05:45");
        nepalTimeZoneLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6c757d; -fx-font-style: italic;");

        timeDisplay.getChildren().addAll(bigTimeLabel, bigDateLabel, nepalTimeZoneLabel);
        timeWidget.getChildren().add(timeDisplay);

        // Create timeline for reports time widget
        Timeline reportsTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTime(bigTimeLabel, bigDateLabel)));
        reportsTimeline.setCycleCount(Animation.INDEFINITE);
        reportsTimeline.play();

        // Initial time update
        updateTime(bigTimeLabel, bigDateLabel);

        // Statistics
        Label statsTitle = new Label("📈 Quick Statistics");
        statsTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(10);
        
        Label totalTourists = new Label("Total Tourists: " + tourists.size());
        Label totalGuides = new Label("Total Guides: " + guides.size());
        Label totalAttractions = new Label("Total Attractions: " + attractions.size());
        Label totalBookings = new Label("Total Bookings: " + bookings.size());
        
        double totalRevenue = bookings.stream().mapToDouble(b -> b.getPrice() - b.getDiscount()).sum();
        Label revenue = new Label(String.format("Total Revenue: NPR %.2f", totalRevenue));
        
        statsGrid.add(totalTourists, 0, 0);
        statsGrid.add(totalGuides, 1, 0);
        statsGrid.add(totalAttractions, 0, 1);
        statsGrid.add(totalBookings, 1, 1);
        statsGrid.add(revenue, 0, 2, 2, 1);
        
        // Nationality breakdown
        Label nationalityTitle = new Label("🌍 Tourist Nationalities");
        nationalityTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        TextArea nationalityArea = new TextArea();
        nationalityArea.setPrefRowCount(5);
        nationalityArea.setEditable(false);
        
        Map<String, Long> nationalityCount = new HashMap<>();
        tourists.forEach(t -> nationalityCount.merge(t.getNationality(), 1L, Long::sum));
        
        StringBuilder nationalityText = new StringBuilder();
        nationalityCount.forEach((nationality, count) -> 
            nationalityText.append(nationality).append(": ").append(count).append(" tourists\n"));
        nationalityArea.setText(nationalityText.toString());
        
        // Export buttons
        HBox exportButtons = new HBox(10);
        Button exportAllBtn = new Button("Export All Data");
        exportAllBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        exportAllBtn.setOnAction(e -> exportAllData());
        
        Button refreshBtn = new Button("Refresh Stats");
        refreshBtn.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white;");
        refreshBtn.setOnAction(e -> {
            // Refresh the reports tab
            mainTabPane.getTabs().set(4, createReportsTab());
            mainTabPane.getSelectionModel().select(4);
        });
        
        exportButtons.getChildren().addAll(exportAllBtn, refreshBtn);
        
        statsContent.getChildren().addAll(timeWidget, statsTitle, statsGrid, 
            new Separator(), nationalityTitle, nationalityArea, exportButtons);
        statsTab.setContent(statsContent);
        
        // Data Export Tab - Fixed Layout
        Tab dataExportTab = new Tab("📤 Data Export");
        ScrollPane exportScrollPane = new ScrollPane();
        exportScrollPane.setFitToWidth(true);
        exportScrollPane.setPrefViewportHeight(600);

        VBox exportContent = new VBox(20);
        exportContent.setPadding(new Insets(20));
        exportContent.setPrefWidth(900);

        Label exportTitle = new Label("📤 Export Data to CSV");
        exportTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #dc143c;");

        // Create a proper grid with fixed spacing
        VBox exportCardsContainer = new VBox(15);
        exportCardsContainer.setPrefWidth(850);

        // Row 1
        HBox row1 = new HBox(15);
        row1.setPrefWidth(850);
        VBox touristCard = createFixedExportCard("👥 Tourist Data", 
            "Export all tourist records with contact details", 
            "Export Tourists CSV", "#007bff", e -> exportTouristsCSV());
        VBox guideCard = createFixedExportCard("🗺️ Guide Data", 
            "Export all guide profiles and specializations", 
            "Export Guides CSV", "#007bff", e -> exportGuidesCSV());
        row1.getChildren().addAll(touristCard, guideCard);

        // Row 2
        HBox row2 = new HBox(15);
        row2.setPrefWidth(850);
        VBox attractionCard = createFixedExportCard("🏔️ Attraction Data", 
            "Export attraction catalog with pricing", 
            "Export Attractions CSV", "#007bff", e -> exportAttractionsCSV());
        VBox bookingCard = createFixedExportCard("📅 Booking Data", 
            "Export complete booking records", 
            "Export Bookings CSV", "#007bff", e -> exportBookingsCSV());
        row2.getChildren().addAll(attractionCard, bookingCard);

        // Row 3
        HBox row3 = new HBox(15);
        row3.setPrefWidth(850);
        VBox revenueCard = createFixedExportCard("💰 Revenue Report", 
            "Monthly revenue analysis and trends", 
            "Export Revenue CSV", "#28a745", e -> exportRevenueReport());
        VBox festivalCard = createFixedExportCard("🎉 Festival Impact", 
            "Festival discount impact analysis", 
            "Export Festival CSV", "#ffc107", e -> exportFestivalReport());
        row3.getChildren().addAll(revenueCard, festivalCard);

        exportCardsContainer.getChildren().addAll(row1, row2, row3);

        // Export Information
        VBox infoBox = new VBox(10);
        infoBox.setStyle("-fx-background-color: #e3f2fd; -fx-padding: 15px; -fx-background-radius: 10px;");
        infoBox.setPrefWidth(850);

        Label infoTitle = new Label("ℹ️ CSV Export Information");
        infoTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1976d2;");

        VBox infoList = new VBox(5);
        infoList.getChildren().addAll(
            new Label("• All CSV files are UTF-8 encoded and compatible with Excel, Google Sheets"),
            new Label("• Data includes proper escaping for special characters and commas"),
            new Label("• Reports include calculated fields like totals, percentages, and trends"),
            new Label("• Files can be used for external analysis, backup, or integration")
        );

        infoList.getChildren().forEach(label -> 
            ((Label)label).setStyle("-fx-text-fill: #1976d2; -fx-font-size: 12px;"));

        infoBox.getChildren().addAll(infoTitle, infoList);

        exportContent.getChildren().addAll(exportTitle, exportCardsContainer, infoBox);
        exportScrollPane.setContent(exportContent);
        dataExportTab.setContent(exportScrollPane);
        
        // User Management Tab
        Tab usersTab = new Tab("👥 Users");
        VBox usersContent = new VBox(15);
        usersContent.setPadding(new Insets(10));
        
        Label usersTitle = new Label("👥 System Users");
        usersTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Users table
        TableView<Admin> usersTable = new TableView<>();
        
        TableColumn<Admin, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUsername()));
        
        TableColumn<Admin, String> fullNameCol = new TableColumn<>("Full Name");
        fullNameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFullName()));
        
        TableColumn<Admin, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRole()));
        
        TableColumn<Admin, String> lastLoginCol = new TableColumn<>("Last Login");
        lastLoginCol.setCellValueFactory(data -> {
            LocalDateTime lastLogin = data.getValue().getLastLogin();
            String loginText = lastLogin != null ? lastLogin.toString() : "Never";
            return new javafx.beans.property.SimpleStringProperty(loginText);
        });
        
        TableColumn<Admin, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> {
            String status = data.getValue().isActive() ? "Active" : "Inactive";
            return new javafx.beans.property.SimpleStringProperty(status);
        });
        
        usersTable.getColumns().addAll(usernameCol, fullNameCol, roleCol, lastLoginCol, statusCol);
        usersTable.getItems().addAll(admins);
        
        // Current user info
        VBox currentUserBox = new VBox(10);
        currentUserBox.setStyle("-fx-background-color: #e3f2fd; -fx-padding: 15px; -fx-background-radius: 5px;");
        
        Label currentUserTitle = new Label("Current Session:");
        currentUserTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #1976d2;");
        
        String currentUserInfo = currentUser != null ? 
            "Logged in as: " + currentUser.getFullName() + " (" + currentUser.getRole() + ")\n" +
            "Username: " + currentUser.getUsername() + "\n" +
            "Login Time: " + (currentUser.getLastLogin() != null ? currentUser.getLastLogin().toString() : "Unknown") :
        "No user logged in";
        
        Label currentUserLabel = new Label(currentUserInfo);
        currentUserLabel.setStyle("-fx-text-fill: #1976d2;");
        
        currentUserBox.getChildren().addAll(currentUserTitle, currentUserLabel);
        
        usersContent.getChildren().addAll(usersTitle, usersTable, currentUserBox);
        usersTab.setContent(usersContent);
        
        reportsTabPane.getTabs().addAll(statsTab, dataExportTab, usersTab);
        tab.setContent(reportsTabPane);
        return tab;
    }
    
    // Helper method to create export cards with fixed layout
    private VBox createFixedExportCard(String title, String description, String buttonText, String buttonColor, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(410);
        card.setPrefHeight(140);
        card.setMaxWidth(410);
        card.setMaxHeight(140);
        card.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-background-radius: 10px; " +
                     "-fx-border-color: #dee2e6; -fx-border-width: 1px; -fx-border-radius: 10px; " +
                     "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #495057;");
        titleLabel.setMaxWidth(380);
        titleLabel.setWrapText(false);
        
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #6c757d; -fx-text-alignment: center;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(380);
        descLabel.setPrefHeight(30);
        
        Button exportBtn = new Button(buttonText);
        exportBtn.setStyle("-fx-background-color: " + buttonColor + "; -fx-text-fill: white; " +
                          "-fx-font-weight: bold; -fx-padding: 8px 16px; -fx-font-size: 11px; " +
                          "-fx-cursor: hand;");
        exportBtn.setPrefWidth(200);
        exportBtn.setOnAction(action);
        
        card.getChildren().addAll(titleLabel, descLabel, exportBtn);
        return card;
    }
    
    // Table setup methods
    private void setupTouristTable() {
        TableColumn<Tourist, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        
        TableColumn<Tourist, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        
        TableColumn<Tourist, String> nationalityCol = new TableColumn<>("Nationality");
        nationalityCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNationality()));
        
        TableColumn<Tourist, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getContact()));
        
        TableColumn<Tourist, String> emergencyCol = new TableColumn<>("Emergency Contact");
        emergencyCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmergencyContact()));
        
        touristTable.getColumns().addAll(idCol, nameCol, nationalityCol, contactCol, emergencyCol);
        refreshTouristTable();
    }
    
    private void setupGuideTable() {
        TableColumn<Guide, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        
        TableColumn<Guide, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        
        TableColumn<Guide, String> languagesCol = new TableColumn<>("Languages");
        languagesCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.join(", ", data.getValue().getLanguages())));
        
        TableColumn<Guide, String> experienceCol = new TableColumn<>("Experience");
        experienceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getExperience() + " years"));
        
        TableColumn<Guide, String> specializationCol = new TableColumn<>("Specialization");
        specializationCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSpecialization()));
        
        guideTable.getColumns().addAll(idCol, nameCol, languagesCol, experienceCol, specializationCol);
        refreshGuideTable();
    }
    
    private void setupAttractionTable() {
        TableColumn<Attraction, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        
        TableColumn<Attraction, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        
        TableColumn<Attraction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));
        
        TableColumn<Attraction, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLocation()));
        
        TableColumn<Attraction, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            "NPR " + String.format("%.2f", data.getValue().getPrice())));
        
        TableColumn<Attraction, String> altitudeCol = new TableColumn<>("Altitude");
        altitudeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getAltitude() + "m"));
        
        attractionTable.getColumns().addAll(idCol, nameCol, typeCol, locationCol, priceCol, altitudeCol);
        refreshAttractionTable();
    }
    
    private void setupBookingTable() {
        TableColumn<Booking, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getBookingId()));
        
        TableColumn<Booking, String> touristCol = new TableColumn<>("Tourist");
        touristCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTourist().getName()));
        
        TableColumn<Booking, String> guideCol = new TableColumn<>("Guide");
        guideCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getGuide().getName()));
        
        TableColumn<Booking, String> attractionCol = new TableColumn<>("Attraction");
        attractionCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAttraction().getName()));
        
        TableColumn<Booking, String> dateCol = new TableColumn<>("Travel Date");
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTravelDate().toString()));
        
        TableColumn<Booking, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.format("NPR %.2f", data.getValue().getPrice())));
        
        TableColumn<Booking, String> discountCol = new TableColumn<>("Discount");
        discountCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.format("NPR %.2f", data.getValue().getDiscount())));
        
        TableColumn<Booking, String> finalCol = new TableColumn<>("Final Amount");
        finalCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.format("NPR %.2f", data.getValue().getPrice() - data.getValue().getDiscount())));
        
        bookingTable.getColumns().addAll(idCol, touristCol, guideCol, attractionCol, dateCol, priceCol, discountCol, finalCol);
        refreshBookingTable();
    }
    
    // Fixed refresh methods
    private void refreshTouristTable() {
        touristTable.getItems().clear();
        touristTable.getItems().addAll(tourists);
        
        // Update combo boxes in booking tab if it exists
        if (mainTabPane != null && mainTabPane.getTabs().size() > 3) {
            Tab bookingTab = mainTabPane.getTabs().get(3);
            VBox content = (VBox) bookingTab.getContent();
            if (content.getChildren().size() > 1) {
                HBox addForm = (HBox) content.getChildren().get(1); // Skip the header
                @SuppressWarnings("unchecked")
                ComboBox<Tourist> touristCombo = (ComboBox<Tourist>) addForm.getChildren().get(0);
                touristCombo.getItems().clear();
                touristCombo.getItems().addAll(tourists);
            }
        }
    }
    
    private void refreshGuideTable() {
        guideTable.getItems().clear();
        guideTable.getItems().addAll(guides);
        
        // Update combo boxes in booking tab if it exists
        if (mainTabPane != null && mainTabPane.getTabs().size() > 3) {
            Tab bookingTab = mainTabPane.getTabs().get(3);
            VBox content = (VBox) bookingTab.getContent();
            if (content.getChildren().size() > 1) {
                HBox addForm = (HBox) content.getChildren().get(1); // Skip the header
                @SuppressWarnings("unchecked")
                ComboBox<Guide> guideCombo = (ComboBox<Guide>) addForm.getChildren().get(1);
                guideCombo.getItems().clear();
                guideCombo.getItems().addAll(guides);
            }
        }
    }
    
    private void refreshAttractionTable() {
        attractionTable.getItems().clear();
        attractionTable.getItems().addAll(attractions);
        
        // Update combo boxes in booking tab if it exists
        if (mainTabPane != null && mainTabPane.getTabs().size() > 3) {
            Tab bookingTab = mainTabPane.getTabs().get(3);
            VBox content = (VBox) bookingTab.getContent();
            if (content.getChildren().size() > 1) {
                HBox addForm = (HBox) content.getChildren().get(1); // Skip the header
                @SuppressWarnings("unchecked")
                ComboBox<Attraction> attractionCombo = (ComboBox<Attraction>) addForm.getChildren().get(2);
                attractionCombo.getItems().clear();
                attractionCombo.getItems().addAll(attractions);
            }
        }
    }
    
    private void refreshBookingTable() {
        bookingTable.getItems().clear();
        bookingTable.getItems().addAll(bookings);
    }
    
    // CSV Export methods
    private void exportTouristsCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Tourists");
        fileChooser.setInitialFileName("tourists.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showSaveDialog(mainTabPane.getScene().getWindow());
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("ID,Name,Nationality,Contact,Emergency Contact");
                for (Tourist tourist : tourists) {
                    writer.printf("%s,%s,%s,%s,%s%n",
                            tourist.getId(), tourist.getName(), tourist.getNationality(),
                            tourist.getContact(), tourist.getEmergencyContact());
                }
                showAlert("Success", "Tourists exported to: " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Error", "Failed to export: " + e.getMessage());
            }
        }
    }


    private void exportGuidesCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Guides");
        fileChooser.setInitialFileName("guides.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        
        File file = fileChooser.showSaveDialog(mainTabPane.getScene().getWindow());
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("ID,Name,Languages,Experience,Specialization");
                for (Guide guide : guides) {
                    writer.printf("%s,%s,\"%s\",%d,%s%n",
                        guide.getId(), guide.getName(), String.join(";", guide.getLanguages()),
                        guide.getExperience(), guide.getSpecialization());
                }
                showAlert("Success", "Guides exported to: " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Error", "Failed to export: " + e.getMessage());
            }
        }
    }
    
    private void exportAttractionsCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Attractions");
        fileChooser.setInitialFileName("attractions.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        
        File file = fileChooser.showSaveDialog(mainTabPane.getScene().getWindow());
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("ID,Name,Type,Location,Price,Altitude");
                for (Attraction attraction : attractions) {
                    writer.printf("%s,%s,%s,%s,%.2f,%d%n",
                        attraction.getId(), attraction.getName(), attraction.getType(),
                        attraction.getLocation(), attraction.getPrice(), attraction.getAltitude());
                }
                showAlert("Success", "Attractions exported to: " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Error", "Failed to export: " + e.getMessage());
            }
        }
    }
    
    private void exportBookingsCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Bookings");
        fileChooser.setInitialFileName("bookings.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        
        File file = fileChooser.showSaveDialog(mainTabPane.getScene().getWindow());
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("Booking ID,Tourist,Guide,Attraction,Travel Date,Price,Discount,Final Amount");
                for (Booking booking : bookings) {
                    writer.printf("%s,%s,%s,%s,%s,%.2f,%.2f,%.2f%n",
                        booking.getBookingId(), booking.getTourist().getName(),
                        booking.getGuide().getName(), booking.getAttraction().getName(),
                        booking.getTravelDate(), booking.getPrice(), booking.getDiscount(),
                        booking.getPrice() - booking.getDiscount());
                }
                showAlert("Success", "Bookings exported to: " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Error", "Failed to export: " + e.getMessage());
            }
        }
    }
    
    private void exportRevenueReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Revenue Report");
        fileChooser.setInitialFileName("revenue_report.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        
        File file = fileChooser.showSaveDialog(mainTabPane.getScene().getWindow());
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("REVENUE REPORT - NEPAL TOURISM MANAGEMENT");
                writer.println("Generated on: " + LocalDate.now());
                writer.println();
                
                writer.println("Month,Total Bookings,Total Revenue,Average Booking Value");
                
                Map<String, List<Booking>> monthlyBookings = new HashMap<>();
                for (Booking booking : bookings) {
                    String month = booking.getTravelDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                    monthlyBookings.computeIfAbsent(month, k -> new ArrayList<>()).add(booking);
                }
                
                for (Map.Entry<String, List<Booking>> entry : monthlyBookings.entrySet()) {
                    String month = entry.getKey();
                    List<Booking> monthBookings = entry.getValue();
                    double totalRevenue = monthBookings.stream()
                        .mapToDouble(b -> b.getPrice() - b.getDiscount()).sum();
                    double avgBookingValue = totalRevenue / monthBookings.size();
                    
                    writer.printf("%s,%d,%.2f,%.2f%n", 
                        month, monthBookings.size(), totalRevenue, avgBookingValue);
                }
                
                showAlert("Success", "Revenue report exported to: " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Error", "Failed to export: " + e.getMessage());
            }
        }
    }
    
    private void exportFestivalReport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Festival Impact Report");
        fileChooser.setInitialFileName("festival_impact.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        
        File file = fileChooser.showSaveDialog(mainTabPane.getScene().getWindow());
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("FESTIVAL IMPACT REPORT - NEPAL TOURISM MANAGEMENT");
                writer.println("Generated on: " + LocalDate.now());
                writer.println();
                
                writer.println("Booking ID,Tourist,Attraction,Travel Date,Original Price,Discount Applied,Final Amount,Festival Period");
                
                for (Booking booking : bookings) {
                    String festivalPeriod = "No";
                    if (booking.getTravelDate().getMonthValue() == 10 || booking.getTravelDate().getMonthValue() == 11) {
                        festivalPeriod = "Yes (Oct-Nov Festival Season)";
                    }
                    
                    writer.printf("%s,%s,%s,%s,%.2f,%.2f,%.2f,%s%n",
                        booking.getBookingId(),
                        booking.getTourist().getName(),
                        booking.getAttraction().getName(),
                        booking.getTravelDate(),
                        booking.getPrice(),
                        booking.getDiscount(),
                        booking.getPrice() - booking.getDiscount(),
                        festivalPeriod);
                }
                
                writer.println();
                writer.println("FESTIVAL IMPACT SUMMARY");
                
                double totalDiscounts = bookings.stream().mapToDouble(Booking::getDiscount).sum();
                long festivalBookings = bookings.stream()
                    .filter(b -> b.getTravelDate().getMonthValue() == 10 || b.getTravelDate().getMonthValue() == 11)
                    .count();
                
                writer.println("Total Festival Discounts Given,NPR " + String.format("%.2f", totalDiscounts));
                writer.println("Total Festival Season Bookings," + festivalBookings);
                writer.println("Percentage of Festival Bookings," + 
                    String.format("%.1f%%", (festivalBookings * 100.0) / bookings.size()));
                
                showAlert("Success", "Festival impact report exported to: " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Error", "Failed to export: " + e.getMessage());
            }
        }
    }
    
    private void exportAllData() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export All Data");
        fileChooser.setInitialFileName("nepal_tourism_complete_data.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        
        File file = fileChooser.showSaveDialog(mainTabPane.getScene().getWindow());
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                // Summary report
                writer.println("NEPAL TOURISM MANAGEMENT SYSTEM - COMPLETE REPORT");
                writer.println("Generated on: " + LocalDate.now());
                writer.println("Generated at: " + getNepalTimeString() + " NPT");
                writer.println();
                
                writer.println("SUMMARY STATISTICS");
                writer.println("Total Tourists," + tourists.size());
                writer.println("Total Guides," + guides.size());
                writer.println("Total Attractions," + attractions.size());
                writer.println("Total Bookings," + bookings.size());
                double totalRevenue = bookings.stream().mapToDouble(b -> b.getPrice() - b.getDiscount()).sum();
                writer.println("Total Revenue,NPR " + String.format("%.2f", totalRevenue));
                writer.println();
                
                // Nationality breakdown
                writer.println("TOURIST NATIONALITIES");
                Map<String, Long> nationalityCount = new HashMap<>();
                tourists.forEach(t -> nationalityCount.merge(t.getNationality(), 1L, Long::sum));
                writer.println("Nationality,Count,Percentage");
                nationalityCount.forEach((nationality, count) -> {
                    double percentage = (count * 100.0) / tourists.size();
                    writer.printf("%s,%d,%.1f%%%n", nationality, count, percentage);
                });
                writer.println();


                // Popular attractions
                writer.println("POPULAR ATTRACTIONS");
                Map<String, Long> attractionCount = new HashMap<>();
                bookings.forEach(b -> attractionCount.merge(b.getAttraction().getName(), 1L, Long::sum));
                writer.println("Attraction,Bookings,Revenue");
                attractionCount.entrySet().stream()
                    .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                    .forEach(entry -> {
                        double revenue = bookings.stream()
                            .filter(b -> b.getAttraction().getName().equals(entry.getKey()))
                            .mapToDouble(b -> b.getPrice() - b.getDiscount())
                            .sum();
                        writer.printf("%s,%d,NPR %.2f%n", entry.getKey(), entry.getValue(), revenue);
                    });
                
                showAlert("Success", "Complete data report exported to: " + file.getAbsolutePath());
            } catch (IOException e) {
                showAlert("Error", "Failed to export: " + e.getMessage());
            }
        }
    }
    
    // Data persistence
    private void saveData() {
        try {
            // Create data directory if it doesn't exist
            File dataDir = new File("simple_data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            
            // Save admins
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("simple_data/admins.dat"))) {
                oos.writeObject(admins);
            }
            
            // Save tourists
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("simple_data/tourists.dat"))) {
                oos.writeObject(tourists);
            }
            
            // Save guides
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("simple_data/guides.dat"))) {
                oos.writeObject(guides);
            }
            
            // Save attractions
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("simple_data/attractions.dat"))) {
                oos.writeObject(attractions);
            }
            
            // Save bookings
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("simple_data/bookings.dat"))) {
                oos.writeObject(bookings);
            }
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadData() {
        try {
            // Load admins
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("simple_data/admins.dat"))) {
                admins = (List<Admin>) ois.readObject();
            } catch (FileNotFoundException e) {
                // File doesn't exist yet, use default admins from initializeSampleData
            }
            
            // Load tourists
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("simple_data/tourists.dat"))) {
                tourists = (List<Tourist>) ois.readObject();
                refreshTouristTable();
            } catch (FileNotFoundException e) {
                // File doesn't exist yet, use sample data
            }
            
            // Load guides
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("simple_data/guides.dat"))) {
                guides = (List<Guide>) ois.readObject();
                refreshGuideTable();
            } catch (FileNotFoundException e) {
                // File doesn't exist yet, use sample data
            }
            
            // Load attractions
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("simple_data/attractions.dat"))) {
                attractions = (List<Attraction>) ois.readObject();
                refreshAttractionTable();
            } catch (FileNotFoundException e) {
                // File doesn't exist yet, use sample data
            }
            
            // Load bookings
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("simple_data/bookings.dat"))) {
                bookings = (List<Booking>) ois.readObject();
                refreshBookingTable();
            } catch (FileNotFoundException e) {
                // File doesn't exist yet, use sample data
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
    
    private void initializeSampleData() {
        // Initialize admin users
        if (admins.isEmpty()) {
            admins.add(new Admin("admin", "admin123", "System Administrator", "Administrator"));
            admins.add(new Admin("manager", "manager123", "Tourism Manager", "Manager"));
            admins.add(new Admin("staff", "staff123", "Front Desk Staff", "Staff"));
        }
        
        // Sample tourists
        tourists.add(new Tourist("T001", "John Smith", "USA", "+1-555-0123", "+1-555-0124"));
        tourists.add(new Tourist("T002", "Emma Wilson", "UK", "+44-20-7946-0958", "+44-20-7946-0959"));
        tourists.add(new Tourist("T003", "Hiroshi Tanaka", "Japan", "+81-3-1234-5678", "+81-3-1234-5679"));
        
        // Sample guides
        guides.add(new Guide("G001", "Ram Bahadur", Arrays.asList("English", "Nepali", "Hindi"), 8, "Trekking"));
        guides.add(new Guide("G002", "Pemba Sherpa", Arrays.asList("English", "Nepali", "Tibetan"), 12, "High Altitude"));
        guides.add(new Guide("G003", "Sita Gurung", Arrays.asList("English", "Nepali", "German"), 5, "Cultural Tours"));
        
        // Sample attractions
        attractions.add(new Attraction("A001", "Everest Base Camp Trek", "Trek", "Khumbu Region", 1500.0, 5364));
        attractions.add(new Attraction("A002", "Annapurna Base Camp Trek", "Trek", "Annapurna Region", 800.0, 4130));
        attractions.add(new Attraction("A003", "Kathmandu Durbar Square", "Heritage", "Kathmandu", 50.0, 1400));
        attractions.add(new Attraction("A004", "Chitwan National Park", "Adventure", "Chitwan", 200.0, 150));
    }
    
    // Utility methods
    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateTime(Label timeLabel, Label dateLabel) {
        // Get Nepal time (UTC+5:45)
        ZoneId nepalZone = ZoneId.of("Asia/Kathmandu");
        LocalDateTime nepalTime = LocalDateTime.now(nepalZone);

        // Format time (24-hour format)
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = nepalTime.format(timeFormatter);

        // Format date
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
        String formattedDate = nepalTime.format(dateFormatter);

        // Update labels
        timeLabel.setText("🕐 " + formattedTime);
        dateLabel.setText(formattedDate);
    }

    private String getNepalTimeString() {
        ZoneId nepalZone = ZoneId.of("Asia/Kathmandu");
        LocalDateTime nepalTime = LocalDateTime.now(nepalZone);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return nepalTime.format(timeFormatter);
    }

    private String getNepalDateString() {
        ZoneId nepalZone = ZoneId.of("Asia/Kathmandu");
        LocalDateTime nepalTime = LocalDateTime.now(nepalZone);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
        return nepalTime.format(dateFormatter);
    }

    private String getGreeting() {
        ZoneId nepalZone = ZoneId.of("Asia/Kathmandu");
        LocalDateTime nepalTime = LocalDateTime.now(nepalZone);
        int hour = nepalTime.getHour();
        
        if (hour >= 5 && hour < 12) {
            return "🌅 Good Morning";
        } else if (hour >= 12 && hour < 17) {
            return "☀️ Good Afternoon";
        } else if (hour >= 17 && hour < 21) {
            return "🌆 Good Evening";
        } else {
            return "🌙 Good Night";
        }
    }

    private void showTimeBasedWelcome() {
        String greeting = getGreeting();
        String userName = currentUser != null ? currentUser.getFullName() : "User";
        String message = greeting + ", " + userName + "!\n\n" +
                        "Current Nepal Time: " + getNepalTimeString() + "\n" +
                        "Date: " + getNepalDateString() + "\n\n" +
                        "Welcome to Nepal Tourism Management System!";
        
        showAlert("Welcome", message);
    }

    private HBox createTabTimeDisplay() {
        HBox timeBox = new HBox(10);
        timeBox.setAlignment(Pos.CENTER_RIGHT);
        timeBox.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 5px 10px; -fx-background-radius: 5px;");
        
        Label tabTimeLabel = new Label();
        tabTimeLabel.setStyle("-fx-text-fill: #dc143c; -fx-font-size: 12px; -fx-font-weight: bold;");
        
        Label tabDateLabel = new Label();
        tabDateLabel.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 10px;");
        
        VBox tabTimeDisplay = new VBox(1);
        tabTimeDisplay.setAlignment(Pos.CENTER_RIGHT);
        tabTimeDisplay.getChildren().addAll(tabTimeLabel, tabDateLabel);
        
        timeBox.getChildren().add(tabTimeDisplay);
        
        // Create timeline for tab time
        Timeline tabTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTime(tabTimeLabel, tabDateLabel)));
        tabTimeline.setCycleCount(Animation.INDEFINITE);
        tabTimeline.play();
        
        // Initial time update
        updateTime(tabTimeLabel, tabDateLabel);
        
        return timeBox;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

package com.nepaltourism.service;

import com.nepaltourism.model.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataService {
    private static DataService instance;
    
    // Data storage using Collections
    private Map<String, Tourist> tourists;
    private Map<String, Guide> guides;
    private Map<String, Attraction> attractions;
    private Map<String, Booking> bookings;
    private Map<String, Festival> festivals;
    private Map<String, Admin> admins;
    
    // File paths
    private static final String DATA_DIR = "data/";
    private static final String TOURISTS_FILE = DATA_DIR + "tourists.dat";
    private static final String GUIDES_FILE = DATA_DIR + "guides.dat";
    private static final String ATTRACTIONS_FILE = DATA_DIR + "attractions.dat";
    private static final String BOOKINGS_FILE = DATA_DIR + "bookings.dat";
    private static final String FESTIVALS_FILE = DATA_DIR + "festivals.dat";
    private static final String ADMINS_FILE = DATA_DIR + "admins.dat";
    
    private DataService() {
        tourists = new ConcurrentHashMap<>();
        guides = new ConcurrentHashMap<>();
        attractions = new ConcurrentHashMap<>();
        bookings = new ConcurrentHashMap<>();
        festivals = new ConcurrentHashMap<>();
        admins = new ConcurrentHashMap<>();
        
        createDataDirectory();
        loadAllData();
        initializeDefaultData();
    }
    
    public static synchronized DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }
    
    private void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    // Tourist operations
    public void addTourist(Tourist tourist) {
        tourists.put(tourist.getTouristId(), tourist);
        saveTourists();
    }
    
    public Tourist getTourist(String id) {
        return tourists.get(id);
    }
    
    public List<Tourist> getAllTourists() {
        return new ArrayList<>(tourists.values());
    }
    
    public void updateTourist(Tourist tourist) {
        tourists.put(tourist.getTouristId(), tourist);
        saveTourists();
    }
    
    public void deleteTourist(String id) {
        tourists.remove(id);
        saveTourists();
    }
    
    // Guide operations
    public void addGuide(Guide guide) {
        guides.put(guide.getGuideId(), guide);
        saveGuides();
    }
    
    public Guide getGuide(String id) {
        return guides.get(id);
    }
    
    public List<Guide> getAllGuides() {
        return new ArrayList<>(guides.values());
    }
    
    public void updateGuide(Guide guide) {
        guides.put(guide.getGuideId(), guide);
        saveGuides();
    }
    
    public void deleteGuide(String id) {
        guides.remove(id);
        saveGuides();
    }
    
    // Attraction operations
    public void addAttraction(Attraction attraction) {
        attractions.put(attraction.getAttractionId(), attraction);
        saveAttractions();
    }
    
    public Attraction getAttraction(String id) {
        return attractions.get(id);
    }
    
    public List<Attraction> getAllAttractions() {
        return new ArrayList<>(attractions.values());
    }
    
    public void updateAttraction(Attraction attraction) {
        attractions.put(attraction.getAttractionId(), attraction);
        saveAttractions();
    }
    
    public void deleteAttraction(String id) {
        attractions.remove(id);
        saveAttractions();
    }
    
    // Booking operations
    public void addBooking(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
        saveBookings();
    }
    
    public Booking getBooking(String id) {
        return bookings.get(id);
    }
    
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings.values());
    }
    
    public void updateBooking(Booking booking) {
        bookings.put(booking.getBookingId(), booking);
        saveBookings();
    }
    
    public void deleteBooking(String id) {
        bookings.remove(id);
        saveBookings();
    }
    
    // Festival operations
    public void addFestival(Festival festival) {
        festivals.put(festival.getFestivalId(), festival);
        saveFestivals();
    }
    
    public Festival getFestival(String id) {
        return festivals.get(id);
    }
    
    public List<Festival> getAllFestivals() {
        return new ArrayList<>(festivals.values());
    }
    
    public void updateFestival(Festival festival) {
        festivals.put(festival.getFestivalId(), festival);
        saveFestivals();
    }
    
    public void deleteFestival(String id) {
        festivals.remove(id);
        saveFestivals();
    }
    
    // Admin operations
    public void addAdmin(Admin admin) {
        admins.put(admin.getAdminId(), admin);
        saveAdmins();
    }
    
    public Admin getAdmin(String id) {
        return admins.get(id);
    }
    
    public List<Admin> getAllAdmins() {
        return new ArrayList<>(admins.values());
    }
    
    public Admin authenticateAdmin(String username, String password) {
        return admins.values().stream()
                .filter(admin -> admin.getUsername().equals(username) && 
                               admin.getPassword().equals(password) && 
                               admin.isActive())
                .findFirst()
                .orElse(null);
    }
    
    // File I/O operations
    private void loadAllData() {
        loadTourists();
        loadGuides();
        loadAttractions();
        loadBookings();
        loadFestivals();
        loadAdmins();
    }
    
    @SuppressWarnings("unchecked")
    private void loadTourists() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TOURISTS_FILE))) {
            tourists = (Map<String, Tourist>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, start with empty map
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading tourists: " + e.getMessage());
        }
    }
    
    private void saveTourists() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(TOURISTS_FILE))) {
            oos.writeObject(tourists);
        } catch (IOException e) {
            System.err.println("Error saving tourists: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadGuides() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GUIDES_FILE))) {
            guides = (Map<String, Guide>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, start with empty map
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading guides: " + e.getMessage());
        }
    }
    
    private void saveGuides() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GUIDES_FILE))) {
            oos.writeObject(guides);
        } catch (IOException e) {
            System.err.println("Error saving guides: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadAttractions() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ATTRACTIONS_FILE))) {
            attractions = (Map<String, Attraction>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, start with empty map
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading attractions: " + e.getMessage());
        }
    }
    
    private void saveAttractions() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ATTRACTIONS_FILE))) {
            oos.writeObject(attractions);
        } catch (IOException e) {
            System.err.println("Error saving attractions: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadBookings() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKINGS_FILE))) {
            bookings = (Map<String, Booking>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, start with empty map
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading bookings: " + e.getMessage());
        }
    }
    
    private void saveBookings() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKINGS_FILE))) {
            oos.writeObject(bookings);
        } catch (IOException e) {
            System.err.println("Error saving bookings: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadFestivals() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FESTIVALS_FILE))) {
            festivals = (Map<String, Festival>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, start with empty map
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading festivals: " + e.getMessage());
        }
    }
    
    private void saveFestivals() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FESTIVALS_FILE))) {
            oos.writeObject(festivals);
        } catch (IOException e) {
            System.err.println("Error saving festivals: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadAdmins() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ADMINS_FILE))) {
            admins = (Map<String, Admin>) ois.readObject();
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, start with empty map
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading admins: " + e.getMessage());
        }
    }
    
    private void saveAdmins() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ADMINS_FILE))) {
            oos.writeObject(admins);
        } catch (IOException e) {
            System.err.println("Error saving admins: " + e.getMessage());
        }
    }
    
    private void initializeDefaultData() {
        // Add default admin if no admins exist
        if (admins.isEmpty()) {
            Admin defaultAdmin = new Admin("ADM001", "admin", "admin123", 
                                         "System Administrator", "admin@nepaltourism.com", "Administrator");
            addAdmin(defaultAdmin);
        }
        
        // Add sample festivals if none exist
        if (festivals.isEmpty()) {
            initializeFestivals();
        }
        
        // Add sample attractions if none exist
        if (attractions.isEmpty()) {
            initializeAttractions();
        }
    }
    
    private void initializeFestivals() {
        Festival dashain = new Festival("FES001", "Dashain", 
                                      java.time.LocalDate.of(2024, 10, 15),
                                      java.time.LocalDate.of(2024, 10, 25),
                                      15.0, "Major Hindu festival in Nepal");
        
        Festival tihar = new Festival("FES002", "Tihar", 
                                    java.time.LocalDate.of(2024, 11, 1),
                                    java.time.LocalDate.of(2024, 11, 5),
                                    10.0, "Festival of lights in Nepal");
        
        addFestival(dashain);
        addFestival(tihar);
    }
    
    private void initializeAttractions() {
        Attraction ebc = new Attraction("ATT001", "Everest Base Camp Trek", 
                                      Attraction.AttractionType.TREK, "Khumbu Region",
                                      Attraction.DifficultyLevel.DIFFICULT, 
                                      "World's most famous trekking route", 
                                      1500.0, 5364, 14);
        
        Attraction abc = new Attraction("ATT002", "Annapurna Base Camp Trek", 
                                      Attraction.AttractionType.TREK, "Annapurna Region",
                                      Attraction.DifficultyLevel.MODERATE, 
                                      "Beautiful mountain trek with diverse landscapes", 
                                      800.0, 4130, 12);
        
        Attraction kathmandu = new Attraction("ATT003", "Kathmandu Durbar Square", 
                                            Attraction.AttractionType.HERITAGE, "Kathmandu",
                                            Attraction.DifficultyLevel.EASY, 
                                            "Historic palace complex", 
                                            50.0, 1400, 1);
        
        addAttraction(ebc);
        addAttraction(abc);
        addAttraction(kathmandu);
    }

    public void updateAdmin(Admin admin) {
        // Put the updated admin in the map (replaces existing entry if key exists)
        admins.put(admin.getAdminId(), admin);

        // Save the updated admins map to file
        saveAdmins();
    }

}

package com.nepaltourism.service;

import com.nepaltourism.model.Tourist;
import com.nepaltourism.model.Guide;
import com.nepaltourism.model.Attraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Path;
import java.util.Arrays;

public class DataServiceTest {
    
    private DataService dataService;
    
    @TempDir
    Path tempDir;
    
    @BeforeEach
    void setUp() {
        // Initialize with fresh data service for each test
        dataService = DataService.getInstance();
    }
    
    @Test
    void testAddAndRetrieveTourist() {
        // Arrange
        Tourist tourist = new Tourist("T001", "John Doe", "USA", "+1234567890", 
                                    "+1987654321", "john@email.com", "P123456789");
        
        // Act
        dataService.addTourist(tourist);
        Tourist retrieved = dataService.getTourist("T001");
        
        // Assert
        assertNotNull(retrieved);
        assertEquals("John Doe", retrieved.getName());
        assertEquals("USA", retrieved.getNationality());
        assertEquals("+1234567890", retrieved.getContact());
    }
    
    @Test
    void testAddAndRetrieveGuide() {
        // Arrange
        Guide guide = new Guide("G001", "Ram Bahadur", Arrays.asList("English", "Nepali"), 
                              5, "+9771234567", "ram@email.com", "Trekking", "L001");
        
        // Act
        dataService.addGuide(guide);
        Guide retrieved = dataService.getGuide("G001");
        
        // Assert
        assertNotNull(retrieved);
        assertEquals("Ram Bahadur", retrieved.getName());
        assertEquals(5, retrieved.getExperienceYears());
        assertTrue(retrieved.getLanguages().contains("English"));
        assertTrue(retrieved.getLanguages().contains("Nepali"));
    }
    
    @Test
    void testAddAndRetrieveAttraction() {
        // Arrange
        Attraction attraction = new Attraction("A001", "Everest Base Camp", 
                                             Attraction.AttractionType.TREK, "Khumbu",
                                             Attraction.DifficultyLevel.DIFFICULT, 
                                             "Famous trek", 1500.0, 5364, 14);
        
        // Act
        dataService.addAttraction(attraction);
        Attraction retrieved = dataService.getAttraction("A001");
        
        // Assert
        assertNotNull(retrieved);
        assertEquals("Everest Base Camp", retrieved.getName());
        assertEquals(Attraction.AttractionType.TREK, retrieved.getType());
        assertEquals(5364, retrieved.getAltitude());
        assertTrue(retrieved.isHighAltitude());
    }
    
    @Test
    void testUpdateTourist() {
        // Arrange
        Tourist tourist = new Tourist("T002", "Jane Smith", "UK", "+441234567890", 
                                    "+449876543210", "jane@email.com", "P987654321");
        dataService.addTourist(tourist);
        
        // Act
        tourist.setNationality("Canada");
        dataService.updateTourist(tourist);
        Tourist updated = dataService.getTourist("T002");
        
        // Assert
        assertEquals("Canada", updated.getNationality());
    }
    
    @Test
    void testDeleteTourist() {
        // Arrange
        Tourist tourist = new Tourist("T003", "Bob Johnson", "Australia", "+611234567890", 
                                    "+619876543210", "bob@email.com", "P456789123");
        dataService.addTourist(tourist);
        
        // Act
        dataService.deleteTourist("T003");
        Tourist deleted = dataService.getTourist("T003");
        
        // Assert
        assertNull(deleted);
    }
    
    @Test
    void testGetAllTourists() {
        // Arrange
        Tourist tourist1 = new Tourist("T004", "Alice Brown", "Germany", "+491234567890", 
                                     "+499876543210", "alice@email.com", "P789123456");
        Tourist tourist2 = new Tourist("T005", "Charlie Wilson", "France", "+331234567890", 
                                     "+339876543210", "charlie@email.com", "P321654987");
        
        // Act
        dataService.addTourist(tourist1);
        dataService.addTourist(tourist2);
        
        // Assert
        assertTrue(dataService.getAllTourists().size() >= 2);
        assertTrue(dataService.getAllTourists().stream()
                  .anyMatch(t -> t.getName().equals("Alice Brown")));
        assertTrue(dataService.getAllTourists().stream()
                  .anyMatch(t -> t.getName().equals("Charlie Wilson")));
    }
}

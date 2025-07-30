package com.nepaltourism.util;

import com.nepaltourism.model.Attraction;
import com.nepaltourism.model.Booking;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class SafetyManager {
    private static final int HIGH_ALTITUDE_THRESHOLD = 3000; // meters
    private static final List<Month> MONSOON_MONTHS = List.of(Month.JUNE, Month.JULY, Month.AUGUST);
    
    public static List<String> getSafetyWarnings(Attraction attraction, LocalDate travelDate) {
        List<String> warnings = new ArrayList<>();
        
        // High altitude warning
        if (attraction.getAltitude() > HIGH_ALTITUDE_THRESHOLD) {
            warnings.add("⚠️ HIGH ALTITUDE WARNING: This trek goes above " + HIGH_ALTITUDE_THRESHOLD + 
                        "m. Risk of altitude sickness. Proper acclimatization required.");
        }
        
        // Monsoon season warning
        if (MONSOON_MONTHS.contains(travelDate.getMonth()) && 
            attraction.getType() == Attraction.AttractionType.TREK) {
            warnings.add("🌧️ MONSOON SEASON WARNING: Trekking during monsoon season (June-August) " +
                        "involves risks of landslides, leeches, and poor visibility.");
        }
        
        // Difficulty level warnings
        switch (attraction.getDifficulty()) {
            case DIFFICULT:
                warnings.add("🏔️ DIFFICULTY WARNING: This is a difficult trek requiring good physical fitness " +
                           "and previous trekking experience.");
                break;
            case EXTREME:
                warnings.add("⛔ EXTREME DIFFICULTY WARNING: This trek is extremely challenging and requires " +
                           "extensive mountaineering experience and proper equipment.");
                break;
        }
        
        return warnings;
    }
    
    public static boolean isSafeForBooking(Attraction attraction, LocalDate travelDate) {
        // Block extreme difficulty treks during monsoon
        if (attraction.getDifficulty() == Attraction.DifficultyLevel.EXTREME && 
            MONSOON_MONTHS.contains(travelDate.getMonth())) {
            return false;
        }
        
        return true;
    }
    
    public static String getEmergencyContactInfo() {
        return "🚨 EMERGENCY CONTACTS:\n" +
               "Tourist Police: 1144\n" +
               "Nepal Police: 100\n" +
               "Ambulance: 102\n" +
               "Fire Brigade: 101\n" +
               "Tourist Helpline: +977-1-4247041";
    }
}

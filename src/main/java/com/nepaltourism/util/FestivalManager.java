package com.nepaltourism.util;

import com.nepaltourism.model.Festival;
import com.nepaltourism.service.DataService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class FestivalManager {
    private static DataService dataService = DataService.getInstance();
    
    public static List<Festival> getActiveFestivals() {
        return dataService.getAllFestivals().stream()
                .filter(Festival::isCurrentlyActive)
                .collect(Collectors.toList());
    }
    
    public static double calculateFestivalDiscount(double originalAmount) {
        List<Festival> activeFestivals = getActiveFestivals();
        
        if (activeFestivals.isEmpty()) {
            return 0.0;
        }
        
        // Apply the highest discount available
        double maxDiscount = activeFestivals.stream()
                .mapToDouble(Festival::getDiscountPercentage)
                .max()
                .orElse(0.0);
        
        return originalAmount * (maxDiscount / 100.0);
    }
    
    public static Festival getBestActiveFestival() {
        return getActiveFestivals().stream()
                .max((f1, f2) -> Double.compare(f1.getDiscountPercentage(), f2.getDiscountPercentage()))
                .orElse(null);
    }
    
    public static boolean isFestivalPeriod() {
        return !getActiveFestivals().isEmpty();
    }
}

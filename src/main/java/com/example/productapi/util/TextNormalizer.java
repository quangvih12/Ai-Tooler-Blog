package com.example.productapi.util;

import java.text.Normalizer;

public class TextNormalizer {
    
    public static String normalize(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }
        
        // Convert to lowercase and normalize Unicode
        String normalized = Normalizer.normalize(input.toLowerCase().trim(), Normalizer.Form.NFD);
        
        // Remove diacritics (accents)
        normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        
        // Handle special Vietnamese characters that might not be caught by the above
        normalized = normalized
            .replace("đ", "d")
            .replace("Đ", "d");
            
        return normalized;
    }
}
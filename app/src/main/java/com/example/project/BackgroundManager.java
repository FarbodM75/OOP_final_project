package com.example.project;

import java.util.Random;

/**
 * List of background images
 */
public class BackgroundManager {
    // Randomly selects and returns a background image for each mission
    private static final String[] backgrounds = {
            "bg_space",
            "bg_space2",
            "bg_desert",
            "bg_desert2",
            "bg_castle",
            "bg_sahara"
    };
// Returns a random background image
    public static String getRandomBackground() {
        Random rand = new Random();
        return backgrounds[rand.nextInt(backgrounds.length)];
    }
}
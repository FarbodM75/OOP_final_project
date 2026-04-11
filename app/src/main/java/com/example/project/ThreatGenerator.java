package com.example.project;

import java.util.ArrayList;
import java.util.Random;
/**
Creates a threat based on the selected crew members stats
 */
public class ThreatGenerator {
    public static Threat generate(ArrayList<CrewMember> selectedCrew) {
        if (selectedCrew.size() != 3) return null; // sanity check

        CrewMember c1 = selectedCrew.get(0);
        CrewMember c2 = selectedCrew.get(1);
        CrewMember c3 = selectedCrew.get(2);

        Random rand = new Random();

        // Random threat poster (threat type)
        String[] posterOptions = {"enemy_1", "enemy_2", "enemy_3", "enemy_4"};
        String poster = posterOptions[rand.nextInt(posterOptions.length)];

        // Algorithm to generate threat stats based on crew stats for game difficulty
        int totalSkill = c1.getSkill() + c2.getSkill() + c3.getSkill();
        int totalResilience = c1.getResilience() + c2.getResilience() + c3.getResilience();
        int totalEnergy = c1.getEnergy() + c2.getEnergy() + c3.getEnergy();

        int threatSkill = Math.max(1, (int)(totalSkill * 0.5) + rand.nextInt(5));
        int threatResilience = Math.max(1, (int)(totalResilience * 0.15) + rand.nextInt(5));
        int threatEnergy = Math.max(10, (int)(totalEnergy * 1.1) + rand.nextInt(10));

        return new Threat("Generated Threat", threatSkill, threatResilience, threatEnergy, poster);
    }
}
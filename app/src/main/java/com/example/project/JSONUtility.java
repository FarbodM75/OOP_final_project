package com.example.project;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Stores or loads crew members from a JSON file.
 */
public class JSONUtility {

    private static final String FILENAME = "crews.json";
    /**
     * Loads crew members from a JSON file.
     */
    public static ArrayList<CrewMember> loadCrew(Context context) {
        Storage.clearCrew(); // clear any existing crew

        try {
            // Internal storage file
            File crewFile = new File(context.getFilesDir(), FILENAME);

            // If the file doesn't exist yet, copy it from assets
            if (!crewFile.exists()) {
                try (FileOutputStream fos = new FileOutputStream(crewFile)) {
                    fos.write("[]".getBytes(StandardCharsets.UTF_8));
                }

            }
            // Read JSON from the internal storage file
            InputStream is = new FileInputStream(crewFile);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            JSONArray array = new JSONArray(json);
            // Parse JSON and create CrewMember objects
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);

                String crwnamee = obj.optString("crwnamee", "Unknown");
                String crwtypee = obj.optString("crwtypee", "Unknown");
                int skill = obj.optInt("skill", 5);         // use saved value if present
                int resilience = obj.optInt("resilience", 5);
                int energy = obj.optInt("energy", 50);
                int maxEnergy = obj.optInt("maxEnergy", 50);
                int experience = obj.optInt("experience", 0);
                // Create full CrewMember for game logic
                CrewMember crewMember = null;
                switch (crwtypee) {
                    case "Pilot":
                        crewMember = new Pilot(crwnamee, skill, resilience, energy, maxEnergy, experience, "pilot_img");
                        break;
                    case "Engineer":
                        crewMember = new Engineer(crwnamee, skill, resilience, energy, maxEnergy, experience, "engineer_img");
                        break;
                    case "Medic":
                        crewMember = new Medic(crwnamee, skill, resilience, energy, maxEnergy, experience, "medic_img");
                        break;
                    case "Scientist":
                        crewMember = new Scientist(crwnamee, skill, resilience, energy, maxEnergy, experience, "scientist_img");
                        break;
                    case "Soldier":
                        crewMember = new Soldier(crwnamee, skill, resilience, energy, maxEnergy, experience, "soldier_img");
                }


                String location = obj.optString("location", "quarters"); // default to quarters
                // Set the location of the crew member
                if (crewMember != null) {
                    switch (location) {
                        case "quarters":
                            Storage.addCrewMember(crewMember);
                            break;
                        case "training":
                            Storage.addToTraining(crewMember);
                            break;
                        case "mission":
                            Storage.addToMission(crewMember);
                            break;
                    }
                }
            }

        } catch (Exception e) {
            Log.e("JSON_ERROR", "Error loading crew: " + e.getMessage());
        }
        // Return the loaded crew
        return Storage.getAllCrewMembers();
    }

    /**
     * Saves crew members to a JSON file.
     */
    public static void saveCrew(Context context) {
        try {
            JSONArray array = new JSONArray();

            // Save Quarters crew
            for (CrewMember crew : Storage.getAllCrewMembers()) {
                JSONObject obj = crewToJson(crew, "quarters");
                array.put(obj);
            }

            // Save Training crew
            for (CrewMember crew : Storage.getTrainingCrew()) {
                JSONObject obj = crewToJson(crew, "training");
                array.put(obj);
            }

            // Save Mission crew
            for (CrewMember crew : Storage.getMissionCrew()) {
                JSONObject obj = crewToJson(crew, "mission");
                array.put(obj);
            }
            // Convert JSONArray to String
            String jsonString = array.toString();
            // Write to internal storage
            File crewFile = new File(context.getFilesDir(), FILENAME);
            // If the file doesn't exist yet, create it
            try (FileOutputStream fos = new FileOutputStream(crewFile)) {
                fos.write(jsonString.getBytes(StandardCharsets.UTF_8));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     Helper function to convert CrewMember to JSON
     */
    private static JSONObject crewToJson(CrewMember crew, String location) throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("crwnamee", crew.getName());
        obj.put("crwtypee", crew.getType());
        obj.put("skill", crew.getSkill());
        obj.put("resilience", crew.getResilience());
        obj.put("energy", crew.getEnergy());
        obj.put("maxEnergy", crew.getMaxEnergy());
        obj.put("experience", crew.getExperience());
        obj.put("poster", crew.getPosterName());
        obj.put("location", location); // NEW: tracks if crew is in quarters, training, or mission
        return obj;
    }
}
package com.example.project;

import android.content.Context;

import java.util.ArrayList;

/*
Universal storage for all types of methods related to storing and retrieving data from the list
 */
public class Storage {
    public static ArrayList<CrewMember> crewList = new ArrayList<>();
    public static ArrayList<CrewMember> trainingCrew = new ArrayList<>();
    public static ArrayList<CrewMember> missionCrew = new ArrayList<>();

    /**
     Adds a crew member to the crewList
     */
    public static void addCrewMember(CrewMember crewMember) {
        crewList.add(crewMember);
    }
    /**
     Returns the list of all crew members within crewList
     */
    public static ArrayList<CrewMember> getAllCrewMembers() {
        return crewList;
    }
    /**
     Clears the crew list
     */
    public static void clearCrew() {

        Storage.crewList.clear();
    }

    // For carrying the threat to next room (setter and getter)
    private static Threat currentThreat;
    public static void setCurrentThreat(Threat threat) {
        currentThreat = threat;
    }
    public static Threat getCurrentThreat() {
        return currentThreat;
    }


    // Methods for mission room (adder, remover, getter)
    public static ArrayList<CrewMember> getMissionCrew() {
        return missionCrew;
    }
    public static boolean addToMission(CrewMember crew) {
        if (missionCrew.size() >= 3) return false; // limit reached

        if (!missionCrew.contains(crew)) {
            missionCrew.add(crew);
            return true;
        }
        return false;
    }
    public static void removeFromMission(CrewMember crew) {
        missionCrew.remove(crew);
    }
    /**
     Moves a crew member to the quarters list and removes from mission list
     */
    public static void moveToQuarters2(CrewMember crew) {
        removeFromMission(crew);
        if (!crewList.contains(crew)) crewList.add(crew);  // add back to main quarters list if not already there
    }


    // Methods for training room (adder, remover, getter)
    public static ArrayList<CrewMember> getTrainingCrew() {
        return trainingCrew;
    }
    public static void addToTraining(CrewMember crew) {
        if (!trainingCrew.contains(crew)) trainingCrew.add(crew);
    }
    public static void removeFromTraining(CrewMember crew) {
        trainingCrew.remove(crew);
    }
    /**
    Moves a crew member to the quarters list and removes from training list
     */
    public static void moveToQuarters(CrewMember crew) {
        removeFromTraining(crew);
        if (!crewList.contains(crew)) crewList.add(crew);  // add back to main quarters list if not already there
    }


    /**
    Removes a crew member by name
     */
    public static boolean removeCrewMemberByName(Context context, String name) {
        for (int i = 0; i < crewList.size(); i++) {
            if (crewList.get(i).getName().equalsIgnoreCase(name)) {
                crewList.remove(i);
                JSONUtility.saveCrew(context);
                return true;
            }
        }
        return false;
    }


    //Background Manager for Mission (getter and setter)
    private static String currentBackground;
    public static void setBackground(String bg) {
        currentBackground = bg;
    }
    public static String getBackground() {
        return currentBackground;
    }

    // a place to store statistics screen information (such as total wins, losses, missions, streak)
    private static Statistics statistics = new Statistics();
    /**
    Returns the statistics object
     */
    public static Statistics getStatistics() {
        return statistics;
    }

}



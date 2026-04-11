package com.example.project;

public class Statistics {
    private int totalMissions;
    private int totalWins;
    private int totalLosses;
    private int winStreak;

    /*
    Records the number of missions won
     */
    public void recordWin() {
        totalMissions++;
        totalWins++;
        winStreak++;        // increase streak
    }
    /*
   Records the number of missions lost
    */
    public void recordLoss() {
        totalMissions++;
        totalLosses++;
        winStreak = 0;      // reset streak on loss
    }

    // Getters
    public int getTotalWins() {
        return totalWins;
    }
    public int getTotalLosses() {
        return totalLosses;
    }
    public int getWinStreak() {
        return winStreak;
    }
    public int getTotalMissions() {
        return totalMissions;
    }
}

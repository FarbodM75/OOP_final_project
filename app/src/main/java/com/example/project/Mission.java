package com.example.project;

import android.content.Context;
import android.os.Handler;
import java.util.ArrayList;

public class Mission {
    private Threat threat;
    private ArrayList<CrewMember> crew;
    private int currentTurnIndex = 0;
    private boolean isPlayerTurn = true;
    private Context context; // for saving data and experience
    private MissionCallback callback; // for communicating back to the Activity

    // Interface to communicate back to the Activity (5 methods)
    public interface MissionCallback {
        void onUpdateUI();
        void onSetTurnText(String text);
        void onSetButtonsEnabled(boolean enabled);
        void onMissionWonDialog();
        void onMissionLostDialog();
    }
    // Constructor
    public Mission(Context context, Threat threat, ArrayList<CrewMember> crew, MissionCallback callback) {
        this.context = context;
        this.threat = threat;
        this.crew = crew;
        this.callback = callback;
    }
    /**
     Keeps track of turn order
     */
    private void nextTurn() {
        currentTurnIndex++;
        if (currentTurnIndex >= crew.size()) {
            currentTurnIndex = 0;
        }
    }
    /**
     Removes dead crew members if they are NOT Alive
     */
    private void removeDeadCrew() {
        for (int i = crew.size() - 1; i >= 0; i--) {
            if (!crew.get(i).isAlive()) {
                Storage.getAllCrewMembers().remove(crew.get(i));
                crew.remove(i);
            }
        }
    }
    /**
     Adds experience and moves crew members to Quarters
     */
    private void missionWon() {
        ArrayList<CrewMember> missionCrew = Storage.getMissionCrew();
        for (CrewMember c : missionCrew) {
            c.restoreEnergy(c.getMaxEnergy() - c.getEnergy());
            c.addExperience(100, context);
            // Move to quarters ONLY if not already there
            if (!Storage.getAllCrewMembers().contains(c)) {
                Storage.getAllCrewMembers().add(c);
            }
        }
        // Clear mission AFTER moving
        missionCrew.clear();
        JSONUtility.saveCrew(context);
        Storage.getStatistics().recordWin();

        // shows the win dialog
        callback.onMissionWonDialog();
    }
    /**
     Removes mission crew members permanently
     */
    private void missionLost() {
        Storage.getMissionCrew().clear();
        JSONUtility.saveCrew(context);
        Storage.getStatistics().recordLoss();

        // shows the loss dialog
        callback.onMissionLostDialog();
    }
    /**
     Performs a full turn and updates the UI accordingly
     */
    public void performTurn(String action) {
        removeDeadCrew();
        // fix indexes after crew dies
        if (currentTurnIndex >= crew.size()) {
            currentTurnIndex = 0;
        }
        // if no crew or threat, return
        if (crew.isEmpty() || threat == null || !isPlayerTurn) return;

        // Determines which crew member's turn it is
        CrewMember currentCrew = crew.get(currentTurnIndex);
        // Crew's turn
        callback.onSetTurnText("Your Turn: " + currentCrew.getName());
        switch (action) {
            case "attack":
                currentCrew.act(threat);
                break;
            case "defend":
                currentCrew.restoreEnergy(currentCrew.getMaxEnergy() / 5);
                break;
            case "special":
                currentCrew.act(threat);
                currentCrew.act(threat);
                break;
        }

        // Update stats immediately after crew member acts
        callback.onUpdateUI();

        // Check if threat defeated
        if (threat.getEnergy() <= 0) {
            missionWon();
            return;
        }

        // Disable buttons
        callback.onSetButtonsEnabled(false);

        // Switch to enemy turn
        isPlayerTurn = false;
        callback.onSetTurnText("Enemy's Turn");

        // Delay enemy action
        new Handler().postDelayed(() -> {
            // Threat's attack (attacks randomly)
            CrewMember target = crew.get((int)(Math.random() * crew.size()));
            threat.attack(target);

            // Update stats after enemy attack
            callback.onUpdateUI();

            // Remove dead crew
            removeDeadCrew();
            if (currentTurnIndex >= crew.size()) {
                currentTurnIndex = 0;
            }

            // Check if all crew is dead
            if (crew.isEmpty()) {
                missionLost();
                return;
            }

            // Prepare next crew member
            nextTurn();

            // Switch back to player
            isPlayerTurn = true;
            callback.onSetTurnText("Your Turn: " + crew.get(currentTurnIndex).getName());

            // Re-enable buttons
            callback.onSetButtonsEnabled(true);

        }, 2000);
    }
}

package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MissionControlActivity extends AppCompatActivity {
    private CrewAdapter adapter;
    private ArrayList<CrewMember> selectedCrew = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_control_main);

        // RecyclerView setup
        RecyclerView recyclerMission = findViewById(R.id.recyclerMission);
        // Adapter creation
        adapter = new CrewAdapter(Storage.getMissionCrew());

        recyclerMission.setLayoutManager(new GridLayoutManager(this, 2)); // grid layout, 2 columns
        //set adapter
        recyclerMission.setAdapter(adapter);
        // Checkbox selection listener in adapter
        adapter.setSelectionListener(new CrewAdapter.SelectionListener() {
            // Called when the selection changes and updates the selectedCrew list
            @Override
            public void onSelectionChanged(ArrayList<CrewMember> newSelection) {
                selectedCrew = newSelection;
            }

        });

        // Start mission button
        Button startButton = findViewById(R.id.buttonMissionStart);

        startButton.setOnClickListener(v -> {
            // Check the number of crew actually selected
            if (selectedCrew.size() != 3) {
                Toast.makeText(this, "Select 3 crew members for the mission!", Toast.LENGTH_SHORT).show();
                return; // Stop here if not exactly 3
            }
            // Clear old mission crew
            Storage.getMissionCrew().clear();

            // Add selected crew
            for (CrewMember crew : selectedCrew) {
                Storage.addToMission(crew);
            }
            // Generate threat
            Storage.setCurrentThreat(ThreatGenerator.generate(selectedCrew));

            Intent intent = new Intent(this, MissionActivity.class);
            startActivity(intent);

        });

        // Move to Quarters button
        Button moveButton = findViewById(R.id.moveBack);
        moveButton.setOnClickListener(v -> {
            // move to quarters and remove from MissionControl
            if (!selectedCrew.isEmpty()) {
                for (CrewMember crew : selectedCrew) {
                    crew.restoreEnergy(crew.getMaxEnergy() - crew.getEnergy());
                    Storage.moveToQuarters2(crew);
                }
            }

            // Clear selection after moving
            selectedCrew.clear();
            // Refresh adapter with the updated Quarters list
            adapter.setCrewList(new ArrayList<>(Storage.getMissionCrew()));
            adapter.notifyDataSetChanged();
            // Save updated crew
            JSONUtility.saveCrew(this);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh RecyclerView after mission ends
        if (adapter != null) {
            adapter.setCrewList(new ArrayList<>(Storage.getMissionCrew()));
            adapter.notifyDataSetChanged();
        }
    }
    // Back to quarters button
    public void moveBack(View view) {
        finish();
    }
}

package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuarterActivity extends AppCompatActivity {
    private RecyclerView rycyclercrew;
    private CrewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quarters_main);
        // RecyclerView setup
        rycyclercrew = findViewById(R.id.rycyclercrew);
        JSONUtility.loadCrew(this); // loads INTO Storage
        // Adapter creation
        adapter = new CrewAdapter(Storage.getAllCrewMembers());
        //set adapter
        rycyclercrew.setLayoutManager(new LinearLayoutManager(this)); // tells RecyclerView how to arrange items
        rycyclercrew.setAdapter(adapter);

        // Setup button to move crew to training
        Button moveToSimulatorButton = findViewById(R.id.buttonMoveToSimulator);
        moveToSimulatorButton.setOnClickListener(v -> {
            // Add selected crew to training
            ArrayList<CrewMember> selectedCrew = adapter.getSelectedCrew();
            if (!selectedCrew.isEmpty()) {
                // Move crew to training
                for (CrewMember crew : selectedCrew) {
                    Storage.addToTraining(crew);
                }

                // Remove from main quarters list
                Storage.getAllCrewMembers().removeAll(selectedCrew);
                // Refresh RecyclerView
                adapter.setCrewList(new ArrayList<>(Storage.getAllCrewMembers())); // reload list
                // Save changes
                JSONUtility.saveCrew(this);
            }
        });
        // Setup button to move crew to mission
        Button missionButton = findViewById(R.id.ButtonMoveToMission);
        missionButton.setOnClickListener(v -> {
            ArrayList<CrewMember> selectedCrew = adapter.getSelectedCrew();
            // MUST be exactly 3
            if (selectedCrew.size() != 3) {
                Toast.makeText(this, "You must select EXACTLY 3 crew!", Toast.LENGTH_SHORT).show();
                return;
            }

            // block if mission already has crew
            if (!Storage.getMissionCrew().isEmpty()) {
                Toast.makeText(this, "Mission already has crew!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Move crew
            for (CrewMember crew : selectedCrew) {
                Storage.addToMission(crew);
                Storage.getAllCrewMembers().remove(crew);
            }

            // Clear selection and refresh UI
            adapter.clearSelection();
            adapter.setCrewList(new ArrayList<>(Storage.getAllCrewMembers()));
            adapter.notifyDataSetChanged();

            JSONUtility.saveCrew(this);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh RecyclerView in case new crew was added, checks if something has changed.
        if (adapter != null) {
            // Reset the list to reflect any changes made in Storage
            adapter.setCrewList(new ArrayList<>(Storage.getAllCrewMembers()));
            adapter.notifyDataSetChanged();
        }
    }
    // Button to recruitment screen
    public void rcruitGame(View view) {
        Intent intent = new Intent(this, RecruitmentActivity.class);
        startActivity(intent);

    }
    // Button to crew removal screen
    public void rmovecrew(View view) {
        Intent intent = new Intent(this, RemovalActivity.class);
        startActivity(intent);
    }
    // Button to training screen
    public void trainGamee(View view) {
        Intent intent = new Intent(this, TrainingActivity.class);
        startActivity(intent);


    }
    // Button to MissionControl screen
    public void missionGame(View view) {
        Intent intent = new Intent(this, MissionControlActivity.class);
        startActivity(intent);
    }
    // Back to main screen button
    public void backbtn2 (View view){
        finish();
    }
}

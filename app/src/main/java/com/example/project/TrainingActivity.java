package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TrainingActivity extends AppCompatActivity {
    private CrewAdapter adapter;
    private ArrayList<CrewMember> selectedCrew = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_main);
        // RecyclerView setup
        RecyclerView recyclerTraining = findViewById(R.id.recyclerTraining);
        // Adapter creation
        adapter = new CrewAdapter(Storage.getTrainingCrew());
        recyclerTraining.setLayoutManager(new GridLayoutManager(this, 2)); // grid layout, 3 columns
        //set adapter
        recyclerTraining.setAdapter(adapter);

        // Checkbox selection listener in adapter
        adapter.setSelectionListener(new CrewAdapter.SelectionListener() {
            // Called when the selection changes and updates the selectedCrew list
            @Override
            public void onSelectionChanged(ArrayList<CrewMember> newSelection) {
                selectedCrew = newSelection;
            }
        });

        // Train button
        Button trainButton = findViewById(R.id.buttonTrain);
        trainButton.setOnClickListener(v -> {

            ArrayList<CrewMember> toRemove = new ArrayList<>(); // crew to stop training

            for (CrewMember crew : selectedCrew) {
                if (crew.getEnergy() <= 0) {
                    toRemove.add(crew); // mark for removal
                    continue; // skip training
                }
                crew.addExperience(10, this);
                crew.restoreEnergy(-5); // energy cost
                if (crew.getEnergy() <= 0) {
                    toRemove.add(crew); // energy depleted after training
                }
            }

            // Remove crew with 0 energy from training selection
            selectedCrew.removeAll(toRemove);
            adapter.notifyDataSetChanged();
            JSONUtility.saveCrew(this);
        });

        // Move to Quarters button
        Button moveButton = findViewById(R.id.buttonMove);
        moveButton.setOnClickListener(v -> {
            if (!selectedCrew.isEmpty()) {
                for (CrewMember crew : selectedCrew) {
                    crew.restoreEnergy(crew.getMaxEnergy() - crew.getEnergy());
                    Storage.moveToQuarters(crew); // move to quarters and remove from training
                }
            }

            // Clear selection after moving
            selectedCrew.clear();
            // Refresh adapter with the updated Quarters list
            adapter.setCrewList(new ArrayList<>(Storage.getTrainingCrew()));
            adapter.notifyDataSetChanged();
            // Save updated crew
            JSONUtility.saveCrew(this);
        });
    }

    // Back to quarters button
    public void backbtn5 (View view){
        finish();
    }
}


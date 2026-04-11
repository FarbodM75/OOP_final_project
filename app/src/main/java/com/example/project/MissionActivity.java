package com.example.project;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

// Mission Screen (connected to Mission.java via MissionCallback)
public class MissionActivity extends AppCompatActivity implements Mission.MissionCallback {
    private Threat threat;
    private ArrayList<CrewMember> crew;
    private TextView turnText;
    private TextView crew1StatsText;
    private TextView crew2StatsText;
    private TextView crew3StatsText;
    private TextView threatStatsText;

    private Button attackBtn, defendBtn, specialBtn;

    // Mission logic controller
    private Mission missionController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_main);

        crew1StatsText = findViewById(R.id.crew1Stats);
        crew2StatsText = findViewById(R.id.crew2Stats);
        crew3StatsText = findViewById(R.id.crew3Stats);
        threatStatsText = findViewById(R.id.threatStats);
        turnText = findViewById(R.id.turnText);

        attackBtn = findViewById(R.id.buttonAttack);
        defendBtn = findViewById(R.id.buttonDefend);
        specialBtn = findViewById(R.id.buttonSpecial);

        turnText.setText("Your Turn"); // initial turn

        // Sets Background
        String bgName = Storage.getBackground();
        if (bgName == null) {
            bgName = BackgroundManager.getRandomBackground();
            Storage.setBackground(bgName);
        }
        int bgRes = getResources().getIdentifier(bgName, "drawable", getPackageName());
        ImageView bg = findViewById(R.id.missionBackground);
        bg.setImageResource(bgRes);

        // Load threat and crew
        threat = Storage.getCurrentThreat();
        crew = Storage.getMissionCrew();

        // Initialize the Mission Logic Controller
        missionController = new Mission(this, threat, crew, this);

        // Crew images
        ImageView crew1 = findViewById(R.id.crew1Image);
        ImageView crew2 = findViewById(R.id.crew2Image);
        ImageView crew3 = findViewById(R.id.crew3Image);
        ImageView threatImage = findViewById(R.id.threatImage);

        crew1.setImageResource(crew.get(0).getPosterResId(this));
        crew2.setImageResource(crew.get(1).getPosterResId(this));
        crew3.setImageResource(crew.get(2).getPosterResId(this));

        // Threat image
        int threatRes = getResources().getIdentifier(threat.getPoster(), "drawable", getPackageName());
        threatImage.setImageResource(threatRes);

        // Map buttons to the Mission controller
        attackBtn.setOnClickListener(v -> missionController.performTurn("attack"));
        defendBtn.setOnClickListener(v -> missionController.performTurn("defend"));
        specialBtn.setOnClickListener(v -> missionController.performTurn("special"));

        // Update stats initially
        updateStatsUI();
    }

    // Displays and updates Stats
    private void updateStatsUI() {
        crew1StatsText.setText("");
        crew2StatsText.setText("");
        crew3StatsText.setText("");

        if (crew.size() >= 1) {
            CrewMember c1 = crew.get(0);
            crew1StatsText.setText("Name: " + c1.getName() +
                    "\nEnergy: " + c1.getEnergy() + "/" + c1.getMaxEnergy() +
                    "\nSkill: " + c1.getSkill() +
                    "\nResilience: " + c1.getResilience());
        }

        if (crew.size() >= 2) {
            CrewMember c2 = crew.get(1);
            crew2StatsText.setText("Name: " + c2.getName() +
                    "\nEnergy: " + c2.getEnergy() + "/" + c2.getMaxEnergy() +
                    "\nSkill: " + c2.getSkill() +
                    "\nResilience: " + c2.getResilience());
        }

        if (crew.size() >= 3) {
            CrewMember c3 = crew.get(2);
            crew3StatsText.setText("Name: " + c3.getName() +
                    "\nEnergy: " + c3.getEnergy() + "/" + c3.getMaxEnergy() +
                    "\nSkill: " + c3.getSkill() +
                    "\nResilience: " + c3.getResilience());
        }

        if (threat != null) {
            threatStatsText.setText("Energy: " + threat.getEnergy()  +
                    "\nSkill: " + threat.getSkill() +
                    "\nResilience: " + threat.getResilience());
        }

        ImageView crew1 = findViewById(R.id.crew1Image);
        ImageView crew2 = findViewById(R.id.crew2Image);
        ImageView crew3 = findViewById(R.id.crew3Image);

        crew1.setImageDrawable(null);
        crew2.setImageDrawable(null);
        crew3.setImageDrawable(null);

        if (crew.size() >= 1) crew1.setImageResource(crew.get(0).getPosterResId(this));
        if (crew.size() >= 2) crew2.setImageResource(crew.get(1).getPosterResId(this));
        if (crew.size() >= 3) crew3.setImageResource(crew.get(2).getPosterResId(this));
    }

    // MissionCallback Interface Implementations (5 methods)
    @Override
    public void onUpdateUI() {
        updateStatsUI();
    }
    @Override
    public void onSetTurnText(String text) {
        turnText.setText(text);
    }
    @Override
    public void onSetButtonsEnabled(boolean enabled) {
        attackBtn.setEnabled(enabled);
        defendBtn.setEnabled(enabled);
        specialBtn.setEnabled(enabled);
    }
    @Override
    public void onMissionWonDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Victory!")
                .setMessage("Threat defeated! Crew gained experience.")
                .setCancelable(false)
                .setPositiveButton("Return", (dialog, which) -> {
                    finish();
                })
                .show();
    }
    @Override
    public void onMissionLostDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Defeat")
                .setMessage("All crew members were lost...")
                .setCancelable(false)
                .setPositiveButton("Return", (dialog, which) -> {
                    finish();
                })
                .show();
    }
}
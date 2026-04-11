package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StatisticsActivity extends AppCompatActivity {
    private RadioGroup crwtype;
    private EditText newcrwname;
    private Button newcrwBtn;
    private CrewMember crew = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_main);

        TextView winsText = findViewById(R.id.winsText);
        TextView lossesText = findViewById(R.id.lossesText);
        TextView streakText = findViewById(R.id.streakText);
        TextView totalMissionText = findViewById(R.id.totalMissionText);

        Statistics stats = Storage.getStatistics();
        // Display stats
        winsText.setText("Wins: " + stats.getTotalWins());
        lossesText.setText("Losses: " + stats.getTotalLosses());
        streakText.setText("Streak: " + stats.getWinStreak());
        totalMissionText.setText("Total Missions: " + stats.getTotalMissions());
    }
    // Back to main screen button
    public void backbtn1(View view) {
        finish();
    }
}
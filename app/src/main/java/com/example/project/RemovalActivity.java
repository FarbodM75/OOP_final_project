package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RemovalActivity extends AppCompatActivity {

    private EditText removeNameEditText;
    private Button removeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.removal_main);

        removeNameEditText = findViewById(R.id.removeNameEditText);
        removeBtn = findViewById(R.id.removeBtn);

        removeBtn.setOnClickListener(v -> {
            String nameToRemove = removeNameEditText.getText().toString().trim();
            // Remove crew member by name
            if (!nameToRemove.isEmpty()) {
                boolean removed = Storage.removeCrewMemberByName(this, nameToRemove);
                if (removed) {
                    Toast.makeText(this, "Crew member says goodbye :(", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Crew member not found", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Back to quarters screen button
    public void backbtn4(View view) {
        finish();
    }
}
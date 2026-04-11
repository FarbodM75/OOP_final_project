package com.example.project;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecruitmentActivity extends AppCompatActivity {
    private RadioGroup crwtype;
    private EditText newcrwname;
    private Button newcrwBtn;
    private CrewMember crew = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recruitment_main); // IMPORTANT

        crwtype = findViewById(R.id.crwtype);
        newcrwBtn = findViewById(R.id.newcrwBtn);
        newcrwname = findViewById(R.id.newcrwname);

        // Create new crew button
        newcrwBtn.setOnClickListener(v -> {
            String name = newcrwname.getText().toString();
            int checkedId = crwtype.getCheckedRadioButtonId();
            if (name.isEmpty()) { // validate name
                Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (checkedId == R.id.plt) {
                try {
                    crew = new Pilot(name, 10, 5, 100, 100, 5, "pilot_img");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }

            } else if (checkedId == R.id.eng) {
                try {
                    crew = new Engineer(name, 8, 7, 70, 70, 10, "engineer_img");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
            else if (checkedId == R.id.mdc) {
                try {
                    crew = new Medic(name, 7, 3, 110, 110, 90, "medic_img");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
            else if (checkedId == R.id.stst) {
                try {
                    crew = new Scientist(name, 8, 5, 70, 70, 50, "scientist_img");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
            else if (checkedId == R.id.sldr) {
                try {
                    crew = new Soldier(name, 15, 8, 120, 120, 85, "soldier_img");

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
            // If no crew type is selected
            else {
                Toast.makeText(getApplicationContext(), "Select a crew type", Toast.LENGTH_SHORT).show();
                return;
            }
            // Add to Storage and save
            Storage.addCrewMember(crew);
            JSONUtility.saveCrew(this);
            Toast.makeText(getApplicationContext(), "Crew member created: " + crew.getName(), Toast.LENGTH_SHORT).show();
        });
    }
    // Back to quarters screen button
    public void backbtn3(View view) {
        finish();
    }
}





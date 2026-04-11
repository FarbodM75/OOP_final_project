package com.example.project;

import android.content.Context;
import android.widget.Toast;

public abstract class CrewMember {
    private String name;
    private int skill;
    private int resilience;
    private int energy;
    private int maxEnergy;
    private int experience;
    String posterName;

    public CrewMember(String name, int skill, int resilience, int energy, int maxEnergy, int experience, String posterName) {
        this.name = name;
        this.skill = skill;
        this.resilience = resilience;
        this.energy = energy;
        this.maxEnergy = maxEnergy;
        this.experience = experience;
        this.posterName = posterName;
    }
    // Getters
    public String getName() {
        return name;
    }
    public String getType() {
        return this.getClass().getSimpleName();
    }
    public String getPosterName() {
        return posterName;
    }
    public int getPosterResId(Context context) {
        int resId = context.getResources().getIdentifier(
                posterName, "drawable", context.getPackageName());

        if (resId == 0) {
            return android.R.drawable.ic_menu_report_image;
        }
        return resId;
    }
    public int getSkill() {
        return skill;
    }
    public int getResilience() {
        return resilience;
    }
    public int getEnergy() {
        return energy;
    }
    public int getMaxEnergy() {
        return maxEnergy;
    }
    public int getExperience() {
        return experience;
    }

    // to add experience in Training or Mission
    // Able to level up skill and resilience
    public void addExperience(int experience, Context context) {
        this.experience += experience;
        if (this.experience >= 100) {
            this.experience -= 100;
            this.skill += 1;
            this.resilience += 1;
            this.maxEnergy += 5;
            Toast.makeText(context, "Crew has levelled up!", Toast.LENGTH_SHORT).show();
        }
    }
    // Attacks threat
    public abstract void act(Threat threat);
    // Takes damage
    public void takeDamage(int damage) {
        energy -= damage;
        if (energy < 0) energy = 0; // prevent negative energy
    }
    // Restores energy
    public void restoreEnergy(int energy) {
        this.energy += energy;
        if (this.energy > maxEnergy) {
            this.energy = maxEnergy;
        }
    }
    // Checks if crew is alive
    public boolean isAlive() {

        return energy > 0;
    }
}



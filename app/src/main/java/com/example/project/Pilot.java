package com.example.project;

import android.content.Context;
/**
 Pilot Class
 */
public class Pilot extends CrewMember {

    public Pilot(String name, int skill, int resilience, int energy, int maxEnergy, int experience, String posterName) {
        super(name, skill, resilience, energy, maxEnergy, experience, posterName);
    }

    public int getPosterResId(Context context) {
        int resId = context.getResources().getIdentifier(posterName, "drawable", context.getPackageName());

        if (resId == 0) {
            return android.R.drawable.ic_menu_report_image; // fallback
        }
        return resId;
    }

    @Override
    public void act(Threat threat) {
        int damage = getSkill() - threat.getResilience();
        if (damage < 0) damage = 0;
        threat.takeDamage(damage);
    }
}


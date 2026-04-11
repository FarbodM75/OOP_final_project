package com.example.project;

public class Threat {
    String name;
    int skill;
    int resilience;
    int energy;
    String poster;
    //constructor
    public Threat(String name, int skill, int resilience, int energy, String poster) {
        this.name = name;
        this.skill = skill;
        this.resilience = resilience;
        this.energy = energy;
        this.poster = poster;
    }
    //getters
    public int getSkill() {
        return skill;
    }
    public int getResilience() {
        return resilience;
    }
    public int getEnergy() {
        return energy;
    }
    public String getPoster() {
        return poster;
    }
    public String getName() {
        return name;
    }

    //setter
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    // methods
    public void attack(CrewMember crew) {
        int damage = skill - crew.getResilience();
        if (damage < 0) damage = 0;
        crew.takeDamage(damage); // create a takeDamage() in CrewMember
    }
    public void takeDamage(int damage) {
        this.energy -= damage;
        if (this.energy < 0) this.energy = 0;
    }
}

package com.example.arnarfreyr.dicegame3000;

import java.util.ArrayList;

/**
 * Class for a group of dice.
 * Groups many die together in a group of dice.
 *
 * Created by arnarfreyr on 7.8.2017.
 */

public class Dice {

    // Init array list of multiple die
    private ArrayList<Die> dice;

    /**
     * Constructor
     */
    public Dice() {
        // Init values for args
        this.dice = new ArrayList<>();
    }

    /**
     * Get dice array list
     * @return Array list of dice
     */
    public ArrayList<Die> getDice() {
        return this.dice;
    }

    /**
     * Set dice array list
     */
    public void setDice() {
        this.dice = dice;
    }

    /**
     * Get a specific die from the ArrayList of dice
     * @param loc Location in the list to return
     * @return Die
     */
    public Die getDie(Integer loc) {
        return this.dice.get(loc);
    }

    /**
     * Check if die is present in dice list
     * @param die Single die
     * @return bool if present
     */
    public Boolean getDie(Die die) {
        if (this.dice.contains(die)) {
            return true;
        }
        return false;
    }

    /**
     * Add die to the dice group
     * @param die Die object
     */
    public void addDie(Die die) {
        this.dice.add(die);
    }

    /**
     * Add a completely new die to the Dice group
     */
    public void addDie() {
        Die die = new Die();
        this.dice.add(die);
    }

    /**
     * Fill the Dice group
     */
    public void fill() {
        // Clear the group, if any old dice are remaining
        this.dice.clear();
        // Fill the group with six dice
        for (int i = 0; i < 6; i++) {
            // Add a new die
            addDie();
        }
    }

    /**
     * Set random value to each die in the group
     */
    public void randomizeDice() {
        // Run through all the dice in the group
        for (Die die : this.dice) {
            // Set a random value for the current die
           die.setRandomValue();
        }
    }
}

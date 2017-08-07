package com.example.arnarfreyr.dicegame3000;

import java.util.ArrayList;

/**
 * Class for a group of dice
 *
 * Created by arnarfreyr on 7.8.2017.
 */

public class Dice {

    // Init array list of multiple die
    ArrayList<Die> dice;

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
     * @param dice ArrayList of dice
     */
    public void setDice(ArrayList<Die> dice) {
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
     * Set a die in a certain location in the list
     * @param loc Location of insert
     * @param die Die to be inserted
     */
    public void setDie(Integer loc, Die die) {
        this.dice.set(loc, die);
    }

    /**
     * Remove die in list based on location
     * @param loc Location of die in list
     */
    public void removeDie(Integer loc) {
        this.dice.remove(loc);
    }

    /**
     * Remove die in list based on a certain die
     * @param die Die to be removed
     */
    public  void removeDie(Die die) {
        this.dice.remove(die);
    }
}

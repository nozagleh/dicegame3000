package com.example.arnarfreyr.dicegame3000;

/**
 * Class for each die present in the game.
 *
 * Hold the die, value, if the die is locked or selected.
 *
 * Created by arnarfreyr on 7.8.2017.
 */

public class Die {

    // Init die args
    private Integer dieValue;
    private Boolean chosen;

    /**
     * Constructor for a die
     */
    public Die() {
        // Init value for die arguments
        int rand = (int)(Math.random() * 6 + 1);
        this.dieValue = rand;
        this.chosen = false;
    }

    /**
     * Get die value
     * @return Die value
     */
    public Integer getDieValue() {
        return this.dieValue;
    }

    /**
     * Set die value
     * @param value Value of die
     */
    public void setDieValue(Integer value) {
        this.dieValue = value;
    }

    /**
     * Get if the current die has been chosen or not
     * @return Boolean value if die has been chosen
     */
    public Boolean getIfChosen() {
        return this.chosen;
    }

    /**
     * Set the chosen flag of the die.
     * @param chosen setif chosen
     */
    public void setIfChosen(Boolean chosen) {
        this.chosen = chosen;
    }

    /**
     * Toggle true/false if the die is chosen or not
     */
    public void toggleChosen() {
        // Check if chosen
        if (this.chosen) {
            // Then set to false
            this.chosen = false;

            // Return
            return;
        }
        // Otherwise set chosen to true
        this.chosen = true;
    }

    /**
     * Set the die value to random
     */
    public void setRandomValue() {
        // Check first if the dice is not locked (chosen)
        if(!getIfChosen()) {
            // Change the die value to random between 1-6
            this.dieValue = (int)(Math.random() * 6 + 1);
        }
    }
}

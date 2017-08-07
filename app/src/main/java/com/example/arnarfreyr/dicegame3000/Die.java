package com.example.arnarfreyr.dicegame3000;

/**
 * Class for each die present in the game.
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
        this.dieValue = 0;
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
}

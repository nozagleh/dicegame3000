package com.example.arnarfreyr.dicegame3000;

import java.net.Inet4Address;
import java.sql.Array;
import java.util.ArrayList;

/**
 * Class for the game play logic for the game
 * Stores arguments for rules and keeps track of the status of the game
 *
 * Created by arnarfreyr on 7.8.2017.
 */

public class GamePlay {

    // Init class args

    // Static game rules
    private static Integer MAX_ROLLS = 3;
    private static Integer MAX_ROUNDS = 10;

    // Arrays
    private ArrayList<Dice> diceList;
    private ArrayList<Integer> roundsScore;
    private ArrayList<Integer> chosenBets;

    // Bools for status checking
    private Boolean isStarted;

    // Integers for keeping track of scores and game status
    private Integer betType;
    private Integer rollNr;
    private Integer roundNr;
    private Integer score;

    /**
     * Constructor
     */
    public GamePlay() {
        // Init arraylist variables
        this.diceList = new ArrayList<>();
        this.roundsScore = new ArrayList<>();
        this.chosenBets = new ArrayList<>();

        // Init boolean variables
        this.isStarted = false;

        // Init int variables
        this.betType = 0;
        this.rollNr = 0;
        this.roundNr = 0;
        this.score = 0;
    }

    /**
     * Get max rolls allowed per round
     * @return Max rolls allowed
     */
    public Integer getMaxRolls() {
        return MAX_ROLLS;
    }

    /**
     * Get max rounds allowed per game
     * @return Max rounds allowed
     */
    public Integer getMaxRounds() {
        return MAX_ROUNDS;
    }

    /**
     * Add dice group to the dice list
     * @param dice Group of die
     */
    public void addDice(Dice dice) {
        this.diceList.add(dice);
    }

    /**
     * Remove dice group from the dice list based on location
     * @param loc Location of the group in the list to be removed
     */
    public void removeDice(Integer loc) {
        this.diceList.remove(loc);
    }

    /**
     * Remove dice group from the dice list based on a dice group
     * @param dice Dice group to be removed from the list
     */
    public void removeDice(Dice dice) {
        this.diceList.remove(dice);
    }

    /**
     * Get the current score of the game
     * @return Current score
     */
    public Integer getScore() {
        return this.score;
    }

    /**
     * Set the score of the game
     * @param score Score to be set
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * Reset the score of the game to zero
     */
    public void resetScore() {
        this.setScore(0);
    }

    /**
     * Adds score points to the score of the game
     * @param score Score to be added to the game score
     */
    public void addScore(Integer score) {
        this.score += score;
    }

    /**
     * Add one point to the score of the game
     */
    public void addScore() {
        this.score++;
    }

    /**
     * Add a round to the game
     */
    public void addRound() {
        this.roundNr++;
    }

    /**
     * Add a roll to the game
     */
    public void addRoll() {
        this.rollNr++;
    }

    /**
     * Get the current bet type selected
     * @return Bet type (integer)
     */
    public Integer getBetType() {
        return this.betType;
    }

    /**
     * Set the bet type of the round
     * @param betType Bet type, represented in int number
     */
    public void setBetType(Integer betType) {
        this.betType = betType;
    }
}

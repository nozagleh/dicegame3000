package com.example.arnarfreyr.dicegame3000;

import android.util.Log;

import java.net.Inet4Address;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

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

    // Dice
    private Dice dice;

    // Arrays
    private ArrayList<Dice> diceList;
    private ArrayList<Integer> roundsScore;
    private ArrayList<Integer> chosenBets;

    private String[] bets = {"Low", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    // Bools for status checking
    private Boolean isStarted;
    private Boolean roundEnded;
    private Boolean gameEnded;

    // Integers for keeping track of scores and game status
    private Integer betType;
    private Integer rollNr;
    private Integer roundNr;
    private Integer score;

    /**
     * Constructor
     */
    public GamePlay(Dice dice) {
        this.dice = new Dice();
        // Set starting dice
        this.dice = dice;

        initVariables();
    }

    public GamePlay() {
        initVariables();
    }

    public void initVariables() {
        this.dice = new Dice();

        // Init arraylist variables
        this.diceList = new ArrayList<>();
        this.roundsScore = new ArrayList<>();
        this.chosenBets = new ArrayList<>();

        // Init boolean variables
        this.isStarted = false;
        this.roundEnded = false;
        this.gameEnded = false;

        // Init int variables
        this.betType = 0;
        this.rollNr = 0;
        this.roundNr = 0;
        this.score = 0;
    }

    /**
     * Check if a new game has been started
     * @return bool if not to start a game or not, based on if a game is already in progress
     */
    public Boolean getIsStarted() {
       return this.isStarted;
    }

    /**
     * Start a new game
     */
    public void startGame() {
        this.isStarted = true;
    }

    /**
     * Check if the game is suppose to end, and do procedures needed to finish the game
     * @return boolean if the game is ending
     */
    public void endGame() {
        this.gameEnded = true;
    }

    public Boolean hasGameEnded() {
        return this.gameEnded;
    }

    /**
     * Add dice group to the dice list
     * @param dice Group of die
     */
    public void addDice(Dice dice) {
        this.diceList.add(dice);
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

    public void resetRolls() {
        this.rollNr = 0;
    }

    public Integer getRollNr() {
        return this.rollNr;
    }

    public void setRollNr(Integer rollNr) {
        this.rollNr = rollNr;
    }

    public void setRoundNr(Integer roundNr) {
        this.roundNr = roundNr;
    }

    public Integer getRoundNr() {
        return this.roundNr;
    }

    /**
     * Check if current round is the last round
     * @return Boolean if it is the last round
     */
    public Boolean isLastRound() {
        if(this.roundNr >= MAX_ROUNDS) {
            return true;
        }
        return false;
    }

    public Boolean isLastRoll() {
        if (this.rollNr.equals(MAX_ROLLS))
            return true;
        return false;
    }

    public Boolean isRollReset() {
        if (this.rollNr > MAX_ROLLS) {
            return true;
        }
        return false;
    }

    public void roll() {
        addRoll();

        if( this.rollNr == 1 ) {
            // Repopulate the dice with new dice values
            this.dice.fill();
        }
        this.roundEnded = false;

        if (isRollReset() && isBetDone(this.betType))
            return;

        // Check if max rolls for a round has been reached
        if(isRollReset()) {
            // Add a new round
            addRound();
            // Calculate score
            saveRoundScore();

            this.roundEnded = true;
        } else {
            // Randomize the dice group
            this.dice.randomizeDice();
        }

        if(isRollReset() && isLastRound())
            this.gameEnded = true;

        if (isRollReset()) {
            // Reset tolls
            resetRolls();
        }
    }

    /**
     * Return the current set of dice
     * @return Dice class object, consists of many Die
     */
    public Dice getDice() {
        return this.dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public ArrayList<Dice> getAllDice() {
        return this.diceList;
    }

    public ArrayList<Integer> getValues() {
        return this.dice.getDieValues();
    }

    /**
     * Set a specific die to chosen
     * @param dieNr The number of the die
     */
    public void setDieChosen(int dieNr) {
        this.dice.getDie(dieNr).toggleChosen();
    }

    public void saveRoundScore() {
        Dice roundGroup = this.dice;
        this.chosenBets.add(getBetType());

        CalculateScore calc = new CalculateScore(roundGroup, getBetType());
        setScore(calc.calculateScore());

        this.roundsScore.add(getScore());
        addDice(this.dice);
        Log.d("CHOSEN BET ---> ", getBetType().toString());
        Log.d("TOTAL SCORE -->", getScore().toString());
        this.score = 0;
    }

    public Integer getRoundScore() {
        int scoreArrSize = this.roundsScore.size();
        return this.roundsScore.get(scoreArrSize - 1);
    }

    public void setRoundScore(Integer roundScore) {
        this.roundsScore.add(roundScore);
    }

    public ArrayList<Integer> getTotalScore() {
        return this.roundsScore;
    }

    public Integer getFinalScore() {
        int score = 0;
        for (int roundScore : this.roundsScore) {
            score += roundScore;
        }

        return score;
    }

    public Boolean isBetDone(int betNr) {
        for (int bet: this.chosenBets) {
            if(betNr == bet) {
                return true;
            }
        }
        return false;
    }

    public Boolean betAlreadyDone() {
        return this.chosenBets.contains(this.betType);
    }

    public ArrayList<Integer> getBetsDone() {
        return this.chosenBets;
    }

    public void addBet(Integer betType) {
        this.chosenBets.add(betType);
    }

    public Boolean endOfRound() {
        return this.roundEnded;
    }
}

package com.nozagleh.dicegame3000;

import android.util.Log;

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

    // Dice
    private Dice dice;

    // Arrays
    private ArrayList<Dice> diceList;
    private ArrayList<Integer> roundsScore;
    private ArrayList<Integer> chosenBets;

    // List of bets available
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
     * Constructor that takes in Dice group as a param
     * @param dice Dice group
     */
    public GamePlay(Dice dice) {
        this.dice = new Dice();
        // Set starting dice
        this.dice = dice;

        // Call init variables function
        initVariables();
    }

    /**
     * Default constructor
     */
    public GamePlay() {
        // Call init variables function
        initVariables();
    }

    /**
     * Init the gameplay variables
     */
    public void initVariables() {
        // Create a new dice list
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

    /**
     * Check if the game has ended
     * @return boolean, if game has ended
     */
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

    /**
     * Reset the roll number
     */
    public void resetRolls() {
        this.rollNr = 0;
    }

    /**
     * Get the roll number
     * @return Integer roll number
     */
    public Integer getRollNr() {
        return this.rollNr;
    }

    /**
     * Set the roll number
     * @param rollNr Integer roll number
     */
    public void setRollNr(Integer rollNr) {
        this.rollNr = rollNr;
    }

    /**
     * Set the round number
     * @param roundNr Integer round number
     */
    public void setRoundNr(Integer roundNr) {
        this.roundNr = roundNr;
    }

    /**
     * Get the round number
     * @return Integer round number
     */
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

    /**
     * Check if it is the last roll
     * @return boolean if is last
     */
    public Boolean isLastRoll() {
        // Check if the roll number equals to max rolls
        if (this.rollNr.equals(MAX_ROLLS))
            return true;
        return false;
    }

    /**
     * Check if the rolls are to be reset
     * @return boolean if to be reset
     */
    public Boolean isRollReset() {
        // Check if the roll number is greater than max rolls
        if (this.rollNr > MAX_ROLLS) {
            return true;
        }
        return false;
    }

    /**
     * Roll the dice
     */
    public void roll() {
        // Add a roll
        addRoll();

        Log.d("ROLLING", "DICE");
        // Check if it is the first roll
        if( getRollNr() == 1 ) {
            // Repopulate the dice with new dice values
            this.dice.fill();
        }

        // Set round ended to false
        this.roundEnded = false;

        // If the roll is to be reset and the bet is done, return
        if (isRollReset() && isBetDone(this.betType))
            return;

        // Check if max rolls for a round has been reached
        if(isRollReset()) {
            // Add a new round
            addRound();
            // Calculate score
            saveRoundScore();
            // Set round ended to true
            this.roundEnded = true;
        } else {
            // Randomize the dice group
            this.dice.randomizeDice();
        }

        // Check if the roll is to be reset and if it is the last round, set game as ended
        if(isRollReset() && isLastRound())
            this.gameEnded = true;

        // Check if the rolls are to be reset
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

    /**
     * Set dice group
     * @param dice Dice group
     */
    public void setDice(Dice dice) {
        this.dice = dice;
    }

    /**
     * Get all dice in a list
     * @return
     */
    public ArrayList<Dice> getAllDice() {
        return this.diceList;
    }

    /**
     * Set a specific die to chosen
     * @param dieNr The number of the die
     */
    public void setDieChosen(int dieNr) {
        this.dice.getDie(dieNr).toggleChosen();
    }

    /**
     * Save the round score
     */
    public void saveRoundScore() {
        Dice roundGroup = this.dice;
        // Add the bet to done bets
        this.chosenBets.add(getBetType());

        // Init the calculation class and pass the dice group as well as the bet type
        CalculateScore calc = new CalculateScore(roundGroup, getBetType());

        // Set the score of the round
        setScore(calc.calculateScore());

        // Add the score to the score list
        this.roundsScore.add(getScore());

        // Add the dice to used dice
        addDice(this.dice);

        // Reset the current round score
        this.score = 0;
    }

    /**
     * Get the round score
     * @return Integer round score, -1 if empty
     */
    public Integer getRoundScore() {
        // Get the size of the round score array
        int scoreArrSize = this.roundsScore.size();

        if (scoreArrSize > 0) {
            // Return the last item in the round score array
            return this.roundsScore.get(scoreArrSize - 1);
        }
        return -1;
    }

    /**
     * Set the round score
     * @param roundScore Integer score for the round
     */
    public void setRoundScore(Integer roundScore) {
        // Add the score to the score list
        this.roundsScore.add(roundScore);
    }

    /**
     * Get the total score of the game
     * @return return the roundscore array
     */
    public ArrayList<Integer> getTotalScore() {
        return this.roundsScore;
    }

    /**
     * Get the final score, adding all the round scores togehter
     * @return Integer final game score
     */
    public Integer getFinalScore() {
        // set the score to 0
        int score = 0;
        // Iterate through each round score and add it to the score
        for (int roundScore : this.roundsScore) {
            score += roundScore;
        }

        // Return the score
        return score;
    }

    /**
     * Check if a bet is done
     * @param betNr int bet number
     * @return boolean if the bet is done or not
     */
    public Boolean isBetDone(int betNr) {
        // Iterate through the done bets list and compare with the param
        for (int bet: this.chosenBets) {
            // If the bet is found in the list, return true
            if(betNr == bet) {
                return true;
            }
        }

        // Return false if nothing was found in the list
        return false;
    }

    /**
     * Check if the currently selected bet is done or not
     * @return boolean if currently selected bet is done
     */
    public Boolean betAlreadyDone() {
        return this.chosenBets.contains(this.betType);
    }

    /**
     * Get all done bets
     * @return array list of bets done (int)
     */
    public ArrayList<Integer> getBetsDone() {
        return this.chosenBets;
    }

    /**
     * Add a new bet to the list
     * @param betType Integer bet type to be added
     */
    public void addBet(Integer betType) {
        this.chosenBets.add(betType);
    }

    /**
     * Check if it is the end of the round
     * @return boolean if the end of round
     */
    public Boolean endOfRound() {
        return this.roundEnded;
    }
}

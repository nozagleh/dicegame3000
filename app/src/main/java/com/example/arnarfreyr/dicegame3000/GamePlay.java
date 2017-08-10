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

    // Bools for status checking
    private Boolean isStarted;
    private Boolean triggerLastRoll;

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

        // Init arraylist variables
        this.diceList = new ArrayList<>();
        this.roundsScore = new ArrayList<>();
        this.chosenBets = new ArrayList<>();

        // Init boolean variables
        this.isStarted = false;
        this.triggerLastRoll = false;

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
     * Start a new game
     * @return bool if not to start a game or not, based on if a game is already in progress
     */
    public Boolean startGame() {
        if(this.isStarted) {
            return false;
        }
        return true;
    }

    /**
     * Check if the game is suppose to end, and do procedures needed to finish the game
     * @return boolean if the game is ending
     */
    public Boolean endGame() {
        if(isLastRound()) {
            return true;
        }
        return false;
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

    public void resetRolls() {
        this.rollNr = 0;
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
        if (this.triggerLastRoll) {
            this.triggerLastRoll = false;
            return true;
        }
        return false;
    }

    public void roll() {
        // Check if max rolls for a round has been reached
        if(this.rollNr >= MAX_ROLLS) {
            // Add a new round
            addRound();
            // Reset the rolls
            resetRolls();
            Log.d("GP -->", "Save score");
            // Calculate score
            saveRoundScore();

            // Add the old dice to an array list
            addDice(this.dice);
            // Repopulate the dice with new dice values
            this.dice.fill();

            this.triggerLastRoll = true;

            return;
        }
        // Add a new roll
        addRoll();
        this.dice.randomizeDice();
    }

    /**
     * Return the current set of dice
     * @return Dice class object, consists of many Die
     */
    public Dice getDice() {
        return this.dice;
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
        Log.d("Bet type --> ",getBetType().toString());
        if ( getBetType() < 1 ) {
            for (Die die : roundGroup.getDice()) {
                if ( die.getDieValue() <= 3 ) {
                    addScore();
                }
            }
        }else {
            int betValue = getBetType() + 3;
            Dice usedDice = new Dice();
            for(Integer i = 0; i < roundGroup.getDice().size(); i++){
                Die die1 = roundGroup.getDice().get(i);
                Log.d("die1 ---> ", die1.getDieValue().toString());
                if (die1.getDieValue() == betValue
                        && !usedDice.getDie(die1)) {
                    Log.d("D1# ->", i.toString());
                    Log.d("D1 MATCH--->", die1.getDieValue().toString());
                    addScore();
                    usedDice.addDie(die1);
                    continue;
                }
                for(Integer x = i+1; x < roundGroup.getDice().size(); x++){
                    Die die2 = roundGroup.getDice().get(x);
                    Log.d("die2 ---> ", die2.getDieValue().toString());
                    if (die1.getDieValue() + die2.getDieValue() == betValue
                            && !usedDice.getDie(die1) && !usedDice.getDie(die2)) {
                        Log.d("D1# ->", i.toString());
                        Log.d("D2# ->", x.toString());
                        Log.d("D1+D2 MATCH --->", die2.getDieValue().toString());
                        usedDice.addDie(die1);
                        usedDice.addDie(die2);
                        addScore();
                        continue;
                    }
                    for(Integer y = x+1; y < roundGroup.getDice().size(); y++){
                        Die die3 = roundGroup.getDice().get(y);
                        //Log.d("die3 ---> ", die3.getDieValue().toString());
                        if (die1.getDieValue() + die2.getDieValue() + die3.getDieValue() == betValue
                                && !usedDice.getDie(die1) && !usedDice.getDie(die2) && !usedDice.getDie(die3)) {
                            Log.d("D1# ->", i.toString());
                            Log.d("D2# ->", x.toString());
                            Log.d("D3# ->", y.toString());
                            Log.d("D1+D2+D3 --->", die3.getDieValue().toString());
                            usedDice.addDie(die1);
                            usedDice.addDie(die2);
                            usedDice.addDie(die3);
                            addScore();
                            continue;
                        }
                        for(Integer z = y+1; z < roundGroup.getDice().size(); z++){
                            Die die4 = roundGroup.getDice().get(z);
                            //Log.d("die4 ---> ", die4.getDieValue().toString());
                            if (die1.getDieValue() + die2.getDieValue() + die3.getDieValue() + die4.getDieValue() == betValue
                                    && !usedDice.getDie(die1) && !usedDice.getDie(die2)
                                    && !usedDice.getDie(die3) && !usedDice.getDie(die4)) {
                                Log.d("D1# ->", i.toString());
                                Log.d("D2# ->", x.toString());
                                Log.d("D3# ->", y.toString());
                                Log.d("D4# ->", z.toString());
                                Log.d("D1+D2+D3+D4 --->", die4.getDieValue().toString());
                                usedDice.addDie(die1);
                                usedDice.addDie(die2);
                                usedDice.addDie(die3);
                                usedDice.addDie(die4);
                                addScore();
                                continue;
                            }
                            for(Integer a = z+1; a < roundGroup.getDice().size(); a++){
                                Die die5 = roundGroup.getDice().get(a);
                                //Log.d("die5 ---> ", die5.getDieValue().toString());
                                if (die1.getDieValue() + die2.getDieValue()
                                        + die3.getDieValue() + die4.getDieValue()
                                        + die5.getDieValue() == betValue
                                        && !usedDice.getDie(die1) && !usedDice.getDie(die2)
                                        && !usedDice.getDie(die3) && !usedDice.getDie(die4)
                                        && !usedDice.getDie(die5)) {
                                    Log.d("D1# ->", i.toString());
                                    Log.d("D2# ->", x.toString());
                                    Log.d("D3# ->", y.toString());
                                    Log.d("D4# ->", z.toString());
                                    Log.d("D5# ->", a.toString());
                                    Log.d("D1+D2+D3+D4+D5 --->", die5.getDieValue().toString());
                                    usedDice.addDie(die1);
                                    usedDice.addDie(die2);
                                    usedDice.addDie(die3);
                                    usedDice.addDie(die4);
                                    usedDice.addDie(die5);
                                    addScore();
                                    continue;
                                }
                                for(Integer b = a+1; b < roundGroup.getDice().size(); b++){
                                    Die die6 = roundGroup.getDice().get(b);
                                    //Log.d("die6 ---> ", die6.getDieValue().toString());
                                    if (die1.getDieValue() + die2.getDieValue()
                                            + die3.getDieValue() + die4.getDieValue()
                                            + die5.getDieValue() + die6.getDieValue() == betValue
                                            && !usedDice.getDie(die1) && !usedDice.getDie(die2)
                                            && !usedDice.getDie(die3) && !usedDice.getDie(die4)
                                            && !usedDice.getDie(die5) && !usedDice.getDie(die6)) {
                                        Log.d("D1# ->", i.toString());
                                        Log.d("D2# ->", x.toString());
                                        Log.d("D3# ->", y.toString());
                                        Log.d("D4# ->", z.toString());
                                        Log.d("D5# ->", a.toString());
                                        Log.d("D6# ->", b.toString());
                                        Log.d("D1+D2+D3+D4+D5+D6 --->", die6.getDieValue().toString());
                                        usedDice.addDie(die1);
                                        usedDice.addDie(die2);
                                        usedDice.addDie(die3);
                                        usedDice.addDie(die4);
                                        usedDice.addDie(die5);
                                        usedDice.addDie(die6);
                                        addScore();
                                    }
                                }
                            }
                        }
                    }
                }
                //Log.d("FIRST ----> ", "LOOP");
            }
        }
        this.roundsScore.add(getScore());
        Log.d("CHOSEN BET ---> ", getBetType().toString());
        Log.d("TOTAL SCORE -->", getScore().toString());
    }

    public Integer getRoundScore() {
        Log.d("GP -->", "Get round score");
        int scoreArrSize = this.roundsScore.size();
        return this.roundsScore.get(scoreArrSize - 1);
    }

}

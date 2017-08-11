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
        // Check if max rolls for a round has been reached
        if(isRollReset()) {
            // Add a new round
            addRound();
            // Calculate score
            saveRoundScore();
            // Add the old dice to an array list
            addDice(this.dice);

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
                //Log.d("die1 ---> ", die1.getDieValue().toString());
                if (die1.getDieValue() == betValue
                        && !usedDice.getDie(die1)) {
                    /*Log.d("D1# ->", i.toString());
                    Log.d("D1 MATCH--->", die1.getDieValue().toString());*/
                    addScore();
                    usedDice.addDie(die1);
                    continue;
                }
                for(Integer x = i+1; x < roundGroup.getDice().size(); x++){
                    Die die2 = roundGroup.getDice().get(x);
                    //Log.d("die2 ---> ", die2.getDieValue().toString());
                    if (die1.getDieValue() + die2.getDieValue() == betValue
                            && !usedDice.getDie(die1) && !usedDice.getDie(die2)) {
                        /*Log.d("D1# ->", i.toString());
                        Log.d("D2# ->", x.toString());
                        Log.d("D1+D2 MATCH --->", die2.getDieValue().toString());*/
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
                            /*Log.d("D1# ->", i.toString());
                            Log.d("D2# ->", x.toString());
                            Log.d("D3# ->", y.toString());
                            Log.d("D1+D2+D3 --->", die3.getDieValue().toString());*/
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
                                /*Log.d("D1# ->", i.toString());
                                Log.d("D2# ->", x.toString());
                                Log.d("D3# ->", y.toString());
                                Log.d("D4# ->", z.toString());
                                Log.d("D1+D2+D3+D4 --->", die4.getDieValue().toString());*/
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
                                    /*Log.d("D1# ->", i.toString());
                                    Log.d("D2# ->", x.toString());
                                    Log.d("D3# ->", y.toString());
                                    Log.d("D4# ->", z.toString());
                                    Log.d("D5# ->", a.toString());
                                    Log.d("D1+D2+D3+D4+D5 --->", die5.getDieValue().toString());*/
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
                                        /*Log.d("D1# ->", i.toString());
                                        Log.d("D2# ->", x.toString());
                                        Log.d("D3# ->", y.toString());
                                        Log.d("D4# ->", z.toString());
                                        Log.d("D5# ->", a.toString());
                                        Log.d("D6# ->", b.toString());
                                        Log.d("D1+D2+D3+D4+D5+D6 --->", die6.getDieValue().toString());*/
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
            }
        }
        this.roundsScore.add(getScore());
        Log.d("CHOSEN BET ---> ", getBetType().toString());
        Log.d("TOTAL SCORE -->", getScore().toString());
    }

    public Integer getRoundScore() {
        int scoreArrSize = this.roundsScore.size();
        return this.roundsScore.get(scoreArrSize - 1);
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

    public Boolean endOfRound() {
        return this.roundEnded;
    }
}

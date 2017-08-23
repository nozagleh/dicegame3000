package com.example.arnarfreyr.dicegame3000;

import android.util.Log;

import java.util.ArrayList;

/**
 * Class for calculating the score of each round.
 * Runs through each possible combination from 1 to 6 dice
 * If any of those combinations equal to the bet set then
 * the score will be added to the users total score
 *
 * Created by arnarfreyr on 20.8.2017.
 */

public class CalculateScore {
    // Init private variables
    private Dice dice;
    private Dice usedDice;
    private ArrayList<Dice> combinations;

    // Init integers for the bet and the score
    private int bet;
    private int score;

    /**
     * Constructor that takes in a Dice group and the chosen bet
     * @param dice Dice group, dice of the round
     * @param bet int Bet number
     */
    public CalculateScore(Dice dice, int bet) {
        // Set the dice
        this.dice = dice;
        // Init the used dice group to an empty Dice group
        usedDice = new Dice();
        // Set the bet
        this.bet = bet;
        // Set the score to 0
        score = 0;

        // Init the array list for all possible combinations
        combinations = new ArrayList<>();
    }

    /**
     * Calculate the score
     * @return int score of the dice group for the current bet
     */
    public int calculateScore() {
        // Calculate the ones
        calculateOnes();

        // Iterate through the combinations as of now
        for (Dice d : combinations) {
            // Go through every Die
            for (Die die : d.getDice()) {
                // Check if the die has not been used before
                if (!usedDice.getDie(die)) {
                    // Check if the bet is greater or equal to 1
                    if (bet >= 1) {
                        // Add the value of the die to the score
                        score += die.getDieValue();
                    }else {
                        // Otherwise add one point to the score
                        score++;
                    }

                    // Add the die to the used dice
                    usedDice.addDie(die);
                }
            }
        }

        // Calculate the rest of the combinations, from 2-6
        calculateTwos();
        calculateThrees();
        calculateFours();
        calculateFives();
        calculateSixes();

        // Iterate again through the combinations
        for (Dice d : combinations) {
            // Init temp score int
            int tempScore = 0;

            // Set any dice used to false
            boolean anyDiceUsed = false;

            // Run through each die in the dice group
            for (Die die : d.getDice()) {
                // Check if the die is used and that no dice has been used from the group
                if (usedDice.getDie(die) && !anyDiceUsed)
                    anyDiceUsed = true;
            }

            // Check if any dice has been used
            if(!anyDiceUsed) {
                // Run through each of the die
                for (Die die : d.getDice()) {
                    // Add the die value to the temp score
                    tempScore += die.getDieValue();
                    // Set the current die as used
                    usedDice.addDie(die);
                }

                // Add the temporary score to the actual score
                score += tempScore;
            }
        }

        // Return the score
        return score;
    }

    /**
     * Calculate combinations of only one die
     * I.E. If the bet is set as 6 and if there are
     * any matching dice with the value 6
     */
    private void calculateOnes() {
        // Check if the bet is lower than 1 ("Low")
        if (bet < 1) {
            // Run through each die in the group
            for (Die die : dice.getDice()) {
                // If the die value is lower than 4, i.e. bet "low"
                if (die.getDieValue() < 4) {
                    // Create a new dice combo
                    Dice diceCombo = new Dice();
                    // Add the die to the combo
                    diceCombo.addDie(die);

                    // Add the combo to the list of combinations
                    combinations.add(diceCombo);
                }
            }
        } else {
            // Run through each die in the group
            for (Die die : dice.getDice()) {
                // Check if the die value equals to that of the bet
                if (die.getDieValue() == bet) {
                    // Init a new dice combo
                    Dice diceCombo = new Dice();
                    // Add the die to the combo
                    diceCombo.addDie(die);

                    // Add the combo to the list of combinations
                    combinations.add(diceCombo);
                }
            }
        }
    }

    /**
     * Calculate combinations of two dice
     * I.E. If a bet is set as 6 and if there are
     * any two matching dice that make up 6, (3+3),(4+2)
     */
    private void calculateTwos() {
        // Loop through dice group
        for (int i = 0; i < dice.getDice().size() - 1; i++) {
            // Inner loop for the second die
            for (int x = i+1; x < dice.getDice().size(); x++) {
                // Check if the two current dice match the value of the bet
                if (dice.getDie(i).getDieValue() + dice.getDie(x).getDieValue() == bet) {
                    // Init a new dice combo
                    Dice diceCombo = new Dice();
                    // Add the two dice to the combo
                    diceCombo.addDie(dice.getDie(i));
                    diceCombo.addDie(dice.getDie(x));

                    // Add the combo to the list of all possible combinations
                    combinations.add(diceCombo);
                }
            }
        }
    }

    /**
     * Calculate combinations of three dice
     * I.E. If a bet is set as 6 and if there are
     * any three matching dice that total to 6
     * (1+3+2), (2+2+2), (1+1+4)
     */
    private void calculateThrees() {
        // Functions the same was as calculating two dice, only adding extra inner loop
        for (int i = 0; i < dice.getDice().size() - 2; i++) {
            for (int x = i+1; x < dice.getDice().size() - 1; x++) {
                for (int y = x+1; y < dice.getDice().size(); y++) {
                    if (dice.getDie(i).getDieValue() + dice.getDie(x).getDieValue() +
                            dice.getDie(y).getDieValue() == bet) {
                        Dice diceCombo = new Dice();
                        diceCombo.addDie(dice.getDie(i));
                        diceCombo.addDie(dice.getDie(y));
                        diceCombo.addDie(dice.getDie(x));
                        combinations.add(diceCombo);
                    }
                }
            }
        }
    }

    /**
     * Calculate combinations of four dice
     */
    private void calculateFours() {
        // See function "calculateTwos()"
        for (int i = 0; i < dice.getDice().size() - 3; i++) {
            for (int x = i+1; x < dice.getDice().size() - 2; x++) {
                for (int y = x+1; y < dice.getDice().size() -1; y++) {
                    for (int z = y+1; z < dice.getDice().size(); z++) {
                        if (dice.getDie(i).getDieValue() + dice.getDie(x).getDieValue() +
                                dice.getDie(y).getDieValue() + dice.getDie(z).getDieValue() == bet) {
                            Dice diceCombo = new Dice();
                            diceCombo.addDie(dice.getDie(i));
                            diceCombo.addDie(dice.getDie(x));
                            diceCombo.addDie(dice.getDie(y));
                            diceCombo.addDie(dice.getDie(z));
                            combinations.add(diceCombo);
                        }
                    }
                }
            }
        }
    }

    /**
     * Calculate combination of five dice
     */
    private void calculateFives() {
        // See function "calculateTwos"
        for (int i = 0; i < dice.getDice().size() - 4; i++) {
            for (int x = i+1; x < dice.getDice().size() - 3; x++) {
                for (int y = x+1; y < dice.getDice().size() - 2; y++) {
                    for (int z = y+1; z < dice.getDice().size() - 1; z++) {
                        for (int q = z+1; q < dice.getDice().size(); q++) {
                            if (dice.getDie(i).getDieValue() + dice.getDie(x).getDieValue() +
                                    dice.getDie(y).getDieValue() + dice.getDie(z).getDieValue() +
                                    dice.getDie(q).getDieValue() == bet) {
                                Dice diceCombo = new Dice();
                                diceCombo.addDie(dice.getDie(i));
                                diceCombo.addDie(dice.getDie(i+1));
                                diceCombo.addDie(dice.getDie(i+2));
                                diceCombo.addDie(dice.getDie(i+3));
                                diceCombo.addDie(dice.getDie(i+4));
                                combinations.add(diceCombo);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Calculate combination of six dice
     * Adds the score together of every die in the group
     * and matches it to the bet value
     */
    private void calculateSixes() {
        // Init a temp score
        int tempScore = 0;
        // Init a dice combo
        Dice diceCombo = new Dice();
        // Run through each die in the group
        for (Die die : dice.getDice()) {
            // Add the die to the combo
            diceCombo.addDie(die);
            // Add the die value to the temporary score
            tempScore =+ die.getDieValue();
        }

        // Check if the die value of the six dice match the bet
        if (tempScore == bet) {
            // Add the dice combination to the list of possible combinations
            combinations.add(diceCombo);
        }
    }

}

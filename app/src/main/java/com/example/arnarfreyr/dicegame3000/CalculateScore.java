package com.example.arnarfreyr.dicegame3000;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by arnarfreyr on 20.8.2017.
 */

public class CalculateScore {

    private static String TAG_CALCULATE_SCORE = "CalculateScore";

    private Dice dice;
    private Dice usedDice;
    private ArrayList<Dice> combinations;

    private int bet;
    private int score;

    public CalculateScore(Dice dice, int bet) {
        this.dice = dice;
        usedDice = new Dice();
        this.bet = bet;
        score = 0;
        combinations = new ArrayList<>();
    }

    public int calculateScore() {
        calculateOnes();
        for (Dice d : combinations) {
            for (Die die : d.getDice()) {
                if (!usedDice.getDie(die)) {
                    if (bet >= 1) {
                        score += die.getDieValue();
                        Log.d(TAG_CALCULATE_SCORE, combinations.toString());
                    }else {
                        score++;
                    }
                    usedDice.addDie(die);
                    //Log.d(TAG_CALCULATE_SCORE, die.getDieValue().toString());
                }
            }
        }

        calculateTwos();
        calculateThrees();
        calculateFours();
        calculateFives();
        calculateSixes();

        for (Dice d : combinations) {
            int tempScore = 0;
            boolean anyDiceUsed = false;
            for (Die die : d.getDice()) {
                if (usedDice.getDie(die) && !anyDiceUsed)
                    anyDiceUsed = true;
            }

            if(!anyDiceUsed) {
                for (Die die : d.getDice()) {
                    tempScore += die.getDieValue();
                    usedDice.addDie(die);
                }
                //Log.d(TAG_CALCULATE_SCORE, "tempScore = " + String.valueOf(tempScore));
                score += tempScore;
            }
        }

        return score;
    }

    private void calculateOnes() {
        if (bet < 1) {
            for (Die die : dice.getDice()) {
                if (die.getDieValue() < 4) {
                    Dice diceCombo = new Dice();
                    diceCombo.addDie(die);
                    combinations.add(diceCombo);
                }
            }
        } else {
            for (Die die : dice.getDice()) {
                if (die.getDieValue() == bet) {
                    Dice diceCombo = new Dice();
                    diceCombo.addDie(die);
                    combinations.add(diceCombo);
                }
            }
        }
    }

    private void calculateTwos() {
        for (int i = 0; i < dice.getDice().size() - 1; i++) {
            for (int x = i+1; x < dice.getDice().size(); x++) {
                if (dice.getDie(i).getDieValue() + dice.getDie(x).getDieValue() == bet) {
                    Dice diceCombo = new Dice();
                    diceCombo.addDie(dice.getDie(i));
                    diceCombo.addDie(dice.getDie(x));
                    combinations.add(diceCombo);
                }
            }
        }
    }

    private void calculateThrees() {
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

    private void calculateFours() {
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

    private void calculateFives() {
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

    private void calculateSixes() {
        int tempScore = 0;
        Dice diceCombo = new Dice();
        for (Die die : dice.getDice()) {
            diceCombo.addDie(die);
            tempScore =+ die.getDieValue();
        }

        if (tempScore == bet) {
            combinations.add(diceCombo);
        }
    }

}

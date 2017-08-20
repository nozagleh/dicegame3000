package com.example.arnarfreyr.dicegame3000;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by arnarfreyr on 20.8.2017.
 */

public class CalculateScore {

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
                if (!usedDice.getDie(die))
                    score++;
            }
        }

        calculateTwos();
        calculateThrees();
        calculateFours();
        calculateFives();
        calculateSixes();

        for (Dice d : combinations) {
            int tempScore = 0;
            for (int i = 0; i < d.getDice().size(); i++) {
                if (usedDice.getDie(d.getDie(i))) {
                    continue;
                }
                tempScore += d.getDie(i).getDieValue();
            }
            score += tempScore;
        }

        return score;
    }

    private void calculateOnes() {
        if (bet < 1) {
            for (Die die : dice.getDice()) {
                if (die.getDieValue() < 4) {
                    Dice diceCombo = new Dice();
                    diceCombo.addDie(die);
                    Log.d("DICE VALUE -->", die.getDieValue().toString());
                    combinations.add(diceCombo);
                }
            }
        } else {
            for (Die die : dice.getDice()) {
                if (die.getDieValue() == bet+3) {
                    Dice diceCombo = new Dice();
                    diceCombo.addDie(die);
                    combinations.add(diceCombo);
                }
            }
        }
    }

    private void calculateTwos() {
        for (int i = 0; i < dice.getDice().size() - 1; i++) {
            Log.d("TWOS -->", dice.getDie(i).getDieValue().toString());
            if (dice.getDie(i).getDieValue() + dice.getDie(i+1).getDieValue() == bet+3) {
                Dice diceCombo = new Dice();
                diceCombo.addDie(dice.getDie(i));
                diceCombo.addDie(dice.getDie(i+1));
                combinations.add(diceCombo);
            }
        }
    }

    private void calculateThrees() {
        for (int i = 0; i < dice.getDice().size() - 2; i++) {
            if (dice.getDie(i).getDieValue() + dice.getDie(i+1).getDieValue() +
                    dice.getDie(i).getDieValue() + dice.getDie(i+2).getDieValue()
                    == bet+3) {
                Dice diceCombo = new Dice();
                diceCombo.addDie(dice.getDie(i));
                diceCombo.addDie(dice.getDie(i+1));
                diceCombo.addDie(dice.getDie(i+2));
                combinations.add(diceCombo);
            }
        }
    }

    public void calculateFours() {
        for (int i = 0; i < dice.getDice().size() - 3; i++) {
            if (dice.getDie(i).getDieValue() + dice.getDie(i+1).getDieValue() +
                    dice.getDie(i).getDieValue() + dice.getDie(i+2).getDieValue() +
                    dice.getDie(i).getDieValue() + dice.getDie(i+3).getDieValue()
                    == bet+3) {
                Dice diceCombo = new Dice();
                diceCombo.addDie(dice.getDie(i));
                diceCombo.addDie(dice.getDie(i+1));
                diceCombo.addDie(dice.getDie(i+2));
                diceCombo.addDie(dice.getDie(i+3));
                combinations.add(diceCombo);
            }
        }
    }

    private void calculateFives() {
        for (int i = 0; i < dice.getDice().size() - 4; i++) {
            if (dice.getDie(i).getDieValue() + dice.getDie(i+1).getDieValue() +
                    dice.getDie(i).getDieValue() + dice.getDie(i+2).getDieValue() +
                    dice.getDie(i).getDieValue() + dice.getDie(i+3).getDieValue() +
                    dice.getDie(i).getDieValue() + dice.getDie(i+4).getDieValue()
                    == bet+3) {
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

    private void calculateSixes() {
        for (int i = 0; i < dice.getDice().size() - 5; i++) {
            if (dice.getDie(i).getDieValue() + dice.getDie(i+1).getDieValue() +
                    dice.getDie(i).getDieValue() + dice.getDie(i+2).getDieValue() +
                    dice.getDie(i).getDieValue() + dice.getDie(i+3).getDieValue() +
                    dice.getDie(i).getDieValue() + dice.getDie(i+4).getDieValue() +
                    dice.getDie(i).getDieValue() + dice.getDie(i+5).getDieValue()
                    == bet+3) {
                Dice diceCombo = new Dice();
                diceCombo.addDie(dice.getDie(i));
                diceCombo.addDie(dice.getDie(i+1));
                diceCombo.addDie(dice.getDie(i+2));
                diceCombo.addDie(dice.getDie(i+3));
                diceCombo.addDie(dice.getDie(i+4));
                diceCombo.addDie(dice.getDie(i+5));
                combinations.add(diceCombo);
            }
        }
    }

}

package com.example.arnarfreyr.dicegame3000;

import java.util.ArrayList;

/**
 * Created by arnarfreyr on 8.8.2017.
 */

public interface FragmentListener {
    void onRollClick();
    void onDieChosen(Integer imgNr);
    void scoreFragment();
    void onBetChange();
    void onBetSelected(int betNr);
    ArrayList<Integer> getScores();
    GamePlay getGame();
    Boolean registerScore(String playerName);
    void closeActivity();
    String[] getBets();
}

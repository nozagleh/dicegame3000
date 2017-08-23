package com.example.arnarfreyr.dicegame3000;

import java.util.ArrayList;

/**
 * Fragment listener for the fragments in the Game activity
 *
 * Created by arnarfreyr on 8.8.2017.
 */

public interface FragmentListener {
    // Void functions
    void onRollClick();
    void onDieChosen(Integer imgNr);
    void scoreFragment();
    void onBetChange();
    void onBetSelected(int betNr);
    void closeActivity();
    void setTextHidden(Boolean hidden);

    // Returning functions
    GamePlay getGame();
    Boolean registerScore(String playerName);
    String[] getBets();
    Boolean getTextHidden();

}


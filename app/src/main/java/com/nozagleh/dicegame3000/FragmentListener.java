package com.nozagleh.dicegame3000;

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


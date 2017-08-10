package com.example.arnarfreyr.dicegame3000;

/**
 * Created by arnarfreyr on 8.8.2017.
 */

public interface FragmentListener {
    void onRollClick();
    void onDieChosen(Integer imgNr);
    void scoreFragment();
    void onBetChange();
    Boolean onBetSelected(int betNr);
}

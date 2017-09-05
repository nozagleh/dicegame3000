package com.nozagleh.dicegame3000;

import java.util.ArrayList;

/**
 * Fragment listener for the overlay fragment.
 * The overlay fragment shows the round score
 * to the user after each round is finished
 *
 * Created by arnarfreyr on 20.8.2017.
 */

public interface OverlayFragmentListener {
    // Get variables needed for the overlay fragment
    ArrayList<String> getOverlayVariables();

    // Run on close of fragment
    void onClickClose();
}
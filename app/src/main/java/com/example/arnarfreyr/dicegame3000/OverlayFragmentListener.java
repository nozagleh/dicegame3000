package com.example.arnarfreyr.dicegame3000;

import java.util.ArrayList;

/**
 * Created by arnarfreyr on 20.8.2017.
 */

public interface OverlayFragmentListener {
    ArrayList<String> getOverlayVariables();
    void onClickClose();
}
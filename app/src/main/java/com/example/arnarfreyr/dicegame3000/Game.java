package com.example.arnarfreyr.dicegame3000;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class Game extends AppCompatActivity
        implements FragmentListener {

    GamePlay game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Only init fragments if the fragment container is present in activity
        if(findViewById(R.id.fragment_container) != null) {
            // If saved state has data, return
            if(savedInstanceState != null) {
                return;
            }

            // Init the play fragment
            Play fmPlay = new Play();
            // Add the play fragment to the fragment manager
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fmPlay).commit();
        }

        startGame();
    }

    /**
     * Callback from play fragment when the roll button is pressed.
     */
    @Override
    public void onRollClick() {
        Play playFrag = (Play)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(game.isLastRound()) {
            //TODO do stuff when is last round
            if (playFrag != null) {
                playFrag.updateButtonText(R.string.btn_highscore);
            }
        } else {
            game.roll();

            //TODO send the values to the fragment for display

            if (playFrag != null) {
                playFrag.updateImages(game.getDice());
            }
        }
    }

    @Override
    public void onDieChosen(Integer dieNr) {
        game.setDieChosen(dieNr);

        Play playFrag = (Play)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (playFrag != null) {
            playFrag.updateImages(game.getDice());
        }
    }

    public void startGame() {
        Dice dice = new Dice();
        dice.fill();
        game = new GamePlay(dice);
        game.startGame();
    }

    @Override
    public void scoreFragment() {

        Score newScoreFrag = new Score();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newScoreFrag);
        transaction.addToBackStack(null);

        transaction.commit();

    }
}

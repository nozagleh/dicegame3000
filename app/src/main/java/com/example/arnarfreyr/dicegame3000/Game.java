package com.example.arnarfreyr.dicegame3000;

import android.app.DialogFragment;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class Game extends FragmentActivity
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
        Log.d("Is last roll? -->", game.isNextToLastRoll().toString());
        Log.d("Is bet done? -->", game.betAlreadyDone().toString());
        if (game.isNextToLastRoll() && game.betAlreadyDone()) {
            Log.d("TOAST -->", "YES");
            Toast betToast = Toast.makeText(this,R.string.dialog_bet,Toast.LENGTH_SHORT);
            betToast.show();
            return;
        }
        Play playFrag = (Play)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if(game.isNextToLastRoll()) {
            if (playFrag != null) {
                playFrag.updateButtonText(R.string.txt_next_round, false);
            }
        }

        if(game.isLastRound()) {
            //TODO do stuff when is last round
            if (playFrag != null) {
                playFrag.updateButtonText(R.string.btn_highscore, true);
            }
        } else {
            game.roll();
            if(game.isLastRoll()) {
                Integer score = game.getRoundScore();
                playFrag.displayRoundScore(score);
            }

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

    /**
     * Start a new game
     */
    public void startGame() {
        // Init dice for the game
        Dice dice = new Dice();
        dice.fill();

        // Init a new instance of the game
        game = new GamePlay(dice);
        // Start the game
        game.startGame();
    }

    /**
     * Call for changing to the score fragment.
     * There the final score of the player is displayed and the user is prompted for a name
     */
    @Override
    public void scoreFragment() {
        // Init a new score fragment
        Score newScoreFrag = new Score();

        // Start a new fragment transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace the current fragment in the fragment container
        transaction.replace(R.id.fragment_container, newScoreFrag);

        // Add the old fragment to the back stack, allowing for a back button press
        transaction.addToBackStack(null);

        // Commit the fragment changes
        transaction.commit();
    }


    @Override
    public void onBetChange() {
        DialogFragment betDialog = new BetDialog();
        betDialog.show(getFragmentManager(),"fragment_bet_dialog");
    }

    @Override
    public Boolean onBetSelected(int betNr) {
        if(!game.isBetDone(betNr)) {
            game.setBetType(betNr);
            return false;
        }

        return true;
    }
}

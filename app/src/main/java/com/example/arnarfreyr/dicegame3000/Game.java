package com.example.arnarfreyr.dicegame3000;

import android.app.DialogFragment;
import android.content.Context;
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

    Boolean setToLast;

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
            final Play fmPlay = new Play();
            // Add the play fragment to the fragment manager
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fmPlay, "FRAG_PLAY").commit();

            getSupportFragmentManager().addOnBackStackChangedListener(
                    new FragmentManager.OnBackStackChangedListener() {
                        @Override
                        public void onBackStackChanged() {
                            if (getSupportFragmentManager().findFragmentByTag("FRAG_PLAY") instanceof Play) {
                                Log.d("PLAY", "YES");
                            }
                        }
                    }
            );
        }

        setToLast = false;

        // Init dice for the game
        Dice dice = new Dice();
        dice.fill();

        // Init a new instance of the game
        game = new GamePlay(dice);
    }

    /**
     * Callback from play fragment when the roll button is pressed.
     */
    @Override
    public void onRollClick() {
        if (!game.getIsStarted())
                startGame();
        Log.d("ENDED? -->", game.hasGameEnded().toString());
        if (game.hasGameEnded())
            scoreFragment();

        if (game.isRollReset() && game.betAlreadyDone()) {
            showBetToast();
            return;
        }

        Play playFrag = (Play)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            game.roll();

            if(game.endOfRound()) {
                showScores(playFrag);
                playFrag.updateButtonText(R.string.txt_next_round);
                playFrag.lockDice();
            } else {
                playFrag.updateButtonText(R.string.btn_roll);
                playFrag.unlockDice();
            }

            if(game.endOfRound() && game.isLastRound()) {
                playFrag.updateButtonText(R.string.txt_score);
                scoreFragment();
            }

            playFrag.displayRoll(game.getRollNr());
            playFrag.updateImages(game.getDice());
    }

    @Override
    public void onDieChosen(Integer dieNr) {
        if (!game.getIsStarted())
            return;

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
    public void onBetSelected(int betNr) {
        game.setBetType(betNr);
    }

    @Override
    public ArrayList<Integer> getScores() {
        ArrayList<Integer> scores = new ArrayList<>();
        scores.add(game.getRollNr());
        scores.add(game.getRoundNr());
        scores.add(game.getScore());

        return scores;
    }

    private void showBetToast() {
        String betText = "";
        if(game.getBetType() == 0) {
            betText = "Low";
        }else {
            Integer bet = game.getBetType() + 3;
            betText = bet.toString();
        }

        String message = getString(R.string.dialog_bet_text, betText);
        Toast betToast = Toast.makeText(this, message ,Toast.LENGTH_SHORT);
        betToast.show();
    }

    private void showScores(Play playFrag) {
        Log.d("SCORES -->", "YES" );
        Integer score = game.getRoundScore();
        playFrag.displayRound(game.getRoundNr());
        playFrag.displayRoundScore(score);
    }
}

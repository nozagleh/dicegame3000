package com.example.arnarfreyr.dicegame3000;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Game extends FragmentActivity
        implements FragmentListener {

    GamePlay game;

    Boolean setToLast;

    Boolean sharedPrefOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Only init fragments if the fragment container is present in activity
        if(findViewById(R.id.fragment_container) != null) {

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

            // Init dice for the game
            Dice dice = new Dice();

            // Init a new instance of the game
            game = new GamePlay();


            SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
            if (!sharedPreferences.getBoolean(getString(R.string.preference_set), false)) {
                dice.fill();
                game = new GamePlay(dice);
            }
        }

        setToLast = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        game.startGame();

        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean(getString(R.string.preference_set), false)) {

            // Set shared preferences
            sharedPrefOn = true;

            // If saved state has data, restore it
            Integer rollNr = sharedPreferences.getInt(getString(R.string.preference_roll_nr), -1);
            game.setRollNr(rollNr);
            Integer roundNr = sharedPreferences.getInt(getString(R.string.preference_round_nr), -1);
            game.setRoundNr(roundNr);

            String tempDiceRolls = sharedPreferences.getString(getString(R.string.preference_rolls), "-1");
            String[] diceRolls = tempDiceRolls.split(",");
            Log.d("DICE ROLLS -->", tempDiceRolls);
            game.getAllDice().clear();
            Dice dice = new Dice();
            for (int i = 0; i < diceRolls.length; i++) {
                Die die = new Die();
                die.setDieValue(Integer.valueOf(diceRolls[i]));

                dice.addDie(die);

                if (((i+1) % 6) == 0) {
                    Log.d("MOD 6 -->", "true");
                    game.addDice(dice);
                    dice = new Dice();
                }
            }

            String tempScores = sharedPreferences.getString(getString(R.string.preference_scores), "-1");
            String[] scores = tempScores.split(",");
            game.getTotalScore().clear();
            for (int i = 0; i < scores.length; i++) {
                game.setRoundScore(Integer.valueOf(scores[i]));
            }

            String tempCurrentDice = sharedPreferences.getString(getString(R.string.preference_current_dice), "-1");
            String[] currentDice = tempCurrentDice.split(",");

            String tempChosenDice = sharedPreferences.getString(getString(R.string.preference_chosen_dice), "-1");
            String[] chosenDice = tempChosenDice.split(",");
            dice = new Dice();
            for (int i = 0; i < currentDice.length; i++) {
                Die die = new Die();
                die.setDieValue(Integer.valueOf(currentDice[i]));
                if (Integer.valueOf(chosenDice[i]) == 1)
                    die.setIfChosen(true);

                dice.addDie(die);
            }
            game.setDice(dice);

            String tempBets = sharedPreferences.getString(getString(R.string.preference_done_bets), "-1");
            String[] chosenBets = tempBets.split(",");
            game.getBetsDone().clear();
            for (int i = 0; i < chosenBets.length; i++) {
                game.addBet(Integer.valueOf(chosenBets[i]));
            }

            if (sharedPreferences.getBoolean(getString(R.string.preference_game_ended), false)) {
                scoreFragment();
            }else {
                Play playFrag = (Play)
                        getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                playFrag.displayRoll(game.getRollNr());
                playFrag.updateImages(game.getDice());
                showScores(playFrag);
            }
        }
    }

    /**
     * Save data if the activity is paused.
     */
    @Override
    public void onPause() {
        super.onPause();

        // Open up a shared preferences
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(getString(R.string.preference_set), true);

        // Put roll and round numbers
        editor.putInt(getString(R.string.preference_roll_nr), game.getRollNr());
        editor.putInt(getString(R.string.preference_round_nr), game.getRoundNr());

        // Put values of all previous dice
        String diceString = "";
        Log.d("BEFORE -->", game.getAllDice().toString());
        for (int i = 0; i < game.getAllDice().size(); i++) {
            Log.d("SIZE? -->", String.valueOf(game.getAllDice().get(i).getDice().size()));
            for(int x = 0; x < game.getAllDice().get(i).getDice().size(); x++) {
                Log.d("DIE LIST -->", game.getAllDice().get(i).getDice().get(x).getDieValue().toString());
                diceString += game.getAllDice().get(i).getDice().get(x).getDieValue() + ",";
            }
        }
        Log.d("DICE STRING -->", diceString);

        editor.putString(getString(R.string.preference_rolls), diceString);

        // Put total round scores
        String scoresString = "";
        for (Integer score : game.getTotalScore()) {
            scoresString += score.toString() + ",";
        }
        Log.d("SCORE STRING -->", scoresString);

        editor.putString(getString(R.string.preference_scores), scoresString);

        // Put current dice values
        String currentDice = "";
        Dice d = game.getDice();
        for (Die die : d.getDice()) {
            Log.d("DIE CURRENT -->", die.getDieValue().toString());
            currentDice += die.getDieValue().toString() + ",";
        }
        Log.d("CURRENT DICE -->", currentDice);

        editor.putString(getString(R.string.preference_current_dice), currentDice);

        // Put values for current chosen dice if they are chosen or not
        String chosenString = "";
        for (Die die : game.getDice().getDice()) {
            if (die.getIfChosen()) {
                chosenString += "1";
            }else {
                chosenString += "0";
            }
            chosenString += ",";
        }
        Log.d("CHOSEN DICE -->", chosenString);

        editor.putString(getString(R.string.preference_chosen_dice), chosenString);

        String betsString = "";
        for (int i = 0; i < game.getBetsDone().size(); i++) {
            betsString += game.getBetsDone().get(i) + ",";
        }

        editor.putString(getString(R.string.preference_done_bets), betsString);

        if (game.hasGameEnded()) {
            editor.putBoolean(getString(R.string.preference_game_ended), true);
        }

        editor.apply();
    }

    @Override
    public void onBackPressed() {
        Score scoreFrag = (Score)getSupportFragmentManager().findFragmentByTag(getString(R.string.frag_score));
        if ( scoreFrag != null && scoreFrag.isVisible() ) {
            Toast noBackToast = Toast.makeText(this, getString(R.string.txt_game_finished), Toast.LENGTH_SHORT);
            noBackToast.show();
        }else {
            super.onBackPressed();
        }
    }

    /**
     * Callback from play fragment when the roll button is pressed.
     */
    @Override
    public void onRollClick() {
        if (!game.getIsStarted())
                startGame();

        if (game.hasGameEnded())
            scoreFragment();

        Log.d("Roll reset? -->", game.isRollReset().toString());
        Log.d("Bet done? -->", game.betAlreadyDone().toString());

        Play playFrag = (Play)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            game.roll();

            if (game.isRollReset() && game.betAlreadyDone()) {
                showBetToast();
                return;
            }

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
        transaction.replace(R.id.fragment_container, newScoreFrag, getString(R.string.frag_score));

        // Add the old fragment to the back stack, allowing for a back button press
        transaction.addToBackStack(null);

        // Commit the fragment changes
        transaction.commit();

        game.endGame();
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

    @Override
    public GamePlay getGame() {
        return this.game;
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
        playFrag.displayRound(game.getRoundNr()+1);
        playFrag.displayRoundScore(score);
    }

    public void saveScore(String playerName) {

    }
}

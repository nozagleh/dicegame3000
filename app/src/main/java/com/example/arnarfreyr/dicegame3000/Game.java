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
        implements FragmentListener, OverlayFragmentListener {

    private static final String TAG_PLAY_FRAG = "PLAY_FRAG";
    private static final String TAG_SCORE_FRAG = "SCORE_FRAG";
    private static final String TAG_POPUP_FRAG = "POP_FRAG";

    private Play playFrag;
    private Score scoreFrag;
    private OverlayRound overlayFrag;

    GamePlay game;

    Boolean setToLast;
    Boolean scoreDiagOn;

    Boolean sharedPrefOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Only init fragments if the fragment container is present in activity
        if(findViewById(R.id.fragment_container) != null) {

            playFrag = (Play) getSupportFragmentManager().findFragmentByTag(TAG_PLAY_FRAG);

            if (playFrag == null) {
                // Init the play fragment
                playFrag = new Play();
                // Add the play fragment to the fragment manager
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, playFrag, TAG_PLAY_FRAG).commit();

                getSupportFragmentManager().addOnBackStackChangedListener(
                        new FragmentManager.OnBackStackChangedListener() {
                            @Override
                            public void onBackStackChanged() {
                                if (getSupportFragmentManager().findFragmentByTag(TAG_PLAY_FRAG) instanceof Play) {
                                    Log.d("PLAY", "YES");
                                }
                            }
                        }
                );
            }

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

        sharedPrefOn = false;
        scoreDiagOn = false;

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
            Dice dice = new Dice();
            if (!tempDiceRolls.isEmpty()) {
                String[] diceRolls = tempDiceRolls.split(",");
                game.getAllDice().clear();

                for (int i = 0; i < diceRolls.length; i++) {
                    Die die = new Die();
                    die.setDieValue(Integer.valueOf(diceRolls[i]));

                    dice.addDie(die);

                    if (((i+1) % 6) == 0) {
                        game.addDice(dice);
                        dice = new Dice();
                    }
                }
            }


            String tempScores = sharedPreferences.getString(getString(R.string.preference_scores), "-1");
            if (!tempScores.isEmpty()) {
                String[] scores = tempScores.split(",");
                game.getTotalScore().clear();
                for (int i = 0; i < scores.length; i++) {
                    game.setRoundScore(Integer.valueOf(scores[i]));
                }
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
            if (!tempBets.isEmpty()) {
                String[] chosenBets = tempBets.split(",");
                game.getBetsDone().clear();
                for (int i = 0; i < chosenBets.length; i++) {
                    game.addBet(Integer.valueOf(chosenBets[i]));
                }
            }

            if (sharedPreferences.getBoolean(getString(R.string.preference_game_ended), false)) {
                scoreFragment();
            }else {
                playFrag.displayRoll(game.getRollNr());
                playFrag.updateImages(game.getDice());

                if (!tempBets.isEmpty())
                    showScores(playFrag);
            }
        }

        Log.d("Game -->", "Load preferences");
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

        editor.putBoolean(getString(R.string.preference_game_ended), game.hasGameEnded());

        if (sharedPrefOn) {

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
        }

        Log.d("Game -->", "save preferences");

        editor.apply();
    }

    @Override
    public void onBackPressed() {
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
        sharedPrefOn = true;
        if (!game.getIsStarted())
                startGame();

        if (game.hasGameEnded())
            scoreFragment();

        Log.d("Roll reset? -->", game.isRollReset().toString());
        Log.d("Bet done? -->", game.betAlreadyDone().toString());

        Play playFrag = (Play)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (!scoreDiagOn)
            game.roll();
        else
            scoreDiagOn = false;

        if (game.isRollReset() && game.betAlreadyDone()) {
            showBetToast();
            return;
        }

        if(game.endOfRound()) {
            showScores(playFrag);
            playFrag.lockDice();

            popupFragment();

        } else if (game.isLastRoll()) {
            playFrag.updateButtonText(R.string.txt_end_round);
        } else {
            playFrag.updateButtonText(R.string.btn_roll);
            playFrag.unlockDice();
        }

        if(game.endOfRound() && game.isLastRound()) {
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
        sharedPrefOn = false;
        // Init a new score fragment
        scoreFrag = new Score();

        // Start a new fragment transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace the current fragment in the fragment container
        transaction.replace(R.id.fragment_container, scoreFrag, TAG_SCORE_FRAG);

        // Add the old fragment to the back stack, allowing for a back button press
        transaction.remove(playFrag);

        // Commit the fragment changes
        transaction.commit();

        setTextHidden(true);

        game.endGame();
    }

    public void popupFragment() {

        if (findViewById(R.id.overlay_fragment) != null) {
            overlayFrag = (OverlayRound) getSupportFragmentManager().findFragmentByTag(TAG_POPUP_FRAG);

            if (overlayFrag == null)
                overlayFrag = new OverlayRound();

            getSupportFragmentManager().beginTransaction()
                    .setTransition(R.anim.frag_slide_in)
                    .add(R.id.overlay_fragment, overlayFrag, TAG_POPUP_FRAG)
                    .addToBackStack(null)
                    .commit();
            scoreDiagOn = true;
        }
    }


    @Override
    public void onBetChange() {
        DialogFragment betDialog = new BetDialog();
        betDialog.show(getFragmentManager(),"fragment_bet_dialog");
    }

    @Override
    public void onBetSelected(int betNr) {
        String[] betsAvailable = getBets();
        Integer bet;
        if (betsAvailable[betNr].equals("Low"))
            bet = 0;
        else
            bet = Integer.valueOf(betsAvailable[betNr]);

        game.setBetType(bet);

        String betText;

        if (bet == 0) {
            betText = String.format(getString(R.string.txt_bet_chosen_string), getString(R.string.txt_bet_low));
        } else {
            betText = String.format(getString(R.string.txt_bet_chosen), bet);
        }

        playFrag.updateBetText(betText);

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
            Integer bet = game.getBetType();
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

    @Override
    public Boolean registerScore(String playerName) {
        UserData user = new UserData(playerName, game.getFinalScore());

        SQLManager sql = new SQLManager(this);

        return sql.insertUser(user);
    }

    @Override
    public void closeActivity() {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        this.finish();
    }

    @Override
    public String[] getBets() {
        String[] bets = getResources().getStringArray(R.array.bet_array);
        ArrayList<String> listBets = new ArrayList<>();

        for (String bet: bets) {
            if (bet.equals("Low"))
                bet = "0";
            if (!game.getBetsDone().contains(Integer.valueOf(bet))) {
                if (bet.equals("0"))
                    listBets.add("Low");
                else
                    listBets.add(bet);
            }
        }

        return listBets.toArray(new String[listBets.size()]);
    }

    @Override
    public void onClickClose() {
        Log.d("Game", "clicked close popup frag");

        if (findViewById(R.id.overlay_fragment) != null) {
            overlayFrag = (OverlayRound) getSupportFragmentManager().findFragmentByTag(TAG_POPUP_FRAG);

            if (overlayFrag == null)
                overlayFrag = new OverlayRound();

            getSupportFragmentManager().beginTransaction()
                    .remove(overlayFrag)
                    .addToBackStack(TAG_POPUP_FRAG)
                    .commit();

            game.roll();

            if (playFrag != null)
                playFrag.updateButtonText(R.string.btn_roll);
        }
        
    }

    @Override
    public ArrayList<String> getOverlayVariables() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(game.getRoundNr().toString());
        strings.add(game.getRoundScore().toString());
        strings.add(game.getFinalScore().toString());

        return strings;
    }

    @Override
    public Boolean getTextHidden() {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(getString(R.string.preferences_text_hidden), false);
    }

    @Override
    public void setTextHidden(Boolean hidden) {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.preferences_text_hidden), hidden);
        editor.apply();
    }
}

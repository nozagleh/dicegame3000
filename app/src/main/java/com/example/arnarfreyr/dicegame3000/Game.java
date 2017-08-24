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

    // Fragment tags
    private static final String TAG_PLAY_FRAG = "PLAY_FRAG";
    private static final String TAG_SCORE_FRAG = "SCORE_FRAG";
    private static final String TAG_POPUP_FRAG = "POP_FRAG";

    // Fragments
    private Play playFrag;
    private Score scoreFrag;
    private OverlayRound overlayFrag;

    // Init GamePlay object
    GamePlay game;

    // Init rule checking booleans used throughout the game
    Boolean setToLast;
    Boolean scoreDiagOn;
    Boolean sharedPrefOn;

    // Init an SQL manager
    SQLManager sql;

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
            }

            // Init dice for the game
            Dice dice = new Dice();

            // Init a new instance of the game
            game = new GamePlay();


            // Get the shared preferences
            SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
            // Check if the preferences should be used
            if (!sharedPreferences.getBoolean(getString(R.string.preference_set), false)) {
                // If not, fill the dice group
                dice.fill();
                // Init a new game with said dice group
                game = new GamePlay(dice);
            }
        }

        // Set shared preferences to true
        sharedPrefOn = false;

        // Set score dialog on to false
        scoreDiagOn = false;
        // Set last round to false
        setToLast = false;
    }

    /**
     * Override activities super function onResume
     */
    @Override
    public void onResume() {
        super.onResume();

        // Get the shared preferences
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        // Check if the shared preferences should be used
        if (sharedPreferences.getBoolean(getString(R.string.preference_set), false)) {

            // Set shared preferences
            sharedPrefOn = true;

            // Get roll number from SP(Shared Preferences)
            Integer rollNr = sharedPreferences.getInt(getString(R.string.preference_roll_nr), -1);
            game.setRollNr(rollNr);
            // Get round number from SP
            Integer roundNr = sharedPreferences.getInt(getString(R.string.preference_round_nr), -1);
            game.setRoundNr(roundNr);

            // Get dice rolls from SP
            String tempDiceRolls = sharedPreferences.getString(getString(R.string.preference_rolls), "-1");
            // Init an empty Die group
            Dice dice = new Dice();
            // Check if the dice are empty
            if (!tempDiceRolls.isEmpty()) {
                // Split the rolls string
                String[] diceRolls = tempDiceRolls.split(",");
                // Get all dice and clear
                game.getAllDice().clear();

                // Run through each die
                for (int i = 0; i < diceRolls.length; i++) {
                    Die die = new Die();
                    die.setDieValue(Integer.valueOf(diceRolls[i]));

                    // Add the die to dice group
                    dice.addDie(die);

                    // Check when the die number is mod(6), and init a new Dice group
                    if (((i+1) % 6) == 0) {
                        game.addDice(dice);
                        dice = new Dice();
                    }
                }
            }

            // Get the scores from SP
            String tempScores = sharedPreferences.getString(getString(R.string.preference_scores), "-1");
            // Check if the scores are empty
            if (!tempScores.isEmpty()) {
                // Split the score string
                String[] scores = tempScores.split(",");
                // Clear the scores
                game.getTotalScore().clear();
                // Run through each score and add the round score
                for (int i = 0; i < scores.length; i++) {
                    game.setRoundScore(Integer.valueOf(scores[i]));
                }
            }

            // Get current dice from SP, split the string
            String tempCurrentDice = sharedPreferences.getString(getString(R.string.preference_current_dice), "-1");
            String[] currentDice = tempCurrentDice.split(",");

            // Get the chosen current dice from SP, split the string
            String tempChosenDice = sharedPreferences.getString(getString(R.string.preference_chosen_dice), "-1");
            String[] chosenDice = tempChosenDice.split(",");

            // Init a clean Dice group
            dice = new Dice();
            // Run through the Dice group
            for (int i = 0; i < currentDice.length; i++) {
                Die die = new Die();
                // Set the die value
                die.setDieValue(Integer.valueOf(currentDice[i]));
                // Set if the die is chosen or not
                if (Integer.valueOf(chosenDice[i]) == 1)
                    die.setIfChosen(true);

                // Add the Die to Dice
                dice.addDie(die);
            }

            // Set the game dice group
            game.setDice(dice);

            int currentBet = sharedPreferences.getInt(getString(R.string.preference_current_bet), -1);
            if (currentBet != -1)
                game.setBetType(currentBet);

            // Get done bets from SP
            String tempBets = sharedPreferences.getString(getString(R.string.preference_done_bets), "-1");
            // Check if the bets string is empty
            if (!tempBets.isEmpty()) {
                // Split the string
                String[] chosenBets = tempBets.split(",");
                // Clear any remaining bets, if any
                game.getBetsDone().clear();

                // Run through the bets and add them
                for (int i = 0; i < chosenBets.length; i++) {
                    game.addBet(Integer.valueOf(chosenBets[i]));
                }
            }

            // Check if the game is actually already finished
            if (sharedPreferences.getBoolean(getString(R.string.preference_game_ended), false)) {
                // Show the score fragment
                scoreFragment();
            }else {
                if (playFrag != null) {
                    // Update the rolls and images in the play fragment
                    playFrag.displayRoll(game.getRollNr());
                    playFrag.updateImages(game.getDice());
                    playFrag.updateBetText(String.format(getString(R.string.txt_bet_chosen), game.getBetType()));
                }

                // Update the scores text if the done bets are not empty
                if (!tempBets.isEmpty())
                    showScores();
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

        // Put boolean if the game has actually ended
        editor.putBoolean(getString(R.string.preference_game_ended), game.hasGameEnded());

        // Check if the shared preferences is on
        if (sharedPrefOn) {

            // Add to shared preferences that the preferences are on
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
            // Add the rolls
            editor.putString(getString(R.string.preference_rolls), diceString);

            // Put total round scores
            String scoresString = "";
            for (Integer score : game.getTotalScore()) {
                scoresString += score.toString() + ",";
            }
            // Add the scores
            editor.putString(getString(R.string.preference_scores), scoresString);

            // Put current dice values
            String currentDice = "";
            Dice d = game.getDice();
            for (Die die : d.getDice()) {
                currentDice += die.getDieValue().toString() + ",";
            }
            // Add the current dice
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
            // Add current chosen dice
            editor.putString(getString(R.string.preference_chosen_dice), chosenString);

            // Create a bet string of done bets
            String betsString = "";
            for (int i = 0; i < game.getBetsDone().size(); i++) {
                betsString += game.getBetsDone().get(i) + ",";
            }
            // Add done bets
            editor.putString(getString(R.string.preference_done_bets), betsString);
        }

        // Apply the preferences
        editor.apply();
    }

    /**
     * Set on back press for final fragment, where the user enters the score into the DB
     */
    @Override
    public void onBackPressed() {
        // Check if the score fragment is visible and not null
        if ( scoreFrag != null && scoreFrag.isVisible() ) {
            // Create a toast that sends a notice to the user that they need to enter the name
            Toast noBackToast = Toast.makeText(this, getString(R.string.txt_game_finished), Toast.LENGTH_SHORT);
            // Show the toast
            noBackToast.show();
        } else if( overlayFrag != null && overlayFrag.isVisible() ) {
            // Close the overlay fragment if the user clicked the back button
            onClickClose();
        } else {
            // Call super on back pressed
            super.onBackPressed();
        }
    }

    /**
     * Callback from play fragment when the roll button is pressed.
     */
    @Override
    public void onRollClick() {
        // Set shared preferences on, on roll click
        sharedPrefOn = true;

        // Check if the game has been started, if not, start one
        if (!game.getIsStarted())
                startGame();

        // Check if the game has actually ended, if so, take the user to the final fragment
        if (game.hasGameEnded())
            scoreFragment();

        // Check if the score dialog is on, do not roll if the score dialog is shown
        if (!scoreDiagOn)
            game.roll();
        else
            scoreDiagOn = false;

        // Check if the rolls have been reset, and the if the chosen bet is done
        if (game.isRollReset() && game.betAlreadyDone()) {
            // Show the chosen bet toast error message
            showBetToast();
            return;
        }

        // Check if it is the end of the round
        if(game.endOfRound()) {
            // Update the scores of the player
            showScores();

            // Lock the dice for changes
            if (playFrag != null)
                playFrag.lockDice();

            // Show the overlay "popup" fragment
            popupFragment();

        } else if (game.isLastRoll()) {
            // If it is the last roll, set the text to finish
            playFrag.updateButtonText(R.string.txt_end_round);
        } else {
            // Otherwise set the text to "roll" and unlock the dice
            playFrag.updateButtonText(R.string.btn_roll);
            playFrag.unlockDice();
        }

        // Check if it is the end of the current round, as well as if it is the final round
        if(game.endOfRound() && game.isLastRound()) {
            // Show the score fragment
            scoreFragment();
        }

        // Display the roll number
        playFrag.displayRoll(game.getRollNr());
        // Update the images
        playFrag.updateImages(game.getDice());
    }

    /**
     * Run on die chosen, when a die is clicked on by the user
     * @param dieNr The number of the die that the user clicked on
     */
    @Override
    public void onDieChosen(Integer dieNr) {
        // Checck if the game has been started, if not, return
        Log.d("Game", game.getIsStarted().toString());
        if (!game.getIsStarted())
            return;
        // Set the die chosen
        game.setDieChosen(dieNr);

        // Check if the play fragment exists, and update the die image accordingly
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

        // Set the text to hidden
        setTextHidden(true);

        // End the game
        game.endGame();
    }

    /**
     * Show a popup fragment with the users round score
     */
    public void popupFragment() {
        // Check if the overlay fragment container exists
        if (findViewById(R.id.overlay_fragment) != null) {
            // Set the overlay fragment from the fragment manager
            overlayFrag = (OverlayRound) getSupportFragmentManager().findFragmentByTag(TAG_POPUP_FRAG);

            // Check if the overlay fragment is still null, then init a new one
            if (overlayFrag == null)
                overlayFrag = new OverlayRound();

            // Show the overlay fragment, adding the play fragment to the backstack
            getSupportFragmentManager().beginTransaction()
                    .setTransition(R.anim.frag_slide_in)
                    .add(R.id.overlay_fragment, overlayFrag, TAG_POPUP_FRAG)
                    .commit();
            // Set overlay fragment score dialog boolean to true
            scoreDiagOn = true;
        }
    }

    /**
     * On bet change, when the user wants to change the bet
     */
    @Override
    public void onBetChange() {
        // Init a new dialog fragment
        DialogFragment betDialog = new BetDialog();

        // Show the dialog fragment
        betDialog.show(getFragmentManager(),"fragment_bet_dialog");
    }

    /**
     * On bet selected function, used for fragment
     * @param betNr bet nr chosen by the user
     */
    @Override
    public void onBetSelected(int betNr) {
        // Get bets that are available
        String[] betsAvailable = getBets();

        // Init an empty Integer
        Integer bet;

        // Check if the bet available and chosen equal to "Low", then set it as 0
        if (betsAvailable[betNr].equals("Low"))
            bet = 0;
        else
            bet = Integer.valueOf(betsAvailable[betNr]);

        // Set the bet type of the game
        game.setBetType(bet);

        // Init bet text
        String betText;

        // Check if the bet text is 0
        if (bet == 0) {
            // Create the bet text
            betText = String.format(getString(R.string.txt_bet_chosen_string), getString(R.string.txt_bet_low));
        } else {
            // Create the bet text
            betText = String.format(getString(R.string.txt_bet_chosen), bet);
        }

        // Check if the play frag exists, and update the bet text
        if (playFrag != null)
            playFrag.updateBetText(betText);

        saveBetToSP();

    }

    public void saveBetToSP() {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(getString(R.string.preference_current_bet),game.getBetType());
        editor.apply();
    }

    /**
     * Get the game variable, used to pass to fragments that might need
     * to do extensive work with it
     * @return Game instance
     */
    @Override
    public GamePlay getGame() {
        return this.game;
    }

    /**
     * Show a toast if user tries to bet with
     * an already used bet.
     */
    private void showBetToast() {
        // Init a bet string
        String betText;

        // Check if the bet type is 0(Low), and set the text accordingly
        if(game.getBetType() == 0) {
            betText = "Low";
        }else {
            // Get the bet type
            Integer bet = game.getBetType();
            // Set the bet type as a string
            betText = bet.toString();
        }

        // Init a toast message and get a pre-defined dialog text, adding the bet text
        String message = getString(R.string.dialog_bet_text, betText);

        // Init the toast itself
        Toast betToast = Toast.makeText(this, message ,Toast.LENGTH_SHORT);
        //Show the toast
        betToast.show();
    }

    /**
     * Show scores
     */
    private void showScores() {
        // Check if the play fragment exists
        if (playFrag != null) {
            // Get the round score
            Integer score = game.getRoundScore();

            // Call the play frag and update the scores
            playFrag.displayRound(game.getRoundNr()+1);
            playFrag.displayRoundScore(score);
        }
    }

    /**
     * Register the players score
     * @param playerName username of the player
     * @return Boolean, from SQL class true|false on fail
     */
    @Override
    public Boolean registerScore(String playerName) {
        // Init a new userdata class, with username and final score
        UserData user = new UserData(playerName, game.getFinalScore());

        // Init a new sql manager
        sql = new SQLManager(this);

        // Return a call to the SQL class with a boolean return indicator
        return sql.insertUser(user);
    }

    /**
     * Close the current activity
     */
    @Override
    public void closeActivity() {
        // Call the shared preferences and editor
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Clear the shared preferences and apply the changes
        editor.clear();
        editor.apply();

        sql.closeConnection();

        // End the current activity
        this.finish();
    }

    /**
     * Get bets for showing to the user
     * @return String[] array of bets left
     */
    @Override
    public String[] getBets() {
        // Get bets from an pre-defined array
        String[] bets = getResources().getStringArray(R.array.bet_array);
        // Init new string array list
        ArrayList<String> listBets = new ArrayList<>();

        // Run through the bets
        for (String bet: bets) {
            // Check if bet string is "Low" and set it to zero for integer check
            if (bet.equals("Low"))
                bet = "0";

            // Check if the current bet has been used before
            if (!game.getBetsDone().contains(Integer.valueOf(bet))) {
                // Return 0 to "Low", and add the bets if not done
                if (bet.equals("0"))
                    listBets.add("Low");
                else
                    listBets.add(bet);
            }
        }

        // Return array of strings with available bets
        return listBets.toArray(new String[listBets.size()]);
    }

    /**
     * Click close.
     */
    @Override
    public void onClickClose() {
        // Check if the fragment container is present
        if (findViewById(R.id.overlay_fragment) != null) {
            // Find the overlay fragment
            overlayFrag = (OverlayRound) getSupportFragmentManager().findFragmentByTag(TAG_POPUP_FRAG);

            // Set new overlay fragment if not exist
            if (overlayFrag == null)
                overlayFrag = new OverlayRound();

            // Remove the overlay fragment and add it to the backstack
            getSupportFragmentManager().beginTransaction()
                    .remove(overlayFrag)
                    .commit();
            // Roll the dice
            game.roll();

            // Check if play fragment is present and update the text
            if (playFrag != null)
                playFrag.updateButtonText(R.string.btn_roll);
        }
        
    }

    /**
     * Get variables for the overlay fragment
     * @return ArrayList of strings
     */
    @Override
    public ArrayList<String> getOverlayVariables() {
        // Init new array list
        ArrayList<String> strings = new ArrayList<>();

        // Add variables to the array list that the overlay needs
        strings.add(game.getRoundNr().toString());
        strings.add(game.getRoundScore().toString());
        strings.add(game.getFinalScore().toString());

        return strings;
    }

    /**
     * Get if text should be hidden
     * @return boolean
     */
    @Override
    public Boolean getTextHidden() {
        // Get preference from shared preferences
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        // Return bool if hidden
        return sharedPreferences.getBoolean(getString(R.string.preferences_text_hidden), false);
    }

    /**
     * Set text to hidden
     * @param hidden boolean
     */
    @Override
    public void setTextHidden(Boolean hidden) {
        // Get the shared preferences and create an editor
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Add boolean if hidden and apply
        editor.putBoolean(getString(R.string.preferences_text_hidden), hidden);
        editor.apply();
    }
}

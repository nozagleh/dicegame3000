package com.example.arnarfreyr.dicegame3000;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // Init the buttons on the main screen
    Button btnPlay;
    Button btnScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init elements in the activity
        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnScore = (Button)findViewById(R.id.btnScore);

        // Button play on click listener
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start game
                startGame(v);
            }
        });

        // Button highscore on click listener
        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show highscore
                showHighScore(v);
            }
        });
    }

    /**
     * Start the main game activity.
     * @param view
     */
    public void startGame(View view) {
        initActivity(Game.class);
    }

    /**
     * Start the highscore activity
     * @param view
     */
    public void showHighScore(View view) {
        initActivity(Highscore.class);
    }

    /**
     * Multipurpose class for starting a new activity
     * @param c Class, activity to start
     */
    public void initActivity(Class c) {
        // Create a new intent for the game activity
        Intent intent = new Intent(this, c);
        // Start the game activity
        startActivity(intent);
    }
}

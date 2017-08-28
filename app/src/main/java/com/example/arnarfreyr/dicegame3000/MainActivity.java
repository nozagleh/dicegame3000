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
    Button btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init elements in the activity
        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnScore = (Button)findViewById(R.id.btnScore);
        btnSettings = (Button)findViewById(R.id.btnSettings);

        // Button play on click listener
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start game
                initActivity(Game.class);
            }
        });

        // Button highscore on click listener
        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show highscore
                initActivity(Highscore.class);
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initActivity(SettingsActivity.class);
            }
        });
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

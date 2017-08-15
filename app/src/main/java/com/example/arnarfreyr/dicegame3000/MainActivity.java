package com.example.arnarfreyr.dicegame3000;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnPlay;
    Button btnScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init elements in the activity
        btnPlay = (Button)findViewById(R.id.btnPlay);
        btnPlay.setText(R.string.btn_play);
        btnScore = (Button)findViewById(R.id.btnScore);
        btnScore.setText(R.string.btn_highscore);
    }

    /**
     * Start the main game activity.
     * @param view
     */
    public void startGame(View view) {
        // Create a new intent for the game activity
        Intent intent = new Intent(this, Game.class);
        // Start the game activity
        startActivity(intent);
    }
}

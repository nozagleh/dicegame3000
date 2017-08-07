package com.example.arnarfreyr.dicegame3000;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Game extends AppCompatActivity
        implements Play.OnFragmentInteractionListener{

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

            // Set arguments for the play fragment
            fmPlay.setArguments(getIntent().getExtras());

            // Add the play fragment to the fragment manager
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fmPlay).commit();
        }
    }

    @Override
    public void onPlayFragmentInteraction(Uri uri) {
        // Do stuff
    }
}

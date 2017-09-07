package com.nozagleh.dicegame3000;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class Highscore extends AppCompatActivity {

    // Init the recycler view and components needed for it
    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        // Get the action bar and set the back button
        ActionBar aB = getSupportActionBar();
        if (aB != null)
            aB.setDisplayHomeAsUpEnabled(true);

        // Init the recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.scoreView);

        // Init an arraylist of user data
        ArrayList<UserData> userData;

        // Set up a new SQL connection
        SQLManager sql = new SQLManager(getApplicationContext());

        // Get the highscore from the DB
        userData = sql.getHighScoreList();

        // Set the recycler view adapter, pass the user data
        mAdapter = new ScoreRecyclerViewAdapter(getApplicationContext(), userData, false);
        mRecyclerView.setAdapter(mAdapter);
        // Set the layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    /**
     * Finish the activity on navigate up
     * @return boolean true
     */
    @Override
    public boolean onSupportNavigateUp() {
        // Finish the activity
        finish();
        return true;
    }
}

package com.example.arnarfreyr.dicegame3000;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class Highscore extends AppCompatActivity {

    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        mRecyclerView = (RecyclerView) findViewById(R.id.scoreView);

        ArrayList<UserData> userData;

        SQLManager sql = new SQLManager(getApplicationContext());

        userData = sql.getHighScoreList();

        mAdapter = new ScoreRecyclerViewAdapter(getApplicationContext(), userData);

        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}

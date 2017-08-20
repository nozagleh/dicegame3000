package com.example.arnarfreyr.dicegame3000;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by arnarfreyr on 16.8.2017.
 */

public class ScoreRecyclerViewAdapter extends RecyclerView.Adapter<ScoreRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UserData> mScores;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtRound;
        public TextView txtScore;
        public ViewHolder(View v) {
            super(v);
            txtRound = (TextView) v.findViewById(R.id.txtLstRound);
            txtScore = (TextView) v.findViewById(R.id.txtLstScore);
        }
    }

    public ScoreRecyclerViewAdapter(Context context, ArrayList<UserData> scores) {
        this.context = context;
        this.mScores = scores;
    }

    @Override
    public ScoreRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.round_listing, parent, false);

        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ScoreRecyclerViewAdapter.ViewHolder holder, int position) {
        String nameOrRound = "";
        nameOrRound = String.format(context.getResources().getString(R.string.txt_rounds_number), String.valueOf(position + 1));
        if (this.mScores.get(position).getName() != null) {
            nameOrRound = this.mScores.get(position).getName();
        }
        holder.txtRound.setText(nameOrRound);

        String score = String.valueOf(this.mScores.get(position).getScore());
        holder.txtScore.setText(score);
    }

    @Override
    public int getItemCount() {
        return this.mScores.size();
    }
}

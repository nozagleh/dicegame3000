package com.nozagleh.dicegame3000;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Recycler view adapter, for the various recycler lists in the app
 *
 * Created by arnarfreyr on 16.8.2017.
 */

public class ScoreRecyclerViewAdapter extends RecyclerView.Adapter<ScoreRecyclerViewAdapter.ViewHolder> {
    // Init the context
    private Context context;

    // Init an array list for the user data
    private ArrayList<UserData> mScores;

    private Boolean isAddScore;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Init the text views
        public TextView txtRound;
        public TextView txtScore;
        public ViewHolder(View v) {
            super(v);
            txtRound = (TextView) v.findViewById(R.id.txtLstRound);
            txtScore = (TextView) v.findViewById(R.id.txtLstScore);
        }
    }

    /**
     * Constructor that takes in the context of the app and the user data to be used
     * @param context App context
     * @param scores UserData scores for the rounds
     */
    public ScoreRecyclerViewAdapter(Context context, ArrayList<UserData> scores, Boolean isAddScore) {
        // Set the variables
        this.context = context;
        this.mScores = scores;
        this.isAddScore = isAddScore;
    }

    @Override
    public ScoreRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate a new view with the custom recycler view XML
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.round_listing, parent, false);

        // Return a view holder
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ScoreRecyclerViewAdapter.ViewHolder holder, int position) {
        // Init an empty string
        String nameOrRound;

        // Init the string bet variable
        String stringBet = "";
        // Check if the bet exists
        if (!this.mScores.get(position).getBetType().equals(-1)) {
            // Get the bet
            int bet = this.mScores.get(position).getBetType();

            // Check if the bet is "Low"
            if (bet == 0)
                stringBet = "Low";
            else
                stringBet = String.valueOf(bet);
        }

        // Set the string to the round number
        nameOrRound = String.format(context.getResources().getString(R.string.txt_bet_chosen_string), stringBet);

        // If the name is not empty for this position, set name instead of round
        if (this.mScores.get(position).getName() != null && !isAddScore) {
            nameOrRound = String.valueOf(position + 1) + ". " + this.mScores.get(position).getName();
        }

        // Set the round/name text
        holder.txtRound.setText(nameOrRound);

        // Assemble a string with the score and set it to the text view
        String score = String.valueOf(context.getString(R.string.txt_pts, this.mScores.get(position).getScore().toString()));
        holder.txtScore.setText(score);
    }

    /**
     * Returns the item count in the recycler view
     * @return int count of objects
     */
    @Override
    public int getItemCount() {
        return this.mScores.size();
    }
}

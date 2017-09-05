package com.nozagleh.dicegame3000;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;


/**
 * Score fragment displays the final score to the user
 * showing the user a list of all the rounds and the scores.
 *
 * As well as allowing the user to submit their score to the highscore list.
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentListener} interface
 * to handle interaction events.
 * Use the {@link Score#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Score extends Fragment {

    // Init a view for the fragment
    private View view;

    // Init a gameplay object
    private GamePlay mGame;

    // Init the custom fragment listener
    private FragmentListener mListener;

    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    // Init submit button and editor for the username
    private Button btnSubmit;
    private EditText txtName;

    public Score() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Score.
     */
    public static Score newInstance() {
        Score fragment = new Score();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the game, makes it easier for us to fetch every data object we might want to use
        // Since the game wont change any more, we can fetch this on create in the frag
        // TODO, test if this fails on rotation!
        mGame = mListener.getGame();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set the view
        view = inflater.inflate(R.layout.fragment_score, container, false);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Create the recycler view, that will show the round scores
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rcView);

        // Init a new array list for the user data
        ArrayList<UserData> data = new ArrayList<>();
        // Get all the round scores of the previous game
        for (int score: mGame.getTotalScore()) {
            // Init a new temporary user data object
            UserData ud = new UserData();
            // Set the score
            ud.setScore(score);
            // Add the user data to the array list of user data
            data.add(ud);
        }

        // Set the adapter for the recycler view, along with the data
        mAdapter = new ScoreRecyclerViewAdapter(getContext(), data);
        mRecyclerView.setAdapter(mAdapter);

        // Set the layout manager for the recycler view
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Init the input field for the username
        txtName = (EditText) view.findViewById(R.id.etName);

        // Init the submit button and add an onlick listener
        btnSubmit = (Button)view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the register score in the parent activity returns true,
                // thus the score has been inserted into the database without error
                if (mListener.registerScore(txtName.getText().toString())) {
                    // Call fragment listener for closing the activity
                    mListener.closeActivity();
                } else {
                    // TODO add toast on fail
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Check if the context is of type fragment listener
        if (context instanceof FragmentListener) {
            // Bind the fragment listener
            mListener = (FragmentListener) context;
        } else {
            // Throw an error if the fragment listener was not found
            throw new RuntimeException(context.toString()
                    + " must implement FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Release the fragment listener
        mListener = null;
    }
}

package com.example.arnarfreyr.dicegame3000;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


/**
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
        Log.d("SCORES -->", mListener.getGame().getTotalScore().toString());
        mGame = mListener.getGame();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_score, container, false);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.rcView);

        ArrayList<UserData> data = new ArrayList<>();
        for (int score: mGame.getTotalScore()) {
            UserData ud = new UserData();
            ud.setScore(score);
            data.add(ud);
        }
        mAdapter = new ScoreRecyclerViewAdapter(getContext(), data);

        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        txtName = (EditText) view.findViewById(R.id.etName);

        btnSubmit = (Button)view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener.registerScore(txtName.getText().toString())) {
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

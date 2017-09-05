package com.nozagleh.dicegame3000;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * OverlayRound fragment.
 *
 * Shows informative info to the user after each round.
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OverlayFragmentListener} interface
 * to handle interaction events.
 * Use the {@link OverlayRound#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverlayRound extends Fragment {

    TextView txtRound;
    TextView txtScore;
    TextView txtTotalScore;

    Button btnNext;

    ArrayList<String> variables = null;

    View view;

    private OverlayFragmentListener mListener;

    public OverlayRound() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OverlayRound.
     */
    // TODO: Rename and change types and number of parameters
    public static OverlayRound newInstance() {
        OverlayRound fragment = new OverlayRound();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Check if the fragment listener exists
        if (mListener != null) {
            // Get variables from the parent activity
            variables = mListener.getOverlayVariables();
        }

        // Check if the variables are not null
        if (variables != null) {
            // Set multiple texts
            txtRound.setText(String.format(getResources().getString(R.string.txt_rounds_number), variables.get(0)));
            txtScore.setText(String.format(getResources().getString(R.string.txt_pts), variables.get(1)));
            txtTotalScore.setText(String.format(getResources().getString(R.string.txt_total_score), variables.get(2)));
        }

        // Set on click listener for the next button
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call on button pressed function
                onButtonPressed();
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_overlay_round, container, false);

        // Init the text views in the fragment
        txtRound = (TextView) view.findViewById(R.id.wScoreTitle);
        txtScore = (TextView) view.findViewById(R.id.wScore);
        txtTotalScore = (TextView) view.findViewById(R.id.wTotalScoreLbl);
        // Init the next button in the fragment
        btnNext = (Button) view.findViewById(R.id.wBtnNext);

        // Return the view
        return view;
    }

    /**
     * On button press activity.
     * Used for the "Next" button in the fragment
     */
    public void onButtonPressed() {
        // Check if the fragment listener is connected
        if (mListener != null) {
            // Call the parent to run its closing function
            mListener.onClickClose();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Check for the fragment listener
        if (context instanceof OverlayFragmentListener) {
            mListener = (OverlayFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Set fragment listener to null on detach
        mListener = null;
    }
}

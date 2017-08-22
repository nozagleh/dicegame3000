package com.example.arnarfreyr.dicegame3000;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


/**
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

        if (mListener != null) {
            variables = mListener.getOverlayVariables();
        }

        if (variables != null) {
            txtRound.setText(String.format(getResources().getString(R.string.txt_rounds_number), variables.get(0)));
            txtScore.setText(String.format(getResources().getString(R.string.txt_pts), variables.get(1)));
            txtTotalScore.setText(String.format(getResources().getString(R.string.txt_total_score), variables.get(2)));
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_overlay_round, container, false);

        txtRound = (TextView) view.findViewById(R.id.wScoreTitle);
        txtScore = (TextView) view.findViewById(R.id.wScore);
        txtTotalScore = (TextView) view.findViewById(R.id.wTotalScoreLbl);

        btnNext = (Button) view.findViewById(R.id.wBtnNext);

        return view;
    }

    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onClickClose();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        mListener = null;
    }
}

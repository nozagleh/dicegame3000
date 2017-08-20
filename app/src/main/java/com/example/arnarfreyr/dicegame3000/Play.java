package com.example.arnarfreyr.dicegame3000;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentListener} interface
 * to handle interaction events.
 * Use the {@link Play#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Play extends Fragment implements View.OnClickListener {

    View view;

    Button btnRoll;
    Button btnBet;
    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;
    ImageView img6;

    TextView txtScore;
    TextView txtRollNr;
    TextView txtRoundNr;

    Boolean lockDice;

    ArrayList<Integer> prevValues;
    ArrayList<ImageView> imgs;
    SparseIntArray imgFiles = new SparseIntArray();

    private FragmentListener mListener;

    public Play() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Play.
     */
    public static Play newInstance() {
        Play fragment = new Play();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("SAVE STATE -->", "YES");
        outState.putIntegerArrayList("SCORES", mListener.getScores());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_play, container, false);

        // Init the roll button and tie the click listener
        btnRoll = (Button)view.findViewById(R.id.btnRoll);
        btnRoll.setOnClickListener(this);

        btnBet = (Button)view.findViewById(R.id.btnBet);
        btnBet.setOnClickListener(this);

        // Init the image views for the dice
        img1 = (ImageView)view.findViewById(R.id.imageView);
        img2 = (ImageView)view.findViewById(R.id.imageView2);
        img3 = (ImageView)view.findViewById(R.id.imageView3);
        img4 = (ImageView)view.findViewById(R.id.imageView4);
        img5 = (ImageView)view.findViewById(R.id.imageView5);
        img6 = (ImageView)view.findViewById(R.id.imageView6);

        // Set click listeners for the imageviews
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);
        img6.setOnClickListener(this);

        // Init an array list for the image views
        imgs = new ArrayList<>();
        // Add all images to the list
        imgs.add(img1);
        imgs.add(img2);
        imgs.add(img3);
        imgs.add(img4);
        imgs.add(img5);
        imgs.add(img6);

        // Add red dices
        imgFiles.put(0, R.drawable.red1);
        imgFiles.put(1, R.drawable.red2);
        imgFiles.put(2, R.drawable.red3);
        imgFiles.put(3, R.drawable.red4);
        imgFiles.put(4, R.drawable.red5);
        imgFiles.put(5, R.drawable.red6);
        // Add gray dices
        imgFiles.put(6, R.drawable.locked1);
        imgFiles.put(7, R.drawable.locked2);
        imgFiles.put(8, R.drawable.locked3);
        imgFiles.put(9, R.drawable.locked4);
        imgFiles.put(10, R.drawable.locked5);
        imgFiles.put(11, R.drawable.locked6);

        int count = 0;
        for (ImageView iv: imgs) {
            iv.setImageResource(imgFiles.get(count));
            count++;
        }

        txtRollNr = (TextView)view.findViewById(R.id.txtRoll);
        txtRoundNr = (TextView)view.findViewById(R.id.txtRound);
        txtScore = (TextView)view.findViewById(R.id.txtScore);
        Log.d("AA","BB");
        if(prevValues != null) {
            Log.d("CC","DD");
            txtRollNr.setText(prevValues.get(0));
            txtRoundNr.setText(prevValues.get(1));
            txtScore.setText(prevValues.get(2));
        }

        lockDice = false;

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            mListener = (FragmentListener) context;
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

    @Override
    public void onClick(View v) {
        // Check what item was pressed
        Boolean found = false;
        switch (v.getId()) {
            case R.id.btnRoll:
                mListener.onRollClick();
                found = true;
                break;
            case R.id.btnBet:
                mListener.onBetChange();
                found = true;
                break;
        }
        Log.d("DICE locked? -->", lockDice.toString());
        if (!found && !lockDice) {
            Log.d("DIE -->", "clicked");
            int imgNr = getRightImg(v);
            mListener.onDieChosen(imgNr);
        }
    }

    public Integer getRightImg(View v) {
        int count = 0;
        for (ImageView iv: this.imgs) {
            if (v.getId() == iv.getId()) {
                return count;
            }
            count++;
        }
        return -1;
    }

    /**
     * Update the imageview images in the fragment
     * @param dice Values for the images (int)
     */
    public void updateImages(Dice dice) {
        for(int i = 0; i < imgs.size(); i++) {
            Integer chosenOffset = 0;

            if (dice.getDie(i).getIfChosen()) {
                chosenOffset += 6;
            }

            imgs.get(i).setImageResource(imgFiles.get(dice.getDie(i).getDieValue() - 1 + chosenOffset));
        }
    }

    public void updateBetText(String text) {
        btnBet.setText(text);
    }

    public void updateButtonText(int stringId) {
        btnRoll.setText(stringId);
    }

    public void displayRoundScore(Integer score) {
        Animation scoreAnim = AnimationUtils.loadAnimation(getContext(), R.anim.score_text);
        scoreAnim.reset();
        txtScore.clearAnimation();
        txtScore.startAnimation(scoreAnim);

        txtScore.setText(score.toString());

        scoreAnim = AnimationUtils.loadAnimation(getContext(), R.anim.score_text_in);
        txtScore.startAnimation(scoreAnim);
    }

    public void displayRoll(Integer rollNr) {
        txtRollNr.setText(rollNr.toString());
    }

    public void displayRound(Integer roundNr) {
        txtRoundNr.setText(roundNr.toString());
    }

    public void lockDice() {
        lockDice = true;
    }

    public void unlockDice() {
        lockDice = false;
    }
}

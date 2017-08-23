package com.example.arnarfreyr.dicegame3000;

import android.content.Context;
import android.content.SharedPreferences;
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

    TextView lblScores;
    TextView lblRoll;
    TextView lblRound;
    TextView lblScore;

    TextView txtScore;
    TextView txtRollNr;
    TextView txtRoundNr;

    Boolean lockDice;
    Boolean txtHidden;

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

        // Add red dice images
        imgFiles.put(0, R.drawable.red1);
        imgFiles.put(1, R.drawable.red2);
        imgFiles.put(2, R.drawable.red3);
        imgFiles.put(3, R.drawable.red4);
        imgFiles.put(4, R.drawable.red5);
        imgFiles.put(5, R.drawable.red6);
        // Add locked dice images
        imgFiles.put(6, R.drawable.locked1);
        imgFiles.put(7, R.drawable.locked2);
        imgFiles.put(8, R.drawable.locked3);
        imgFiles.put(9, R.drawable.locked4);
        imgFiles.put(10, R.drawable.locked5);
        imgFiles.put(11, R.drawable.locked6);

        // Set the image resources
        int count = 0;
        for (ImageView iv: imgs) {
            iv.setImageResource(imgFiles.get(count));
            count++;
        }

        // Set all labels in the fragment
        lblScores = (TextView)view.findViewById(R.id.lblScores);
        lblRoll = (TextView)view.findViewById(R.id.txtRollLabel);
        lblRound = (TextView)view.findViewById(R.id.txtRoundLabel);
        lblScore = (TextView)view.findViewById(R.id.txtScoreLabel);

        // Set texts in the fragment
        txtRollNr = (TextView)view.findViewById(R.id.txtRoll);
        txtRoundNr = (TextView)view.findViewById(R.id.txtRound);
        txtScore = (TextView)view.findViewById(R.id.txtScore);

        // Set lock dice to false
        lockDice = false;

        // Check if the fragment listener is connected, get if text is hidden
        if (mListener != null) {
            txtHidden = mListener.getTextHidden();

            // Check if text should be hidden
            if (txtHidden) {
                // Set the visibility of labels to invisible
                lblScores.setVisibility(View.INVISIBLE);
                lblRoll.setVisibility(View.INVISIBLE);
                lblRound.setVisibility(View.INVISIBLE);
                lblScore.setVisibility(View.INVISIBLE);

                // Set the visibility of text views to invisible
                txtRollNr.setVisibility(View.INVISIBLE);
                txtRoundNr.setVisibility(View.INVISIBLE);
                txtScore.setVisibility(View.INVISIBLE);
            }
        }

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

        // Use a switch to determine which button was pressed
        switch (v.getId()) {
            case R.id.btnRoll:
                // Call fragment listener on roll click
                mListener.onRollClick();

                // Check if the text is hidden, show on roll
                if (txtHidden) {
                    showLabels();
                }

                // Set switch found to true
                found = true;
                break;
            case R.id.btnBet:
                // Call fragment listener on bet change function
                mListener.onBetChange();

                // Set found to true
                found = true;
                break;
        }

        // Check if the clicked element has been found and if it is a die, is not locked
        if (!found && !lockDice) {
            // Get the image number from the clicked die
            int imgNr = getRightImg(v);
            // Call the fragment listener to update the die image
            mListener.onDieChosen(imgNr);
        }
    }

    /**
     * Get the image for the clicked die
     * @param v View
     * @return Integer number of die clicked
     */
    public Integer getRightImg(View v) {
        // Init a count to 0
        int count = 0;

        // Run through all the image views in the fragment
        for (ImageView iv: this.imgs) {
            // If the current image view matches the id of the clicked one
            if (v.getId() == iv.getId()) {
                // Return the count
                return count;
            }
            // Add to count
            count++;
        }

        // Return -1 if nothing found
        return -1;
    }

    /**
     * Update the imageview images in the fragment
     * @param dice Values for the images (int)
     */
    public void updateImages(Dice dice) {
        // Run through each of the images
        for(int i = 0; i < imgs.size(); i++) {
            // Init chosen offset
            Integer chosenOffset = 0;
            // Check if the die is chosen
            if (dice.getDie(i).getIfChosen()) {
                // Add to offset to get the right image for the "chosen" image
                chosenOffset += 6;
            }
            // Set the image resource of the die, adding the offset
            imgs.get(i).setImageResource(imgFiles.get(dice.getDie(i).getDieValue() - 1 + chosenOffset));
        }
    }

    /**
     * Update bet button text
     * @param text String text to change to
     */
    public void updateBetText(String text) {
        // Set the button text
        btnBet.setText(text);
    }

    /**
     * Update the button roll text
     * @param stringId String id of the string resource
     */
    public void updateButtonText(int stringId) {
        // Set the text of the button
        btnRoll.setText(stringId);
    }

    /**
     * Display the score after a round, update the score text
     * @param score Integer score, the score to show
     */
    public void displayRoundScore(Integer score) {
        // Load an animation for the change
        Animation scoreAnim = AnimationUtils.loadAnimation(getContext(), R.anim.score_text);
        // Reset the animation
        scoreAnim.reset();

        // Clear the animation on the text view
        txtScore.clearAnimation();
        // Start the animation
        txtScore.startAnimation(scoreAnim);

        // Set the text
        txtScore.setText(String.valueOf(score));

        // Start the second animation and start it
        scoreAnim = AnimationUtils.loadAnimation(getContext(), R.anim.score_text_in);
        txtScore.startAnimation(scoreAnim);
    }

    /**
     * Display the roll number in the fragment
     * @param rollNr Integer roll number
     */
    public void displayRoll(Integer rollNr) {
        txtRollNr.setText(String.valueOf(rollNr));
    }

    /**
     * Display the round number in the fragment
     * @param roundNr Integer round number
     */
    public void displayRound(Integer roundNr) {
        txtRoundNr.setText(String.valueOf(roundNr));
    }

    /**
     * Lock the dice group, preventing changes to the dice
     */
    public void lockDice() {
        lockDice = true;
    }

    /**
     * Unlock the dice group, allowing for changes to the dice
     */
    public void unlockDice() {
        lockDice = false;
    }

    /**
     * Show label in the fragment
     */
    public void showLabels() {
        // Set the visibility of the labels to visible
        lblScores.setVisibility(View.VISIBLE);
        lblRoll.setVisibility(View.VISIBLE);
        lblRound.setVisibility(View.VISIBLE);
        lblScore.setVisibility(View.VISIBLE);

        // Set the visibility of the text views to visible
        txtRollNr.setVisibility(View.VISIBLE);
        txtRoundNr.setVisibility(View.VISIBLE);
        txtScore.setVisibility(View.VISIBLE);

        // Check if the fragment listener is initiated
        if (mListener != null) {
            // Call the fragments listener function for hidden text
            mListener.setTextHidden(false);
        }
    }
}

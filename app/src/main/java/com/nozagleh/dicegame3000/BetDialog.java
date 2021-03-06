package com.nozagleh.dicegame3000;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Bet Dialog. A dialog that shows the bets that are
 * available to the user in-game.
 *
 * Created by arnar on 2017-08-10.
 */

public class BetDialog extends DialogFragment {
    // Init the fragment listener
    private FragmentListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] bets = mListener.getBets();
        // Set the dialog title and items
        builder.setTitle(R.string.dialog_bet)
                .setItems(bets, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Call listeners function on bet selection
                        mListener.onBetSelected(which);
                    }
                });

        // Return the builder create
        return builder.create();
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
}

package com.example.arnarfreyr.dicegame3000;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by arnarfreyr on 16.8.2017.
 */

public class ScoreListViewAdapter extends ArrayAdapter<String> {

    public ScoreListViewAdapter(Context context, List<String> values) {
        super(context, android.R.layout.simple_list_item_2);
    }
}

package com.example.arnarfreyr.dicegame3000;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get the action bar and set the back button
        ActionBar aB = getSupportActionBar();
        if (aB != null)
            aB.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Finish the activity on navigate up
     * @return boolean true
     */
    @Override
    public boolean onSupportNavigateUp() {
        // Finish the activity
        finish();
        return true;
    }

    /**
     * Remove all entries in the database and reset it
     * @return Boolean true|false on failure
     */
    public boolean purgeDatabase() {

        return true;
    }

    /**
     * Remove all the settings saved in the Shared Preferences
     * @return Boolean true|false on failure
     */
    public boolean purgeSP() {

        return true;
    }
}

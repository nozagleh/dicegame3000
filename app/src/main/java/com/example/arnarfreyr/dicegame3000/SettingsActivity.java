package com.example.arnarfreyr.dicegame3000;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    Switch dbDelete;

    SQLManager sqlManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get the action bar and set the back button
        ActionBar aB = getSupportActionBar();
        if (aB != null)
            aB.setDisplayHomeAsUpEnabled(true);

        sqlManager = new SQLManager(this);

        final SharedPreferences sp = this.getPreferences(MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();

        dbDelete = (Switch) findViewById(R.id.swDB);
        dbDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbDelete.setEnabled(true);
                //sqlManager.clearDB();

                editor.clear();
                editor.apply();
                dbDelete.setEnabled(false);
            }
        });
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

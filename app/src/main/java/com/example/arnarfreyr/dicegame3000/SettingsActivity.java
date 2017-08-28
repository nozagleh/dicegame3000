package com.example.arnarfreyr.dicegame3000;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    protected final String TAG_SETTINGS = "SettingsActivity";

    Switch dbDelete;
    Switch spDelete;

    SQLManager sqlManager;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get the action bar and set the back button
        ActionBar aB = getSupportActionBar();
        if (aB != null)
            aB.setDisplayHomeAsUpEnabled(true);

        sqlManager = new SQLManager(getApplicationContext());

        sp = this.getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = sp.edit();

        dbDelete = (Switch) findViewById(R.id.swDB);
        spDelete = (Switch) findViewById(R.id.swSharedPref);

        dbDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int message;
                if (isChecked) {
                    if (sqlManager.clearDB())
                        message = R.string.settings_purge_db;
                    else
                        message = R.string.settings_purge_db_fail;

                    Snackbar dbDeleteSnack = Snackbar.make(findViewById(R.id.claySettings),
                            message,
                            Snackbar.LENGTH_SHORT);
                    dbDeleteSnack.show();

                    dbDelete.setEnabled(false);

                }
            }
        });

        spDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.clear();
                    editor.apply();

                    Snackbar dbDeleteSnack = Snackbar.make(findViewById(R.id.claySettings),
                            R.string.settings_purge_sp,
                            Snackbar.LENGTH_SHORT);
                    dbDeleteSnack.show();

                    spDelete.setEnabled(false);
                }
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

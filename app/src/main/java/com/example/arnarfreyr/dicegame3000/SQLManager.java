package com.example.arnarfreyr.dicegame3000;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * SQL Manager class handles all the transactions between the app
 * and the database for the application
 *
 * Created by arnarfreyr on 19.8.2017.
 */

public class SQLManager {
    // Set class tag for error logging
    private static final String TAG = "SQLManager";

    // Init SQL classes
    private SQLHandler handler = null;
    private SQLiteDatabase db = null;

    /**
     * Constructor, takes in the app context
     * @param context Context of the application
     */
    public SQLManager(Context context) {
        handler = new SQLHandler(context);
    }

    /**
     * Set the database into write mode
     */
    private void setWrite() {
        db = handler.getWritableDatabase();
    }

    /**
     * Set the database into read mode
     */
    private void setRead() {
        db = handler.getReadableDatabase();
    }

    /**
     * Insert a user into the database
     *
     * @param user UserData, user object
     * @return Boolean, true|false on success
     */
    public Boolean insertUser(UserData user) {
        // Set the database into write mode
        setWrite();

        // Create a new content values object
        ContentValues values = new ContentValues();

        // Put the user values into the content values
        values.put(SQLConstants.Entries.HIGHSCORES_NAME, user.getName());
        values.put(SQLConstants.Entries.HIGHSCORE_SCORE, user.getScore());

        // Init a new datetime
        Date date = new Date();
        // Put the date in content values
        values.put(SQLConstants.Entries.HIGHSCORE_DATETIME, date.toString());

        try {
            // Try an insert of values
            db.insert(SQLConstants.Entries.TABLE_NAME, null, values);
        } catch (SQLException e) {
            // Log error exception on fail inser
            Log.e(TAG, e.getMessage(), e);
            return false;
        }
        return true;
    }

    /**
     * Get the highscores from the database. The highscore returned
     * is in the format of UserData object list
     *
     * @return ArrayList of UserData objects
     */
    public ArrayList<UserData> getHighScoreList() {

        setRead();

        String[] columns = {
                SQLConstants.Entries._ID,
                SQLConstants.Entries.HIGHSCORES_NAME,
                SQLConstants.Entries.HIGHSCORE_SCORE
        };

        Cursor queryCursor = db.query(
                SQLConstants.Entries.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                SQLConstants.Entries.HIGHSCORE_SCORE + " DESC"
        );

        ArrayList<UserData> userData = new ArrayList<>();
        while (queryCursor.moveToNext()) {
            UserData user = new UserData();
            user.setName(queryCursor.getString(queryCursor.getColumnIndexOrThrow(SQLConstants.Entries.HIGHSCORES_NAME)));
            user.setScore(queryCursor.getInt(queryCursor.getColumnIndexOrThrow(SQLConstants.Entries.HIGHSCORE_SCORE)));

            userData.add(user);
        }

        queryCursor.close();

        return userData;
    }

    /**
     * Close the sql connection
     */
    public void closeConnection() {
        try {
            if (db.isOpen())
                db.close();
        } catch(SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}

/**
 * SQL handler class,
 * Extends SQLlite helper class
 */
class SQLHandler extends SQLiteOpenHelper {

    // Static variables for the database
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "dicegameScores.db";

    // Create table statement
    private static final String SQL_CREATE_HIGHSCORES =
            "CREATE TABLE " + SQLConstants.Entries.TABLE_NAME + " (" +
                    SQLConstants.Entries._ID + " INTEGER PRIMARY KEY," +
                    SQLConstants.Entries.HIGHSCORES_NAME + " TEXT," +
                    SQLConstants.Entries.HIGHSCORE_SCORE + " INTEGER," +
                    SQLConstants.Entries.HIGHSCORE_DATETIME + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

    // Drop table statement
    private static final String SQL_DROP_HIGHSCORES =
            "DROP TABLE IF EXISTS " +
                    SQLConstants.Entries.TABLE_NAME + ";";

    /**
     * Empty required constructor, gets the context
     * @param context Current context
     */
    public SQLHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * On create, create the highscores table
     * @param db DB
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_HIGHSCORES);
    }

    /**
     * On DB version upgrade, for now just drop the table and recreate it
     * @param db DB
     * @param oldVersion Old version number
     * @param newVersion New version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_HIGHSCORES);
        onCreate(db);
    }
}

/**
 * Class for keeping the SQL name constants
 */
class SQLConstants {
    private SQLConstants() {}

    /**
     * SQL name constants
     */
    public static class Entries implements BaseColumns {
        public static final String TABLE_NAME = "highscores";
        public static final String HIGHSCORES_NAME = "user";
        public static final String HIGHSCORE_SCORE = "score";
        public static final String HIGHSCORE_DATETIME = "datetime";
    }
}

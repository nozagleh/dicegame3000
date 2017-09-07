package com.nozagleh.dicegame3000;

/**
 * UserData class.
 *
 * Stores the data of users and their scores.
 * Used to insert data into the database as well as fetching
 * database data in a structured way.
 *
 * Created by arnarfreyr on 19.8.2017.
 */

public class UserData {
    private String name = null;
    private Integer score = null;
    private Integer betType = null;

    /**
     * Empty constructor.
     */
    public UserData() {
        this.betType = -1;
    }

    /**
     * Constructor with name and score variables
     * @param name User name
     * @param score User score
     */
    public UserData(String name, Integer score) {
        this.name = name;
        this.score = score;
        this.betType = -1;
    }

    /**
     * Set the username
     * @param name String name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the username
     * @return String name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the score
     * @param score Integer score
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * Get the score
     * @return Integer score
     */
    public Integer getScore() {
        return this.score;
    }

    public Integer getBetType() {
        return betType;
    }

    public void setBetType(Integer betType) {
        this.betType = betType;
    }
}

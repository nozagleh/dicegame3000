package com.example.arnarfreyr.dicegame3000;

/**
 * Created by arnarfreyr on 19.8.2017.
 */

public class UserData {
    private String name = null;
    private Integer score = null;

    /**
     * Empty constructor.
     */
    public UserData() {
    }

    /**
     * Constructor with name and score variables
     * @param name User name
     * @param score User score
     */
    public UserData(String name, Integer score) {
        this.name = name;
        this.score = score;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
    public Integer getScore() {
        return this.score;
    }
}

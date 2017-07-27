package com.nearur.jarvis;

/**
 * Created by mrdis on 7/22/2017.
 */

public class Remember {

    String date,text;

    public Remember(String date, String text) {
        this.date = date;
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

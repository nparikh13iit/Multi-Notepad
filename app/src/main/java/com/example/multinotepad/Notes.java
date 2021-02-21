package com.example.multinotepad;

import java.io.Serializable;

public class Notes implements Serializable {

    private String title;
    private String note_text;
    private String date;

    public Notes(){
        this.title = "";
        this.note_text = "";
        this.date = "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote_text() {
        return note_text;
    }

    public void setNote_text(String note_text) {
        this.note_text = note_text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}

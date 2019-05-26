package com.e.topnoteapp.Model;

import java.io.Serializable;

public class Data implements Serializable {
    private String Title;
    private String Budget;
    private String Notes;
    private String Date;
    private String Id;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getBudget() {
        return Budget;
    }

    public void setBudget(String budget) {
        Budget = budget;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }
    public Data() {
    }

    public Data(String title, String budget, String notes, String date, String id) {
        Title = title;
        Budget = budget;
        Notes = notes;
        Date = date;
        Id = id;
    }
}

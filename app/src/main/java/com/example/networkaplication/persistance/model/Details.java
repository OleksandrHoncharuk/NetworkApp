package com.example.networkaplication.persistance.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "details")
public class Details {

    @PrimaryKey()
    @NonNull
    private String id;
    private String title;
    private String details;
    private String genre;
    private String plotSummary;

    public Details() {}

    @Ignore
    public Details (String id) {
        this.id = id;
    }

    @Ignore
    public Details(String id, String title, String details, String genre, String plotSummary) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.genre = genre;
        this.plotSummary = plotSummary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlotSummary() {
        return plotSummary;
    }

    public void setPlotSummary(String plotSummary) {
        this.plotSummary = plotSummary;
    }
}

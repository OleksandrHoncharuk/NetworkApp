package com.example.networkaplication.persistance.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import java.util.Objects;

@Entity(tableName = "movie_query")
public class MovieQuery {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private long date;

    public MovieQuery () {}

    @Ignore
    public MovieQuery (String name, long date) {
        this.name = name;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieQuery that = (MovieQuery) o;
        return id == that.id &&
                date == that.date &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, date);
    }

}

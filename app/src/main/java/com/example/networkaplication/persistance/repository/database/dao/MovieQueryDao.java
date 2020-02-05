package com.example.networkaplication.persistance.repository.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.example.networkaplication.persistance.model.MovieQuery;

import java.util.List;

@Dao
public abstract class MovieQueryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)

    public abstract void insert (MovieQuery movieQuery);

    @Transaction
    @Query("SELECT * FROM movie_query WHERE name LIKE :search")
    public abstract List<MovieQuery> findAllLike (String search);

    @Query("SELECT * FROM movie_query WHERE name=:name")
    public abstract MovieQuery findByTitle (String name);

    @Delete
    public abstract void delete(MovieQuery movieQuery);

    @Query("DELETE FROM movie_query")
    public abstract void deleteAll();
}

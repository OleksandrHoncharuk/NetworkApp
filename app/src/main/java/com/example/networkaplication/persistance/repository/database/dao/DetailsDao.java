package com.example.networkaplication.persistance.repository.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.networkaplication.persistance.model.Details;

@Dao
public abstract class DetailsDao {
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    public abstract void insert (Details details);

    @Delete
    public abstract void delete(Details details);

    @Update(onConflict = OnConflictStrategy.ROLLBACK)
    public abstract void update(Details details);

    @Query("DELETE FROM details")
    public abstract void deleteAll();

    @Query("SELECT * FROM details WHERE id=:id")
    public abstract Details findById(String id);

    @Query("SELECT * FROM details WHERE title=:title")
    public abstract Details findByTitle(String title);
}

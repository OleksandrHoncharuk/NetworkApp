package com.example.networkaplication.persistance.repository.database.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import com.example.networkaplication.persistance.model.Details

@Dao
abstract class DetailsDao {
    @Insert(onConflict = OnConflictStrategy.ROLLBACK)
    abstract fun insert(details: Details)

    @Delete
    abstract fun delete(details: Details)

    @Update(onConflict = OnConflictStrategy.ROLLBACK)
    abstract fun update(details: Details)

    @Query("DELETE FROM details")
    abstract fun deleteAll()

    @Query("SELECT * FROM details WHERE id=:id")
    abstract fun findById(id: String): Details

    @Query("SELECT * FROM details WHERE title=:title")
    abstract fun findByTitle(title: String): Details
}

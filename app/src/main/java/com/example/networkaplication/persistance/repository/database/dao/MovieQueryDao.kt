package com.example.networkaplication.persistance.repository.database.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

import com.example.networkaplication.persistance.model.MovieQuery

@Dao
abstract class MovieQueryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(movieQuery: MovieQuery)

    @Transaction
    @Query("SELECT * FROM movie_query WHERE name LIKE :search")
    abstract fun findAllLike(search: String): List<MovieQuery>

    @Query("SELECT * FROM movie_query WHERE name=:name")
    abstract fun findByTitle(name: String): MovieQuery

    @Delete
    abstract fun delete(movieQuery: MovieQuery)

    @Query("DELETE FROM movie_query")
    abstract fun deleteAll()
}

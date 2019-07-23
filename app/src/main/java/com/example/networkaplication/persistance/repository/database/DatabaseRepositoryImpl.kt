package com.example.networkaplication.persistance.repository.database

import android.content.Context

import androidx.annotation.VisibleForTesting

import com.example.networkaplication.persistance.repository.database.dao.DetailsDao
import com.example.networkaplication.persistance.repository.database.dao.MovieQueryDao

class DatabaseRepositoryImpl(context: Context) : DatabaseRepository {
    private var db: AppDatabase? = null

    init {
        db = AppDatabase.build(context)
    }

    override fun movieQueryDao(): MovieQueryDao {
        return db!!.movieQueryDao()
    }

    override fun detailsDao(): DetailsDao {
        return db!!.detailsDao()
    }

    @VisibleForTesting
    fun setDb(db: AppDatabase) {
        this.db = db
    }
}

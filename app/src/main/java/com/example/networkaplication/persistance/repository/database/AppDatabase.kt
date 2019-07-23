package com.example.networkaplication.persistance.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

import com.example.networkaplication.persistance.model.Details
import com.example.networkaplication.persistance.model.MovieQuery
import com.example.networkaplication.persistance.repository.database.dao.DetailsDao
import com.example.networkaplication.persistance.repository.database.dao.MovieQueryDao

@Database(version = 4, exportSchema = false, entities = [MovieQuery::class, Details::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieQueryDao(): MovieQueryDao

    abstract fun detailsDao(): DetailsDao

    companion object {

        private const val NAME = "app_database.db"

        @Synchronized
        fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, NAME)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {

                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {}
                    })
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
        }
    }
}
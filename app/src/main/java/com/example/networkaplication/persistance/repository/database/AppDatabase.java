package com.example.networkaplication.persistance.repository.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.networkaplication.persistance.model.Details;
import com.example.networkaplication.persistance.model.MovieQuery;
import com.example.networkaplication.persistance.repository.database.dao.DetailsDao;
import com.example.networkaplication.persistance.repository.database.dao.MovieQueryDao;

@Database(version = 4, exportSchema = false, entities = {MovieQuery.class, Details.class})
public abstract class AppDatabase extends RoomDatabase {

    static final String NAME = "app_database.db";

    public abstract MovieQueryDao movieQueryDao();

    public abstract DetailsDao detailsDao();

    public static synchronized AppDatabase build(@NonNull Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, AppDatabase.NAME)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {

                    }

                    @Override
                    public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    }
                })
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }
}
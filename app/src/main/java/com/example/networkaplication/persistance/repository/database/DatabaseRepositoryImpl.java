package com.example.networkaplication.persistance.repository.database;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.example.networkaplication.persistance.repository.database.dao.DetailsDao;
import com.example.networkaplication.persistance.repository.database.dao.MovieQueryDao;

public class DatabaseRepositoryImpl implements DatabaseRepository {
    private AppDatabase db;

    public DatabaseRepositoryImpl(Context context) {
        db = AppDatabase.build(context);
    }

    @Override
    public MovieQueryDao movieQueryDao() {
        return db.movieQueryDao();
    }

    @Override
    public DetailsDao detailsDao() { return db.detailsDao(); }

    @VisibleForTesting
    public void setDb(AppDatabase db) {
        this.db = db;
    }
}

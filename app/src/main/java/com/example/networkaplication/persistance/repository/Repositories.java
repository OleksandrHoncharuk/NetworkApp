package com.example.networkaplication.persistance.repository;

import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.example.networkaplication.persistance.repository.database.DatabaseRepository;
import com.example.networkaplication.persistance.repository.database.DatabaseRepositoryImpl;

public final class Repositories {

    private static DatabaseRepository databaseRepository;

    private Repositories() {
    }

    @MainThread
    public static void init(Context context) {
        databaseRepository = getDatabase(context);
    }

    @NonNull
    public static DatabaseRepository getDatabase(Context context) {
        if (databaseRepository == null) {
            databaseRepository = new DatabaseRepositoryImpl(context);
        }
        return databaseRepository;
    }

    @VisibleForTesting
    public static void setRoom(DatabaseRepository repository) {
        databaseRepository = repository;
    }
}

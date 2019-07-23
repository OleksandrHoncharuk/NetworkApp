package com.example.networkaplication.persistance.repository

import android.content.Context

import androidx.annotation.MainThread
import androidx.annotation.VisibleForTesting

import com.example.networkaplication.persistance.repository.database.DatabaseRepository
import com.example.networkaplication.persistance.repository.database.DatabaseRepositoryImpl

object Repositories {

    private var databaseRepository: DatabaseRepository? = null

    @MainThread
    fun init(context: Context) {
        databaseRepository = getDatabase(context)
    }

    fun getDatabase(context: Context): DatabaseRepository {
        if (databaseRepository == null) {
            databaseRepository = DatabaseRepositoryImpl(context)
        }
        return databaseRepository as DatabaseRepository
    }

    @VisibleForTesting
    fun setRoom(repository: DatabaseRepository) {
        databaseRepository = repository
    }
}

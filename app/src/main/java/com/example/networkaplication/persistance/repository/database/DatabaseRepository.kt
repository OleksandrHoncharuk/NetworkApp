package com.example.networkaplication.persistance.repository.database

import com.example.networkaplication.persistance.repository.database.dao.DetailsDao
import com.example.networkaplication.persistance.repository.database.dao.MovieQueryDao

interface DatabaseRepository {

    fun movieQueryDao(): MovieQueryDao

    fun detailsDao(): DetailsDao
}

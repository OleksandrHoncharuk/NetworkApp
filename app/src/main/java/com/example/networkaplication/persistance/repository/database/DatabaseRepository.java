package com.example.networkaplication.persistance.repository.database;

import com.example.networkaplication.persistance.repository.database.dao.DetailsDao;
import com.example.networkaplication.persistance.repository.database.dao.MovieQueryDao;

public interface DatabaseRepository {

    MovieQueryDao movieQueryDao ();

    DetailsDao detailsDao();
}

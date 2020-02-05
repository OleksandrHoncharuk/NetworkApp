package com.example.networkaplication.home;

import android.app.Application;

import com.example.networkaplication.persistance.model.MovieQuery;
import com.example.networkaplication.persistance.repository.Repositories;
import com.example.networkaplication.persistance.repository.database.DatabaseRepositoryImpl;
import com.example.networkaplication.persistance.repository.database.dao.MovieQueryDao;
import com.example.networkaplication.retrofit.NetworkService;

import java.util.List;

public class HomeRepositoryImpl implements HomeContract.HomeReposytory {
    private MovieQueryDao movieQueryDao;

    HomeRepositoryImpl(Application app) {
        DatabaseRepositoryImpl repository =
                (DatabaseRepositoryImpl) Repositories.getDatabase(app.getApplicationContext());

        movieQueryDao = repository.movieQueryDao();
    }

    @Override
    public void startSearch(String searchRequest, HomeContract.HomeView view) {
        NetworkService.getInstance()
                .getOMDBApi()
                .getSearchResult(searchRequest)
                .enqueue(new HomeViewFragment.SearchCallback(view));
    }

    @Override
    public List<MovieQuery> findAllFromQuery(String search) {
        return movieQueryDao.findAllLike(search);
    }

    @Override
    public MovieQuery findByTitle(String title) {
        return movieQueryDao.findByTitle(title);
    }

    @Override
    public void insertIntoQuery(MovieQuery query) {
        movieQueryDao.insert(query);
    }

}

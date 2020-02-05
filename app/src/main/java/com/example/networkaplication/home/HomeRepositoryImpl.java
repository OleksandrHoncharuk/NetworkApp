package com.example.networkaplication.home;

import android.app.Application;


import com.example.networkaplication.App;
import com.example.networkaplication.idling.EspressoIdlingResource;
import com.example.networkaplication.models.search.Search;
import com.example.networkaplication.persistance.model.MovieQuery;
import com.example.networkaplication.persistance.repository.Repositories;
import com.example.networkaplication.persistance.repository.database.DatabaseRepositoryImpl;
import com.example.networkaplication.persistance.repository.database.dao.MovieQueryDao;
import com.example.networkaplication.retrofit.OMDBApiInterface;
import com.example.networkaplication.retrofit.component.DaggerHomeComponent;
import com.example.networkaplication.retrofit.component.HomeComponent;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRepositoryImpl implements HomeContract.HomeRepository, Callback<Search> {
    private MovieQueryDao movieQueryDao;
    private HomeCallback callBack;

    @Inject
    OMDBApiInterface omdbApiInterface;

    HomeRepositoryImpl(Application app, HomeCallback callBack) {
        DatabaseRepositoryImpl repository = (DatabaseRepositoryImpl)
                Repositories.getDatabase(app.getApplicationContext());

        movieQueryDao = repository.movieQueryDao();
        this.callBack = callBack;

        HomeComponent component = DaggerHomeComponent.builder()
                .appComponent(((App) app).getComponent())
                .build();
        component.inject(this);
    }

    @Override
    public void startSearch(String searchRequest) {
        EspressoIdlingResource.increment();

        omdbApiInterface
                .getSearchResult(searchRequest)
                .enqueue(this);
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

    @Override
    public void onResponse(Call<Search> call, Response<Search> response) {
        Search search = response.body();

        if (search != null && search.getSearch() != null && search.getSearch().size() != 0) {
            callBack.onSearchReceived(search);
        }
    }

    @Override
    public void onFailure(Call<Search> call, Throwable t) {

    }
}

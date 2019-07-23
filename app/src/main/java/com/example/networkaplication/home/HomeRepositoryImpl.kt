package com.example.networkaplication.home

import android.app.Application

import com.example.networkaplication.App
import com.example.networkaplication.idling.EspressoIdlingResource
import com.example.networkaplication.models.search.Search
import com.example.networkaplication.persistance.model.MovieQuery
import com.example.networkaplication.persistance.repository.Repositories
import com.example.networkaplication.persistance.repository.database.DatabaseRepositoryImpl
import com.example.networkaplication.persistance.repository.database.dao.MovieQueryDao
import com.example.networkaplication.retrofit.OMDBApiInterface
import com.example.networkaplication.retrofit.component.DaggerHomeComponent

import javax.inject.Inject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepositoryImpl internal constructor(app: Application, private val callBack: HomeCallback) : HomeContract.HomeRepository, Callback<Search> {
    private val movieQueryDao: MovieQueryDao

    @Inject
    lateinit var omdbApiInterface: OMDBApiInterface

    init {
        val repository = Repositories.getDatabase(app.applicationContext) as DatabaseRepositoryImpl

        movieQueryDao = repository.movieQueryDao()

        val component = DaggerHomeComponent.builder()
                .appComponent((app as App).component)
                .build()
        component.inject(this)
    }

    override fun startSearch(searchRequest: String) {
        EspressoIdlingResource.increment()

        omdbApiInterface
                .getSearchResult(searchRequest)
                .enqueue(this)
    }

    override fun findAllFromQuery(search: String): List<MovieQuery> {
        return movieQueryDao.findAllLike(search)
    }

    override fun findByTitle(title: String): MovieQuery {
        return movieQueryDao.findByTitle(title)
    }

    override fun insertIntoQuery(query: MovieQuery) {
        movieQueryDao.insert(query)
    }

    override fun onResponse(call: Call<Search>, response: Response<Search>) {
        val search = response.body()

        if (search?.search != null && search.search!!.isNotEmpty()) {
            callBack.onSearchReceived(search)
        }
    }

    override fun onFailure(call: Call<Search>, t: Throwable) {

    }
}

package com.example.networkaplication.retrofit

import com.example.networkaplication.models.search.Search
import com.example.networkaplication.models.select.MovieDetail

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDBApiInterface {

    @GET("?&apikey=c1645dc5")
    fun getSearchResult(@Query("s") search: String): Call<Search>

    @GET("?&apikey=c1645dc5")
    fun getMovieDetails(@Query("t") movieName: String): Call<MovieDetail>
}

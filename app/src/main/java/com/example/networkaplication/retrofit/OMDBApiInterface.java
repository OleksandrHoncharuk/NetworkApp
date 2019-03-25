package com.example.networkaplication.retrofit;

import com.example.networkaplication.pojo.item.search.Search;
import com.example.networkaplication.pojo.item.select.MovieDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OMDBApiInterface {

    @GET("?&apikey=c1645dc5")
    Call<Search> getSearchResult (@Query("s") String search);

    @GET("?&apikey=c1645dc5")
    Call<MovieDetail> getMovieDetails (@Query("t") String movieName);
}

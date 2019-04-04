package com.example.networkaplication.retrofit;

import com.example.networkaplication.models.search.Search;
import com.example.networkaplication.models.select.MovieDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OMDBApiInterface {
    String API_KEY = "?&apikey=c1645dc5";

    @GET(API_KEY)
    Call<Search> getSearchResult(@Query("s") String search);

    @GET(API_KEY)
    Call<MovieDetail> getMovieDetails(@Query("t") String movieName);
}

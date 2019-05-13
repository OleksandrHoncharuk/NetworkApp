package com.example.networkaplication.details;

import com.example.networkaplication.models.select.MovieDetail;
import com.example.networkaplication.persistance.model.Details;

public interface DetailsCallback {
    void onMoviesReceived(MovieDetail movie);

    void onDetailsReceived(Details details);
}

package com.example.networkaplication.details

import com.example.networkaplication.models.select.MovieDetail
import com.example.networkaplication.persistance.model.Details

interface DetailsCallback {
    fun onMoviesReceived(movie: MovieDetail)

    fun onDetailsReceived(details: Details)
}

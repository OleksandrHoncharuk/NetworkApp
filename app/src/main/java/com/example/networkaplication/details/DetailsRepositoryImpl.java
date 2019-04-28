package com.example.networkaplication.details;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.example.networkaplication.models.select.MovieDetail;
import com.example.networkaplication.persistance.model.Details;
import com.example.networkaplication.persistance.repository.Repositories;
import com.example.networkaplication.persistance.repository.database.DatabaseRepositoryImpl;
import com.example.networkaplication.persistance.repository.database.dao.DetailsDao;
import com.example.networkaplication.retrofit.NetworkService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsRepositoryImpl implements DetailsContract.DetailsRepository, Callback<MovieDetail>{

    private DetailsDao detailsDao;
    private MovieDetail movie;
    private DetailsCallback callback;

    private static File root = Environment.getExternalStorageDirectory();


    public DetailsRepositoryImpl(Application app, DetailsCallback callback) {
        DatabaseRepositoryImpl repository =
                (DatabaseRepositoryImpl) Repositories.getDatabase(app.getApplicationContext());

        this.callback = callback;

        detailsDao = repository.detailsDao();
    }

    @Override
    public void saveImage(Bitmap imageToSave, String omdbId) {
        File file = new File(root, omdbId.trim() + ".PNG");

        try {
            FileOutputStream out = new FileOutputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageToSave.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            out.write(byteArrayOutputStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Uri loadImage(String omdbId) {
        File file = new File(root, omdbId.trim() + ".PNG");

        if (file.exists())
            return Uri.fromFile(file);

        return null;
    }

    @Override
    public void loadDetails(String filmName) {
        NetworkService.getInstance()
                .getOMDBApi()
                .getMovieDetails(filmName)
                .enqueue(this);
    }

    @Override
    public MovieDetail getMovie() {
        return this.movie;
    }

    @Override
    public Details getDetailFromId(String omdbId) {
        return detailsDao.findById(omdbId);
    }

    @Override
    public void onResponse(Call<MovieDetail> call, final Response<MovieDetail> response) {
        final MovieDetail movie = response.body();

        if (movie != null) {

            callback.onMoviesReceived(movie);
            String detailsLine = String
                    .format("%s %s %s %s/10 ", movie.getYear(), movie.getRuntime(),
                            movie.getCountry(), movie.getImdbRating());

            Details details = detailsDao.findByTitle(movie.getTitle());

            if (details == null) {
                Details details1 = new Details(movie.getImdbID());
                details1.setTitle(movie.getTitle());
                details1.setGenre(movie.getGenre());
                details1.setDetails(detailsLine);
                details1.setPlotSummary(movie.getPlot());
                callback.onDetailsReceived(details1);
                detailsDao.insert(details1);
            } else {
                details.setTitle(movie.getTitle());
                details.setDetails(detailsLine);
                details.setGenre(movie.getGenre());
                details.setPlotSummary(movie.getPlot());
                callback.onDetailsReceived(details);
                detailsDao.update(details);
            }
        }
    }

    @Override
    public void onFailure(Call<MovieDetail> call, Throwable t) {
        call.cancel();
    }
}

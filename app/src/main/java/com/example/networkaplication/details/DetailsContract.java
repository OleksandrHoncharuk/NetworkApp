package com.example.networkaplication.details;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.networkaplication.MainActivity;
import com.example.networkaplication.models.select.MovieDetail;
import com.example.networkaplication.persistance.model.Details;

public interface DetailsContract {

    interface DetailsView {

        Bitmap getImage();

        void setImage(Uri imageUri);

        void initDetails(Details details);

        ImageView getImageView();

        MainActivity getViewActivity();
    }

    interface DetailsPresenter {

        boolean onOptionItemSelected(MenuItem item);

        void saveImage(Bitmap imageToSave, String omdbId);

        void loadImage(String omdbId);

        void initView(String filmName);

        void initViewById(String omdbId);

        void onSaveImageClicked(String omdbId);
    }


    interface DetailsRepository {

        void saveImage(Bitmap imageToSave, String omdbId);

        Uri loadImage(String omdbId);

        void loadDetails(String filmName);

        MovieDetail getMovie();

        Details getDetailFromId(String omdbId);
    }
}

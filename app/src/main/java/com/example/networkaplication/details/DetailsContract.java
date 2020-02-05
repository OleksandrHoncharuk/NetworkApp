package com.example.networkaplication.details;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;
import com.example.networkaplication.persistance.model.Details;

public interface DetailsContract {

    interface DetailsView {

        Bitmap getImage();

        void setImage(Uri imageUri);

        void initDetails(Details details);

        ImageView getImageView();
      
        void saveIsOffline();

        void backClicked();

        void showWebView();

        void showBrowser();

        void showAlertDialog();
    }

    interface DetailsPresenter {
      
        boolean onOptionItemSelected(int itemId);

        void initView(String filmName);

        void initViewById(String omdbId);

        void onSaveImageClicked(String omdbId);

        boolean isOffline();
    }


    interface DetailsRepository {

        void saveImage(Bitmap imageToSave, String omdbId);

        Uri loadImage(String omdbId);

        void loadDetails(String filmName);

        Details getDetailFromId(String omdbId);
    }
}

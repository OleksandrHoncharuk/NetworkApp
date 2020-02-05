package com.example.networkaplication.details;

import android.Manifest;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.example.networkaplication.R;
import com.example.networkaplication.home.HomeViewFragment;
import com.example.networkaplication.models.select.MovieDetail;
import com.example.networkaplication.persistance.model.Details;
import com.example.networkaplication.retrofit.NetworkService;
import com.example.networkaplication.webview.WebViewFragment;

public class DetailsPresenterImpl extends AndroidViewModel implements DetailsContract.DetailsPresenter, DetailsCallback{

    private MutableLiveData<Boolean> showProgress = new MutableLiveData<>();
    private DetailsContract.DetailsRepository repository;
    private DetailsContract.DetailsView view;
    private MovieDetail movieDetail;
    private Details details;

    public DetailsPresenterImpl(@NonNull Application application, DetailsContract.DetailsView view) {
        super(application);
        this.view = view;
        repository = new DetailsRepositoryImpl(application, this);
    }

    @Override
    public boolean onOptionItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backClicked();
                return true;

            case R.id.web_view_item:
                showWebView();
                return true;

            case R.id.browser:
                showBrowser();
                return true;

            case R.id.alert_dialog:
                showAlertDialog();
                return true;

            case R.id.offline:
                saveIsOffline(item);
                return true;

                default:
                    return false;
        }
    }

    @Override
    public void initView(String filmName) {
        repository.loadDetails(filmName);
/*
                Glide
                .with(view.getViewActivity())
                .load(movieDetail.getPoster())
                .into(view.getImageView());

        String detailsLine = String
                .format("%s %s %s %s/10 ", movieDetail.getYear(), movieDetail.getRuntime(),
                        movieDetail.getCountry(), movieDetail.getImdbRating());

        Details details = new Details(movieDetail.getImdbID());
        details.setTitle(movieDetail.getTitle());
        details.setGenre(movieDetail.getGenre());
        details.setDetails(detailsLine);
        details.setPlotSummary(movieDetail.getPlot());

        view.initDetails(details);*/
    }

    @Override
    public void initViewById(String omdbId) {
        view.setImage(repository.loadImage(omdbId));
        repository.getDetailFromId(omdbId);
    }

    @Override
    public void onSaveImageClicked(String omdbId) {
        if (ContextCompat.checkSelfPermission(view.getViewActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(view.getViewActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(view.getViewActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        } else {
            saveImage(view.getImage(), omdbId);
        }
    }

    @Override
    public void onMoviesReceived(MovieDetail movies) {
        movieDetail = movies;
        Glide
                .with(view.getViewActivity())
                .load(movieDetail.getPoster())
                .into(view.getImageView());

        String detailsLine = String
                .format("%s %s %s %s/10 ", movieDetail.getYear(), movieDetail.getRuntime(),
                        movieDetail.getCountry(), movieDetail.getImdbRating());

        Details details = new Details(movieDetail.getImdbID());
        details.setTitle(movieDetail.getTitle());
        details.setGenre(movieDetail.getGenre());
        details.setDetails(detailsLine);
        details.setPlotSummary(movieDetail.getPlot());

        view.initDetails(details);
    }

    @Override
    public void onDetailsReceived(Details details) {
        this.details = details;
        view.initDetails(details);
    }

    private void backClicked() {

        HomeViewFragment.setIsSearchViewPopped(false);
        view.getViewActivity().onBackPressed();
    }

    private void showWebView() {
        FragmentTransaction transaction =
                view.getViewActivity().getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack("").replace(R.id.RelativeForFragments,
                WebViewFragment.newInstance(NetworkService.BASE_URL),
                WebViewFragment.class.getSimpleName());
        transaction.commit();
    }

    private void showBrowser() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NetworkService.BASE_URL));
        view.getViewActivity().startActivity(browserIntent);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog
                .Builder(view.getViewActivity());

        builder.setTitle("Very dump message!")
                .setMessage("This...is..ALERT DIALOG!")
                .setCancelable(false)
                .setNegativeButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        final AlertDialog alert = builder.create();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alert.show();
            }
        }, 5000);
    }

    private void saveIsOffline(MenuItem item) {
//        if (sharedPreferences != null) {
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean(omdbId, true);
//            editor.apply();
//        }
//
//        isOffline = !isOffline;
//        item.setChecked(isOffline);
    }

    @Override
    public void saveImage(Bitmap imageToSave, String omdbId) {
        repository.saveImage(imageToSave, omdbId);
    }

    @Override
    public void loadImage(String omdbId) {
        view.setImage(repository.loadImage(omdbId));
    }
}

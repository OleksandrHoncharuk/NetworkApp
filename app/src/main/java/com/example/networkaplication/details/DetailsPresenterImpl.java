package com.example.networkaplication.details;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.networkaplication.R;
import com.example.networkaplication.models.select.MovieDetail;
import com.example.networkaplication.persistance.model.Details;

public class DetailsPresenterImpl extends AndroidViewModel implements DetailsContract.DetailsPresenter, DetailsCallback {

    private DetailsContract.DetailsRepository repository;
    private DetailsContract.DetailsView view;
    private Details details;
    private boolean isOffline = false;

    DetailsPresenterImpl(@NonNull Application app, DetailsContract.DetailsView view) {
        super(app);
        this.view = view;
        repository = new DetailsRepositoryImpl(app, this);
    }

    @Override
    public boolean onOptionItemSelected(int itemId) {
        switch (itemId) {
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
                saveIsOffline();
                return true;

                default:
                    return false;
        }
    }

    @Override
    public void initView(String filmName) {
        repository.loadDetails(filmName);
    }

    @Override
    public void initViewById(String omdbId) {
        view.setImage(repository.loadImage(omdbId));
        view.initDetails(repository.getDetailFromId(omdbId));
    }

    @Override
    public void onSaveImageClicked(String omdbId) {
        repository.saveImage(view.getImage(), omdbId);
    }

    @Override
    public void onMoviesReceived(MovieDetail movies) {
        setImage(movies.getPoster());

        details = new Details(movies.getImdbID());
        details.setTitle(movies.getTitle());
        details.setGenre(movies.getGenre());

        String detailsLine = String
                .format("%s %s %s %s/10 ", movies.getYear(), movies.getRuntime(),
                        movies.getCountry(), movies.getImdbRating());
        details.setDetails(detailsLine);
        details.setPlotSummary(movies.getPlot());

        view.initDetails(details);
    }

    private void setImage(String poster) {
        Glide
                .with(view.getImageView().getContext())
                .load(poster)
                .apply(new RequestOptions().optionalTransform(new CropSquareTransformation()))
                .into(view.getImageView());
    }

    @Override
    public void onDetailsReceived(Details details) {
        this.details = details;
        view.initDetails(details);
    }

    private void backClicked() {
        view.backClicked();
    }

    private void showWebView() {
        view.showWebView();
    }

    private void showBrowser() {
        view.showBrowser();
    }

    private void showAlertDialog() {
        view.showAlertDialog();
    }

    private void saveIsOffline() {
        isOffline = !isOffline;
        view.saveIsOffline();
    }

    @Override
    public boolean isOffline() {
        return isOffline;
    }
}

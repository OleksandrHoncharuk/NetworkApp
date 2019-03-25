package com.example.networkaplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.networkaplication.pojo.item.select.MovieDetail;
import com.example.networkaplication.retrofit.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsFragment extends Fragment {
    String filmName;
    ImageView poster;
    TextView title;
    TextView details;
    TextView genre;
    TextView plotSummary;

    public static DetailsFragment newInstance (String filmName) {
        DetailsFragment fragment = new DetailsFragment();
        fragment.setFilmName(filmName);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.details_fragment, container, false);

        poster = rootView.findViewById(R.id.detail_poster);
        title = rootView.findViewById(R.id.film_name);
        details = rootView.findViewById(R.id.details);
        genre = rootView.findViewById(R.id.genre);
        plotSummary = rootView.findViewById(R.id.plot_summary);

        NetworkService.getInstance()
                .getOMDBApi()
                .getMovieDetails(filmName)
                .enqueue(new Callback<MovieDetail>() {
                    @Override
                    public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                        MovieDetail movie = response.body();

                        if (movie != null) {
                            Glide
                                    .with(container.getContext())
                                    .load(movie.getPoster())
                                    .into(poster);

                            title.setText(movie.getTitle());
                            details.setText(movie.getYear() + " " + movie.getRuntime() + " "
                                    + movie.getCountry() + " " + movie.getImdbRating() + "/10 ");
                            genre.setText(movie.getGenre());
                            plotSummary.setText(movie.getPlot());
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDetail> call, Throwable t) {
                        call.cancel();
                    }
                });

        return rootView;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Details");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;

            case R.id.web_view_item:
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.addToBackStack("").replace(R.id.RelativeForFragments,
                        WebViewFragment.newInstance("http://omdbapi.com/"), "WEV_VIEW_FRAGMENT");
                transaction.commit();
                return true;

            case R.id.browser:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://omdbapi.com/"));
                startActivity(browserIntent);
                return true;

            case R.id.alert_dialog:
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
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

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

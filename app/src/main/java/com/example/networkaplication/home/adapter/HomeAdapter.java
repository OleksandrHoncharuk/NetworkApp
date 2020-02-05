package com.example.networkaplication.home.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.networkaplication.R;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private final ListPresenter presenter;
    private OnFilmClickedListener onFilmClickedListener;

    public HomeAdapter(ListPresenter presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HomeViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_layout, null));
    }

    public void setOnFilmClickedListener(OnFilmClickedListener onFilmClickedListener) {
        this.onFilmClickedListener = onFilmClickedListener;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder viewHolder, int i) {
        presenter.onBindRowViewAtPosition(viewHolder, i);
    }

    @Override
    public int getItemCount() {
        return presenter.getRowsCount();
    }

    public interface OnFilmClickedListener {
        void onClicked(ItemData itemData);
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements RowView, View.OnClickListener {
        final ImageView image;
        final TextView title;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.film_poster);
            title = itemView.findViewById(R.id.film_title);
        }

        @Override
        public void setImage(String imageUrl) {
            Glide.with(itemView)
                    .load(imageUrl)
                    .into(image);
        }

        @Override
        public void setTitle(String title) {
            this.title.setText(title);
        }

        @Override
        public void onClick(View v) {
            onFilmClickedListener.onClicked(presenter.get(getAdapterPosition()));
        }
    }

}

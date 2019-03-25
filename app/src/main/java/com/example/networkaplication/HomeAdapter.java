package com.example.networkaplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private ArrayList<ItemData> items;
    private OnFilmClicked onFilmClicked;

    HomeAdapter (List<ItemData> data) {
        items = new ArrayList<>(data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView title;

        ViewHolder (View itemLayoutView) {
            super(itemLayoutView);

            itemLayoutView.setOnClickListener(this);
            image = itemLayoutView.findViewById(R.id.film_poster);
            title = itemLayoutView.findViewById(R.id.film_title);
        }

        @Override
        public void onClick(View v) {
            onFilmClicked.onClicked(items.get(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_layout, null);

        return new ViewHolder(itemLayoutView);
    }

    void setOnFilmClicked(OnFilmClicked onFilmClicked) {
        this.onFilmClicked = onFilmClicked;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Glide.with(viewHolder.itemView)
                .load(items.get(i).getImageUrl())
                .into(viewHolder.image);

        viewHolder.title.setText(items.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnFilmClicked {
        void onClicked (ItemData itemData);
    }
}

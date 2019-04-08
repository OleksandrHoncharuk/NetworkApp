package com.example.networkaplication.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.networkaplication.R;

import java.util.ArrayList;
import java.util.List;

public class SearchStoryAdapter extends RecyclerView.Adapter<SearchStoryAdapter.ViewHolder> {

    private ArrayList<SearchItem> items;
    private OnSearchItemClickedListener onSearchItemClickedListener;

    SearchStoryAdapter(List<SearchItem> data) {
        items = new ArrayList<>(data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView searchStory;
        final ImageView remove;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            itemLayoutView.setOnClickListener(this);
            searchStory = itemLayoutView.findViewById(R.id.search_story);
            remove = itemLayoutView.findViewById(R.id.delete_query);
            remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           onSearchItemClickedListener.onClickedSearchItem(v, items.get(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_item_layout, null);

        return new ViewHolder(itemLayoutView);
    }

    void setOnSearchItemClickedListener(OnSearchItemClickedListener onSearchItemClickedListener) {
        this.onSearchItemClickedListener = onSearchItemClickedListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.searchStory.setText(items.get(i).getSearchText());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnSearchItemClickedListener {
        void onClickedSearchItem(View view, SearchItem itemData);
    }

    public void refresh (ArrayList<SearchItem> items) {
        this.items = items;
    }
}
package com.example.networkaplication.home.search.story;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.networkaplication.R;

import java.util.ArrayList;

public class SearchStoryAdapter extends RecyclerView.Adapter<SearchStoryAdapter.SearchStoryViewHolder> {

    private SearchStoryPresenter presenter;
    private OnSearchItemClickedListener onSearchItemClickedListener;

    public SearchStoryAdapter(SearchStoryPresenter presenter) {
        this.presenter = presenter;
    }

    public class SearchStoryViewHolder extends RecyclerView.ViewHolder implements SearchStoryRowView, View.OnClickListener {
        final TextView searchStory;
        final ImageView remove;

        public SearchStoryViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            searchStory = itemView.findViewById(R.id.search_story);
            remove = itemView.findViewById(R.id.delete_query);
            remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onSearchItemClickedListener.onClickedSearchItem(v, presenter.getSearchItem(getAdapterPosition()));
        }

        @Override
        public void setSearchStoryTest(String text) {
            searchStory.setText(text);
        }
    }

    @NonNull
    @Override
    public SearchStoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SearchStoryViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_item_layout, null));
    }

    public void setOnSearchItemClickedListener(OnSearchItemClickedListener onSearchItemClickedListener) {
        this.onSearchItemClickedListener = onSearchItemClickedListener;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchStoryViewHolder viewHolder, int i) {
        presenter.onBindSearchStoryRowAtPosition(viewHolder, i);
    }

    @Override
    public int getItemCount() {
        return presenter.getSearchRowCount();
    }

    public interface OnSearchItemClickedListener {
        void onClickedSearchItem(View view, SearchItem itemData);
    }

    public void refresh (ArrayList<SearchItem> items) {
        presenter.refreshSearch(items);
    }
}
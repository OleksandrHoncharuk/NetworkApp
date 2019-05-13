package com.example.networkaplication.home.adapter;

public class ItemData {
    private String imageUrl;
    private String title;
    private String omdbId;

    public ItemData(String imageUrl, String title, String omdbId) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.omdbId = omdbId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOmdbId() {
        return omdbId;
    }

    public void setOmdbId(String omdbId) {
        this.omdbId = omdbId;
    }
}

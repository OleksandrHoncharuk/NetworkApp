package com.example.networkaplication.models.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Search {
    @SerializedName("Search")
    @Expose
    private List<SearchObject> search = null;
    @SerializedName("totalResults")
    @Expose
    private String totalResults;
    @SerializedName("Response")
    @Expose
    private String response;

    public List<SearchObject> getSearch() {
        return search;
    }

    public void setSearch(List<SearchObject> search) {
        this.search = search;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}

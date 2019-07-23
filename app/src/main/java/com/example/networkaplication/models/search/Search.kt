package com.example.networkaplication.models.search

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Search {
    @SerializedName("Search")
    @Expose
    var search: List<SearchObject>? = null
    @SerializedName("totalResults")
    @Expose
    var totalResults: String? = null
    @SerializedName("Response")
    @Expose
    var response: String? = null
}

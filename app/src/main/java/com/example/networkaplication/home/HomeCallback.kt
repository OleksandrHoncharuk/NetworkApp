package com.example.networkaplication.home

import com.example.networkaplication.models.search.Search

interface HomeCallback {
    fun onSearchReceived(search: Search)
}

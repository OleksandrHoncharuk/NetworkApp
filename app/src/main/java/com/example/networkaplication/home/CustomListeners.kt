package com.example.networkaplication.home

import android.view.View
import android.widget.TextView

interface CustomOnFocusChangeListener {
    fun onFocusChanged(view: View, hasFocus: Boolean)
}

interface CustomOnEditorActionListener {
    fun onEditorAction(v: TextView?, actionId: Int)
}
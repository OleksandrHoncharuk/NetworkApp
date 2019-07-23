package com.example.networkaplication

import android.app.Application

import com.example.networkaplication.persistance.repository.Repositories
import com.example.networkaplication.retrofit.component.AppComponent
import com.example.networkaplication.retrofit.component.DaggerAppComponent

class App : Application() {
    var component: AppComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent.builder().build()
        component!!.inject(this)

        Repositories.init(this)
    }
}


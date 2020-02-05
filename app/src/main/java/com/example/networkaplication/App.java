package com.example.networkaplication;

import android.app.Application;

import com.example.networkaplication.persistance.repository.Repositories;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Repositories.init(this);
    }
}

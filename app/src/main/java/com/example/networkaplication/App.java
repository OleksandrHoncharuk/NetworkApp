package com.example.networkaplication;

import android.app.Application;

import com.example.networkaplication.persistance.repository.Repositories;
import com.example.networkaplication.retrofit.component.AppComponent;
import com.example.networkaplication.retrofit.component.DaggerAppComponent;


public class App extends Application {
    private AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder().build();
        component.inject(this);

        Repositories.init(this);
    }

    public AppComponent getComponent() {
        return component;
    }
}

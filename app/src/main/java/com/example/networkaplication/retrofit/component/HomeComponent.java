package com.example.networkaplication.retrofit.component;

import com.example.networkaplication.home.HomeRepositoryImpl;

import dagger.Component;

@RepositoryScope
@Component(dependencies = AppComponent.class)
public interface HomeComponent {
    void inject(HomeRepositoryImpl repository);
}

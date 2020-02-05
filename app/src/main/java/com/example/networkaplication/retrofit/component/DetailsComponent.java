package com.example.networkaplication.retrofit.component;

import com.example.networkaplication.details.DetailsRepositoryImpl;

import dagger.Component;

@RepositoryScope
@Component(dependencies = AppComponent.class)
public interface DetailsComponent {
    void inject(DetailsRepositoryImpl repository);
}

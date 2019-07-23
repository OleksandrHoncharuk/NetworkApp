package com.example.networkaplication.retrofit.component

import com.example.networkaplication.home.HomeRepositoryImpl

import dagger.Component

@RepositoryScope
@Component(dependencies = [AppComponent::class])
interface HomeComponent {
    fun inject(repository: HomeRepositoryImpl)
}

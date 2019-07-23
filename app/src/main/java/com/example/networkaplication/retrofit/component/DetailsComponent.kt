package com.example.networkaplication.retrofit.component

import com.example.networkaplication.details.DetailsRepositoryImpl

import dagger.Component

@RepositoryScope
@Component(dependencies = [AppComponent::class])
interface DetailsComponent {
    fun inject(repository: DetailsRepositoryImpl)
}

package com.example.networkaplication.retrofit.component


import com.example.networkaplication.App
import com.example.networkaplication.retrofit.OMDBApiInterface
import com.example.networkaplication.retrofit.RetrofitModule

import dagger.Component

@AppScope
@Component(modules = [RetrofitModule::class])
interface AppComponent {
    val omdbApi: OMDBApiInterface

    fun inject(app: App)
}

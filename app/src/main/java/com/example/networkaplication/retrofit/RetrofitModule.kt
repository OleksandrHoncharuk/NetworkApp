package com.example.networkaplication.retrofit

import com.example.networkaplication.retrofit.component.AppScope

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module(includes = [OkHttpClientModule::class])
class RetrofitModule {

    @Module
    companion object {
        val BASE_URL = "http://www.omdbapi.com/"

        @JvmStatic
        @AppScope
        @Provides
        fun omdbApi(retrofit: Retrofit): OMDBApiInterface {
            return retrofit.create(OMDBApiInterface::class.java)
        }

        @JvmStatic
        @AppScope
        @Provides
        fun retrofit(client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
        }
    }
}

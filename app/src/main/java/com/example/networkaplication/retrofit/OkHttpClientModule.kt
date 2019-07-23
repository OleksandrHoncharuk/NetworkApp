package com.example.networkaplication.retrofit

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
class OkHttpClientModule {

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun interceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            return interceptor
        }

        @JvmStatic
        @Provides
        fun okHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
            val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)

            return client.build()
        }
    }
}

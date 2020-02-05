package com.example.networkaplication.retrofit;

import com.example.networkaplication.retrofit.component.AppScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = OkHttpClientModule.class)
public class RetrofitModule {
    public final static String BASE_URL = "http://www.omdbapi.com/";

    @AppScope
    @Provides
    public OMDBApiInterface omdbApi(Retrofit retrofit) {
        return retrofit.create(OMDBApiInterface.class);
    }

    @AppScope
    @Provides
    public Retrofit retrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
}

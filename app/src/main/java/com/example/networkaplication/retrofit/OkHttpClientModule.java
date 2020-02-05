package com.example.networkaplication.retrofit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class OkHttpClientModule {

    @Provides
    public HttpLoggingInterceptor interceptor() {
       HttpLoggingInterceptor interceptor= new HttpLoggingInterceptor();
       interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

       return interceptor;
    }

    @Provides
    public OkHttpClient okHttpClient(HttpLoggingInterceptor interceptor) {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        return client.build();
    }
}

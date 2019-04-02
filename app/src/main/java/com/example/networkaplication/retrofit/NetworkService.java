package com.example.networkaplication.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService service;
    public final static String BASE_URL = "http://www.omdbapi.com/";
    private final Retrofit retrofit;

    private NetworkService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(interceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static NetworkService getInstance() {
        if (service == null)
            service = new NetworkService();

        return service;
    }

    public OMDBApiInterface getOMDBApi() {
        return retrofit.create(OMDBApiInterface.class);
    }
}

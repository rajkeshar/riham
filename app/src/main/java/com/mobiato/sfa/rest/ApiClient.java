package com.mobiato.sfa.rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobiato.sfa.App;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static String urlEndpoint = App.BASE_URL;

    private static Retrofit retrofit = null;
    private static OkHttpClient client = null;
    private static HttpLoggingInterceptor interceptor = null;
    private static ApiInterface service;

    public static Retrofit getClient() {


        if (retrofit == null) {
            if (interceptor == null) {
                interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            }
            if (client == null) {
                client = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.MINUTES)
                        .readTimeout(10, TimeUnit.MINUTES)
                        .writeTimeout(10, TimeUnit.MINUTES)
                        .retryOnConnectionFailure(true)
                        .followSslRedirects(true)
                        .addInterceptor(interceptor)
                        .build();
            }
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(urlEndpoint)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }

    public static ApiInterface getService() {
        if (service == null) {
            service = getClient().create(ApiInterface.class);
        }
        return service;
    }


}

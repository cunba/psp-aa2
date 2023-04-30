package com.sanvalero.aa2.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sanvalero.aa2.model.Artist;
import com.sanvalero.aa2.model.Artwork;
import com.sanvalero.aa2.model.Exhibition;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArtService {
    private static final String BASE_URL = "https://api.artic.edu/api/v1";
    private ArtAPI api;

    public ArtService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Gson gsonParser = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gsonParser))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.api = retrofit.create(ArtAPI.class);
    }

    public Observable<Artwork> getAllArtworks() {
        return this.api.getAllArtworks().flatMapIterable(artworks -> artworks);
    }

    public Observable<Artwork> getArtworkById(int id) {
        return this.api.getArtworkById(id);
    }

    public Observable<Artist> getAllArtists() {
        return this.api.getAllArtists().flatMapIterable(artists -> artists);
    }

    public Observable<Artist> getArtistsById(int id) {
        return this.api.getArtistById(id);
    }

    public Observable<Exhibition> getAllExhibitions() {
        return this.api.getAllExhibitions().flatMapIterable(exhibitions -> exhibitions);
    }

    public Observable<Exhibition> getExhibitionById(int id) {
        return this.api.getExhibitionById(id);
    }
}

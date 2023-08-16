package com.svalero.aa2.service;

import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.svalero.aa2.model.Artist;
import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.ArtworkType;
import com.svalero.aa2.model.Exhibition;
import com.svalero.aa2.model.Gallery;
import com.svalero.aa2.model.Image;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.model.ResponsePaginated;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArtService {
    private static final String BASE_URL = "https://api.artic.edu";
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

    public Observable<ResponsePaginated<Artwork>> getAllArtworks() {
        return this.api.getAllArtworks();
    }

    public Observable<ResponsePaginated<Artwork>> getAllArtworksPage(int page) {
        return this.api.getAllArtworksPage(page);
    }

    public Observable<Response<Artwork>> getArtworkById(int id) {
        return this.api.getArtworkById(id);
    }

    public Observable<ResponsePaginated<Artist>> getAllArtists() {
        return this.api.getAllArtists();
    }

    public Observable<ResponsePaginated<Artist>> getAllArtistsPage(int page) {
        return this.api.getAllArtistsPage(page);
    }

    public Observable<Response<Artist>> getArtistById(int id) {
        return this.api.getArtistById(id);
    }

    public Observable<ResponsePaginated<Exhibition>> getAllExhibitions() {
        return this.api.getAllExhibitions();
    }

    public Observable<ResponsePaginated<Exhibition>> getAllExhibitionsPage(int page) {
        return this.api.getAllExhibitionsPage(page);
    }

    public Observable<Response<Exhibition>> getExhibitionById(int id) {
        return this.api.getExhibitionById(id);
    }

    public Observable<ResponsePaginated<ArtworkType>> getAllArtworkTypes() {
        return this.api.getAllArtworkTypes();
    }

    public Observable<ResponsePaginated<ArtworkType>> getAllArtworkTypesPage(int page) {
        return this.api.getAllArtworkTypesPage(page);
    }

    public Observable<Response<Image>> getImageById(UUID id) {
        return this.api.getImageById(id);
    }

    public Observable<ResponsePaginated<Gallery>> getAllGalleries() {
        return this.api.getAllGalleries();
    }

    public Observable<ResponsePaginated<Gallery>> getAllGalleriesPage(int page) {
        return this.api.getAllGalleriesPage(page);
    }
}

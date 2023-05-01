package com.svalero.aa2.service;

import com.svalero.aa2.model.Artist;
import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.ArtworkType;
import com.svalero.aa2.model.Exhibition;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.model.ResponsePaginated;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ArtAPI {

    // ARTWORK

    @GET("/api/v1/artworks")
    Observable<ResponsePaginated<Artwork>> getAllArtworks();

    @GET("/api/v1/artworks/{id}")
    Observable<Response<Artwork>> getArtworkById(@Path("id") int id);

    // ARTIST

    @GET("/api/v1/artists")
    Observable<ResponsePaginated<Artist>> getAllArtists();

    @GET("/api/v1/artists/{id}")
    Observable<Response<Artist>> getArtistById(@Path("id") int id);

    // EXHIBITION

    @GET("/api/v1/exhibitions")
    Observable<ResponsePaginated<Exhibition>> getAllExhibitions();

    @GET("/api/v1/exhibitions/{id}")
    Observable<Response<Exhibition>> getExhibitionById(@Path("id") int id);

    // ARTWORK TYPE

    @GET("/api/v1/artwork-types")
    Observable<ResponsePaginated<ArtworkType>> getAllArtworkTypes();

    @GET("/api/v1/artwork-types/{id}")
    Observable<Response<ArtworkType>> getArtworkTypeById(@Path("id") int id);

}

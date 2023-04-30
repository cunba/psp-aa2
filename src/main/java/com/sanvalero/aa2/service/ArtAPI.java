package com.sanvalero.aa2.service;

import java.util.List;

import com.sanvalero.aa2.model.Artist;
import com.sanvalero.aa2.model.Artwork;
import com.sanvalero.aa2.model.Exhibition;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ArtAPI {

    // ARTWORK

    @GET("/artworks")
    Observable<List<Artwork>> getAllArtworks();

    @GET("/artworks/{id}")
    Observable<Artwork> getArtworkById(@Path("id") int id);
    


    // ARTIST

    @GET("/artists")
    Observable<List<Artist>> getAllArtists();

    @GET("/artists/{id}")
    Observable<Artist> getArtistById(@Path("id") int id);



    // EXHIBITION

    @GET("/exhibitions")
    Observable<List<Exhibition>> getAllExhibitions();

    @GET("/exhibitions/{id}")
    Observable<Exhibition> getExhibitionById(@Path("id") int id);
}

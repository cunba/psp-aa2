package com.svalero.aa2.task;

import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class ArtworkTaskPage extends Task<Integer> {
    private Consumer<ResponsePaginated<Artwork>> consumer;
    private Consumer<Throwable> throwable;
    private int page;

    public ArtworkTaskPage(int page, Consumer<ResponsePaginated<Artwork>> consumer, Consumer<Throwable> throwable) {
        this.consumer = consumer;
        this.throwable = throwable;
        this.page = page;
    }

    @Override
    protected Integer call() throws Exception {
        ArtService artService = new ArtService();
        artService.getAllArtworksPage(page).subscribe(consumer, throwable);

        return null;
    }
}

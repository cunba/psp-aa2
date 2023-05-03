package com.svalero.aa2.task;

import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class ArtworkTask extends Task<Integer> {
    private Consumer<ResponsePaginated<Artwork>> consumer;
    private Consumer<Throwable> throwable;

    public ArtworkTask(Consumer<ResponsePaginated<Artwork>> consumer, Consumer<Throwable> throwable) {
        this.consumer = consumer;
        this.throwable = throwable;
    }

    @Override
    protected Integer call() throws Exception {
        ArtService artService = new ArtService();
        artService.getAllArtworks().subscribe(consumer, throwable);

        return null;
    }
}

package com.svalero.aa2.task;

import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class ArtworkTaskById extends Task<Integer> {
    private int requestedId;
    private Consumer<Response<Artwork>> consumer;
    private Consumer<Throwable> throwable;

    public ArtworkTaskById(int requestedId, Consumer<Response<Artwork>> consumer, Consumer<Throwable> throwable) {
        this.requestedId = requestedId;
        this.consumer = consumer;
        this.throwable = throwable;
    }

    @Override
    protected Integer call() throws Exception {
        ArtService artService = new ArtService();
        artService.getArtworkById(requestedId).subscribe(consumer, throwable);

        return null;
    }
}

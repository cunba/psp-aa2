package com.svalero.aa2.task;

import com.svalero.aa2.model.Artist;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class ArtistTaskById extends Task<Integer> {
    private int requestedId;
    private Consumer<Response<Artist>> consumer;
    private Consumer<Throwable> throwable;

    public ArtistTaskById(int requestedId, Consumer<Response<Artist>> consumer,
            Consumer<Throwable> throwable) {
        this.requestedId = requestedId;
        this.consumer = consumer;
        this.throwable = throwable;
    }

    @Override
    protected Integer call() throws Exception {
        ArtService artService = new ArtService();
        artService.getArtistById(requestedId).subscribe(consumer, throwable);

        return null;
    }
}

package com.svalero.aa2.task;

import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class ArtworkTask extends Task<Integer> {
    private int requestedId;
    private Consumer<ResponsePaginated<Artwork>> consumer;
    private Consumer<Response<Artwork>> consumerById;
    private Consumer<Throwable> throwable;

    public ArtworkTask(int requestedId, Consumer<ResponsePaginated<Artwork>> consumer,
            Consumer<Response<Artwork>> consumerById, Consumer<Throwable> throwable) {
        this.requestedId = requestedId;
        this.consumer = consumer;
        this.consumerById = consumerById;
        this.throwable = throwable;
    }

    @Override
    protected Integer call() throws Exception {
        ArtService artService = new ArtService();
        artService.getAllArtworks().subscribe(consumer, throwable);

        return null;
    }

    protected Integer callById() throws Exception {
        ArtService artService = new ArtService();
        artService.getArtworkById(requestedId).subscribe(consumerById, throwable);
        
        return null;
    }
}

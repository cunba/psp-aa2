package com.svalero.aa2.task;

import com.svalero.aa2.model.Exhibition;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class ExhibitionTaskById extends Task<Integer> {
    private int requestedId;
    private Consumer<Response<Exhibition>> consumer;
    private Consumer<Throwable> throwable;

    public ExhibitionTaskById(int requestedId, Consumer<Response<Exhibition>> consumer,
            Consumer<Throwable> throwable) {
        this.requestedId = requestedId;
        this.consumer = consumer;
        this.throwable = throwable;
    }

    @Override
    protected Integer call() throws Exception {
        ArtService artService = new ArtService();
        artService.getExhibitionById(requestedId).subscribe(consumer, throwable);

        return null;
    }
}

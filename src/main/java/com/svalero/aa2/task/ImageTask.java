package com.svalero.aa2.task;

import java.util.UUID;

import com.svalero.aa2.model.Image;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class ImageTask extends Task<Integer> {
    private UUID requestedId;
    private Consumer<Response<Image>> consumer;
    private Consumer<Throwable> throwable;

    public ImageTask(UUID requestedId, Consumer<Response<Image>> consumer,
            Consumer<Throwable> throwable) {
        this.requestedId = requestedId;
        this.consumer = consumer;
        this.throwable = throwable;
    }

    @Override
    protected Integer call() throws Exception {
        ArtService artService = new ArtService();
        artService.getImageById(requestedId).subscribe(consumer, throwable);

        return null;
    }
}

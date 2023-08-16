package com.svalero.aa2.task;

import com.svalero.aa2.model.ArtworkType;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class ArtworkTypeTask extends Task<Integer> {
    private Consumer<ResponsePaginated<ArtworkType>> consumer;
    private Consumer<Throwable> throwable;

    public ArtworkTypeTask(Consumer<ResponsePaginated<ArtworkType>> consumer, Consumer<Throwable> throwable) {
        this.consumer = consumer;
        this.throwable = throwable;
    }

    @Override
    protected Integer call() throws Exception {
        ArtService artService = new ArtService();
        artService.getAllArtworkTypes().subscribe(consumer, throwable);

        return null;
    }
}

package com.svalero.aa2.task;

import com.svalero.aa2.model.Gallery;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class GalleryTask extends Task<Integer> {
    private Consumer<ResponsePaginated<Gallery>> consumer;
    private Consumer<Throwable> throwable;

    public GalleryTask(Consumer<ResponsePaginated<Gallery>> consumer, Consumer<Throwable> throwable) {
        this.consumer = consumer;
        this.throwable = throwable;
    }

    @Override
    protected Integer call() throws Exception {
        ArtService artService = new ArtService();
        artService.getAllGalleries().subscribe(consumer, throwable);

        return null;
    }
}

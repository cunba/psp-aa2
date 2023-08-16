package com.svalero.aa2.task;

import com.svalero.aa2.model.Gallery;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class GalleryTaskPage extends Task<Integer> {
    private Consumer<ResponsePaginated<Gallery>> consumer;
    private Consumer<Throwable> throwable;
    private int page;

    public GalleryTaskPage(int page, Consumer<ResponsePaginated<Gallery>> consumer,
            Consumer<Throwable> throwable) {
        this.consumer = consumer;
        this.throwable = throwable;
        this.page = page;
    }

    @Override
    protected Integer call() throws Exception {
        ArtService artService = new ArtService();
        artService.getAllGalleriesPage(page).subscribe(consumer, throwable);

        return null;
    }
}

package com.svalero.aa2.task;

import com.svalero.aa2.model.Exhibition;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class ExhibitionTaskPage extends Task<Integer> {
    private Consumer<ResponsePaginated<Exhibition>> consumer;
    private Consumer<Throwable> throwable;
    private int page;

    public ExhibitionTaskPage(int page, Consumer<ResponsePaginated<Exhibition>> consumer,
            Consumer<Throwable> throwable) {
        this.consumer = consumer;
        this.throwable = throwable;
        this.page = page;
    }

    @Override
    protected Integer call() throws Exception {
        ArtService artService = new ArtService();
        artService.getAllExhibitionsPage(page).subscribe(consumer, throwable);

        return null;
    }
}

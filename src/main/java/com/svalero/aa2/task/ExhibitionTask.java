package com.svalero.aa2.task;

import com.svalero.aa2.model.Exhibition;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class ExhibitionTask extends Task<Integer> {
    private Consumer<ResponsePaginated<Exhibition>> consumer;
    private Consumer<Throwable> throwable;

    public ExhibitionTask(Consumer<ResponsePaginated<Exhibition>> consumer, Consumer<Throwable> throwable) {
        this.consumer = consumer;
        this.throwable = throwable;
    }

    @Override
    protected Integer call() throws Exception {
        ArtService artService = new ArtService();
        artService.getAllExhibitions().subscribe(consumer, throwable);

        return null;
    }
}

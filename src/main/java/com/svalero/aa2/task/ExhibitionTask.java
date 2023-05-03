package com.svalero.aa2.task;

import com.svalero.aa2.model.Exhibition;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class ExhibitionTask extends Task<Integer> {
    private int requestedId;
    private Consumer<ResponsePaginated<Exhibition>> consumer;
    private Consumer<Response<Exhibition>> consumerById;
    private Consumer<Throwable> throwable;

    public ExhibitionTask(int requestedId, Consumer<ResponsePaginated<Exhibition>> consumer,
            Consumer<Response<Exhibition>> consumerById, Consumer<Throwable> throwable) {
        this.requestedId = requestedId;
        this.consumer = consumer;
        this.consumerById = consumerById;
        this.throwable = throwable;
    }

    @Override
    protected Integer call() throws Exception {
        ArtService artService = new ArtService();
        artService.getExhibitionById(requestedId).subscribe(consumerById, throwable);
        artService.getAllExhibitions().subscribe(consumer, throwable);

        return null;
    }
}

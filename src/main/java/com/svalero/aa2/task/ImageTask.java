package com.svalero.aa2.task;

import java.util.UUID;

import com.svalero.aa2.model.ImageAPI;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class ImageTask extends Task<Integer> {
    private UUID requestedId;

    public ImageTask(UUID requestedId) {
        this.requestedId = requestedId;
    }

    @Override
    protected Integer call() throws Exception {
        Consumer<Response<ImageAPI>> consumer = (response) -> {
            updateMessage(response.getData().getIiif_url() + "/full/843,/0/default.jpg");
        };

        Consumer<Throwable> throwable = (error) -> {
            System.out.println(error.toString());
        };

        ArtService artService = new ArtService();
        artService.getImageById(requestedId).subscribe(consumer, throwable);

        return null;
    }
}

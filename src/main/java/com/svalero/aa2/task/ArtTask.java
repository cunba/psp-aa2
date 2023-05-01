package com.svalero.aa2.task;

import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.concurrent.Task;

public class ArtTask extends Task<Integer> {
    private int requestedId;
    private Consumer<Response<Artwork>> user;

    public ArtTask(int requestedId, Consumer<Response<Artwork>> user) {
        this.requestedId = requestedId;
        this.user = user;
    }

    @Override
    protected Integer call() throws Exception {
        ArtService artService = new ArtService();
        artService.getArtworkById(requestedId).subscribe(user);

        return null;
    }
}

package com.svalero.aa2.task;

import com.svalero.aa2.model.Artist;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class ArtistTaskById extends Task<Integer> {
    private int requestedId;
    private ObservableList<String> artistList;

    public ArtistTaskById(int requestedId, ObservableList<String> artistList) {
        this.requestedId = requestedId;
        this.artistList = artistList;
    }

    @Override
    protected Integer call() throws Exception {
        Consumer<Response<Artist>> consumer = (response) -> {
            Platform.runLater(() -> artistList.add(response.getData().getTitle()));
        };

        ArtService artService = new ArtService();
        artService.getArtistById(requestedId).subscribe(consumer);

        return null;
    }
}

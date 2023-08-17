package com.svalero.aa2.task;

import com.svalero.aa2.model.ArtworkType;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class ArtworkTypeTask extends Task<Integer> {
    ObservableList<ArtworkType> artworkTypesList;
    int page;

    public ArtworkTypeTask(ObservableList<ArtworkType> artworkTypesList, int page) {
        this.artworkTypesList = artworkTypesList;
        this.page = page;
    }

    @Override
    protected Integer call() throws Exception {
        Consumer<ResponsePaginated<ArtworkType>> consumer = (response) -> {
            for (ArtworkType artworkType : response.getData()) {
                Platform.runLater(() -> artworkTypesList.add(artworkType));
            }

            if (response.getPagination().getNext_url() != null) {
                ArtworkTypeTask artworkTypeTask = new ArtworkTypeTask(this.artworkTypesList,
                        response.getPagination().getCurrent_page() + 1);
                new Thread(artworkTypeTask).start();
            }
        };

        Consumer<Throwable> throwable = (error) -> {
            System.out.println(error.toString());
        };

        ArtService artService = new ArtService();
        if (page == 1)
            artService.getAllArtworkTypes().subscribe(consumer, throwable);
        else
            artService.getAllArtworkTypesPage(page).subscribe(consumer, throwable);

        return null;
    }
}

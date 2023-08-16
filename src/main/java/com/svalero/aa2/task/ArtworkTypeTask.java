package com.svalero.aa2.task;

import com.svalero.aa2.model.ArtworkType;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
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

        Consumer<Throwable> throwable = (error) -> {
            System.out.println(error.toString());
        };

        Consumer<ResponsePaginated<ArtworkType>> consumer = (response) -> {
            for (ArtworkType artworkType : response.getData()) {
                artworkTypesList.add(artworkType);
            }

            if (response.getPagination().getNext_url() != null) {
                ArtworkTypeTask artworkTypeTaskPage = new ArtworkTypeTask(this.artworkTypesList,
                        response.getPagination().getCurrent_page() + 1);
                new Thread(artworkTypeTaskPage).start();
            }
        };

        ArtService artService = new ArtService();
        if (page == 1)
            artService.getAllArtworkTypes().subscribe(consumer, throwable);
        else
            artService.getAllArtworkTypesPage(page).subscribe(consumer, throwable);

        return null;
    }
}

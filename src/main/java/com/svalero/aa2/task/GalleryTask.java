package com.svalero.aa2.task;

import com.svalero.aa2.model.Gallery;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.service.ArtService;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class GalleryTask extends Task<Integer> {
    private ObservableList<Gallery> galleryList;
    private int page;

    public GalleryTask(ObservableList<Gallery> galleryList, int page) {
        this.galleryList = galleryList;
        this.page = page;
    }

    @Override
    protected Integer call() throws Exception {
        Consumer<ResponsePaginated<Gallery>> consumer = (response) -> {
            for (Gallery gallery : response.getData()) {
                Platform.runLater(() -> galleryList.add(gallery));
            }

            if (response.getPagination().getNext_url() != null) {
                GalleryTask galleryTask = new GalleryTask(this.galleryList,
                        response.getPagination().getCurrent_page() + 1);
                new Thread(galleryTask).start();
            }
        };

        Consumer<Throwable> throwable = (error) -> {
            System.out.println(error.toString());
        };

        ArtService artService = new ArtService();
        if (page == 1)
            artService.getAllGalleries().subscribe(consumer, throwable);
        else
            artService.getAllGalleriesPage(page).subscribe(consumer, throwable);

        return null;
    }
}

package com.svalero.aa2.task;

import java.io.IOException;

import com.svalero.aa2.controller.ArtworkController;
import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.service.ArtService;
import com.svalero.aa2.util.R;

import io.reactivex.functions.Consumer;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class ArtworkTask extends Task<Integer> {
    ObservableList<VBox> artworks;
    ObservableMap<Integer, ResponsePaginated<Artwork>> responses;
    int page;

    public ArtworkTask(ObservableList<VBox> artworks, ObservableMap<Integer, ResponsePaginated<Artwork>> responses,
            int page) {
        this.artworks = artworks;
        this.responses = responses;
        this.page = page;
    }

    @Override
    protected Integer call() throws Exception {
        int artworkNumber = 0;

        Consumer<ResponsePaginated<Artwork>> consumer = (response) -> {
            responses.put(response.getPagination().getCurrent_page(), response);
            updateMessage(String.valueOf(response.getPagination().getCurrent_page()));
            for (Artwork artwork : response.getData()) {
                createVBox(artwork, artworkNumber, response.getPagination().getLimit());
            }
        };

        Consumer<Throwable> throwable = (error) -> {
            System.out.println(error.toString());
        };

        ArtService artService = new ArtService();
        if (page == 1)
            artService.getAllArtworks().subscribe(consumer, throwable);
        else
            artService.getAllArtworksPage(page).subscribe(consumer, throwable);

        return null;
    }

    private void createVBox(Artwork artwork, int artworkNumber, int totalArtworks) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        ArtworkController artworkController = new ArtworkController();
        loader.setLocation(R.getUI("artwork-view.fxml"));
        loader.setController(artworkController);

        if (artwork.getImage_id() != null) {
            ImageTask imageTask = new ImageTask(artwork.getImage_id());
            new Thread(imageTask).start();

            imageTask.messageProperty().addListener((observableValue, oldValue, newValue) -> {
                try {
                    Image image = new Image(R.getImage("noImage.png"));
                    artworkController.showArtwork(artwork, image);
                    VBox vbox = loader.load();
                    vbox.setId(String.valueOf(artwork.getId()));
                    artworks.add(vbox);
                    updateProgress(artworkNumber / totalArtworks, 1);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            });
        } else {
            Image image = new Image(R.getImage("noImage.png"));
            artworkController.showArtwork(artwork, image);
            VBox vbox = loader.load();
            vbox.setId(String.valueOf(artwork.getId()));
            artworks.add(vbox);
            updateProgress(artworkNumber / totalArtworks, 1);
        }
    }
}

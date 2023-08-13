package com.svalero.aa2.controller;

import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.Image;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.task.ArtworkTaskById;
import com.svalero.aa2.task.ImageTask;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ArtworkController {
    @FXML
    public Text artworkTypeText;
    @FXML
    public Text artworkDescriptionText;
    @FXML
    public Text artworkTitleText;

    private ArtworkTaskById artworkTask;
    private ImageTask imageTask;

    public void showArtworkById(int artworkId) {
        artworkDescriptionText.setText(null);
        artworkTitleText.setText(null);
        artworkTypeText.setText(null);

        Consumer<Response<Image>> imageConsumer = (response) -> {
            Image image = response.getData();
            Platform.runLater(() -> {

            });
            System.out.println(image.toString());
        };

        Consumer<Response<Artwork>> consumer = (response) -> {
            Artwork artwork = response.getData();
            artworkDescriptionText.setText(artwork.getPublication_history());
            artworkTypeText.setText(artwork.getArtwork_type_title());
            artworkTitleText.setText(artwork.getTitle() + "\t(" + artwork.getArtist_title() + ")");

            if (artwork.getThumbnail() == null) {
                if (artwork.getImage_id() != null) {
                    imageTask = new ImageTask(artwork.getImage_id(), imageConsumer, null);
                    new Thread(imageTask).start();
                }
            }
        };

        Consumer<Throwable> throwable = (error) -> {
            artworkTypeText.setText("Artwork not found");
        };

        artworkTask = new ArtworkTaskById(artworkId, consumer, throwable);
        new Thread(artworkTask).start();
    }

    public void showArtwork(Artwork artwork) {
        artworkDescriptionText.setText(artwork.getPublication_history());
        artworkTypeText.setText(artwork.getArtwork_type_title());
        artworkTitleText.setText(artwork.getTitle() + "\t(" + artwork.getArtist_title() + ")");

        Consumer<Response<Image>> imageConsumer = (response) -> {
            Image image = response.getData();
            Platform.runLater(() -> {

            });
            System.out.println(image.toString());
        };

        if (artwork.getThumbnail() == null) {
            if (artwork.getImage_id() != null) {
                imageTask = new ImageTask(artwork.getImage_id(), imageConsumer, null);
                new Thread(imageTask).start();
            }
        }
    }
}

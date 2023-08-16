package com.svalero.aa2.controller;

import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.Image;
import com.svalero.aa2.model.Response;
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

    private ImageTask imageTask;

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

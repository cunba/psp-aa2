package com.svalero.aa2.controller;

import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.task.ArtworkTask;

import io.reactivex.functions.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ArtworkController {

    public Text artworkTypeText;
    public Text artworkDescriptionText;
    public Text artworkTitleText;

    private ArtworkTask artworkTask;

    @FXML
    public void showArtworkById(ActionEvent event, int artworkId) {
        artworkDescriptionText.setText(null);
        artworkTitleText.setText(null);
        artworkTypeText.setText(null);

        Consumer<Response<Artwork>> consumer = (response) -> {
            Artwork artwork = response.getData();
            artworkDescriptionText.setText(artwork.getPublication_history());
            artworkTypeText.setText(artwork.getArtwork_type_title());
            artworkTitleText.setText(artwork.getTitle() + "\t(" + artwork.getArtist_title() + ")");
        };

        Consumer<Throwable> throwable = (error) -> {
            artworkTypeText.setText("Artwork not found");
        };

        artworkTask = new ArtworkTask(artworkId, null, consumer, throwable);
        new Thread(artworkTask).start();
    }

    @FXML
    public void showArtwork(ActionEvent event, Artwork artwork) {
        artworkDescriptionText.setText(artwork.getPublication_history());
        artworkTypeText.setText(artwork.getArtwork_type_title());
        artworkTitleText.setText(artwork.getTitle() + "\t(" + artwork.getArtist_title() + ")");
    }
}

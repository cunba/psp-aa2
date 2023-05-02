package com.svalero.aa2.controller;

import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.task.ArtTask;

import io.reactivex.functions.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ArtworkController {

    public Text artworkTypeText;
    public Text artworkDescriptionText;
    public Text artworkTitleText;

    private ArtTask artTask;

    @FXML
    public void searchArtwork(ActionEvent event) {
        artworkDescriptionText.setText(null);
        artworkTitleText.setText(null);
        artworkTypeText.setText(null);

        Consumer<Response<Artwork>> user = (response) -> {
            Artwork artwork = response.getData();
            artworkDescriptionText.setText(artwork.getPublication_history());
            artworkTypeText.setText(artwork.getArtwork_type_title());
            artworkTitleText.setText(artwork.getTitle() + "\t(" + artwork.getArtist_title() + ")");
        };

        artTask = new ArtTask(8360, user);
        new Thread(artTask).start();
    }
}

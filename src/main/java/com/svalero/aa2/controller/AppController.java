package com.svalero.aa2.controller;

import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.task.ArtTask;

import io.reactivex.functions.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AppController {

    public TextField artworkId;
    public Button searchArtwork;
    public TextArea textArea;

    private ArtTask artTask;

    @FXML
    public void searchArtwork(ActionEvent event) {
        System.out.println(artworkId.getText());
        int requestedId = Integer.parseInt(artworkId.getText());
        artworkId.clear();
        artworkId.requestFocus();
        textArea.setText("");

        Consumer<Response<Artwork>> user = (response) -> {
            System.out.println(response.getData().toString());
            // textArea.setText(response.getData().toString());
        };

        artTask = new ArtTask(requestedId, user);
        new Thread(artTask).start();
    }
}

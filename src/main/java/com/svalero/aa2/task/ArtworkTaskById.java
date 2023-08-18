package com.svalero.aa2.task;

import java.io.IOException;

import com.svalero.aa2.controller.ArtworkController;
import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.service.ArtService;
import com.svalero.aa2.util.R;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class ArtworkTaskById extends Task<VBox> {
    private int requestedId;

    public ArtworkTaskById(int requestedId) {
        this.requestedId = requestedId;
    }

    @Override
    protected VBox call() throws Exception {
        Consumer<Response<Artwork>> consumer = (response) -> {
            try {
                createVBox(response.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Consumer<Throwable> throwable = (error) -> {
            System.out.println("entra aqui");
            error.printStackTrace();
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Artwork with ID " + requestedId + " not found");
                alert.showAndWait();
            });
        };
        ArtService artService = new ArtService();
        artService.getArtworkById(requestedId).subscribe(consumer, throwable);

        return null;
    }

    private void createVBox(Artwork artwork) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        ArtworkController artworkController = new ArtworkController();
        loader.setLocation(R.getUI("artwork-view.fxml"));
        loader.setController(artworkController);

        if (artwork.getImage_id() != null) {
            ImageTask imageTask = new ImageTask(artwork.getImage_id());
            new Thread(imageTask).start();

            imageTask.messageProperty().addListener((observableValue, oldValue, newValue) -> {
                try {
                    Image image = new Image(newValue);

                    VBox vbox = loader.load();
                    artworkController.showArtwork(artwork, image);
                    vbox.setId(String.valueOf(artwork.getId()));

                    updateValue(vbox);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        } else {
            Image image = new Image(R.getImage("noImage.png"));

            VBox vbox = loader.load();
            artworkController.showArtwork(artwork, image);
            vbox.setId(String.valueOf(artwork.getId()));

            updateValue(vbox);
        }
    }
}

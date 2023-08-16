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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ArtworkTaskById extends Task<Integer> {
    private int requestedId;
    private Stage primaryStage;

    public ArtworkTaskById(int requestedId, Stage primaryStage) {
        this.requestedId = requestedId;
        this.primaryStage = primaryStage;
    }

    @Override
    protected Integer call() throws Exception {
        Consumer<Response<Artwork>> consumer = (response) -> {
            try {
                createVBox(response.getData(), 1, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        Consumer<Throwable> throwable = (error) -> {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Artwork with ID " + requestedId + " not found");
                alert.showAndWait();
            });
        };
        ArtService artService = new ArtService();
        artService.getArtworkById(requestedId).subscribe(consumer, throwable);

        return null;
    }

    private void createVBox(Artwork artwork, int artworkNumber, int totalArtworks) throws IOException {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);

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

                    Scene scene = new Scene(vbox);
                    stage.setScene(scene);
                    stage.show();
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

            Scene scene = new Scene(vbox);
            stage.setScene(scene);
            stage.show();
        }
    }
}

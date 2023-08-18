package com.svalero.aa2.task;

import java.io.IOException;

import com.svalero.aa2.controller.ExhibitionController;
import com.svalero.aa2.model.Exhibition;
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

public class ExhibitionTaskById extends Task<VBox> {
    private int requestedId;

    public ExhibitionTaskById(int requestedId) {
        this.requestedId = requestedId;
    }

    @Override
    protected VBox call() throws Exception {
        Consumer<Response<Exhibition>> consumer = (response) -> {
            Platform.runLater(() -> {
                try {
                    createExhibition(response.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        };

        Consumer<Throwable> throwable = (error) -> {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Exhibition with ID " + requestedId + " not found");
                alert.showAndWait();
            });
        };

        ArtService artService = new ArtService();
        artService.getExhibitionById(requestedId).subscribe(consumer, throwable);

        return null;
    }

    private void createExhibition(Exhibition exhibition)
            throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("exhibition-view.fxml"));
        ExhibitionController exhibitionController = new ExhibitionController();
        loader.setController(exhibitionController);

        if (exhibition.getImage_id() != null) {
            ImageTask imageTask = new ImageTask(exhibition.getImage_id());
            new Thread(imageTask).start();

            imageTask.messageProperty().addListener((observableValue, oldValue, newValue) -> {
                try {
                    Image image = new Image(newValue);

                    VBox vbox = loader.load();
                    exhibitionController.showExhibition(exhibition, image);
                    vbox.setId(String.valueOf(exhibition.getId()));

                    updateValue(vbox);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        } else {
            Image image = new Image(R.getImage("noImage.png"));

            VBox vbox = loader.load();
            exhibitionController.showExhibition(exhibition, image);
            vbox.setId(String.valueOf(exhibition.getId()));

            updateValue(vbox);
        }
    }
}

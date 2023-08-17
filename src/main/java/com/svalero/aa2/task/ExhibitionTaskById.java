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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ExhibitionTaskById extends Task<Integer> {
    private int requestedId;
    private Stage primaryStage;

    public ExhibitionTaskById(int requestedId, Stage primaryStage) {
        this.requestedId = requestedId;
        this.primaryStage = primaryStage;
    }

    @Override
    protected Integer call() throws Exception {
        Consumer<Response<Exhibition>> consumer = (response) -> {
            Platform.runLater(() -> {
                try {
                    createExhibition(response.getData(), 1, 1);
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

    private void createExhibition(Exhibition exhibition, int exhibitionNumber, int totalExhibitions)
            throws IOException {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);

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
                    exhibitionController.showExhibition(exhibition, image);

                    VBox vbox = loader.load();
                    vbox.setId(String.valueOf(exhibition.getId()));

                    Scene scene = new Scene(vbox);
                    stage.setScene(scene);
                    Platform.runLater(() -> stage.show());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        } else {
            Image image = new Image(R.getImage("noImage.png"));
            exhibitionController.showExhibition(exhibition, image);

            VBox vbox = loader.load();
            vbox.setId(String.valueOf(exhibition.getId()));

            Scene scene = new Scene(vbox);
            stage.setScene(scene);
            Platform.runLater(() -> stage.show());
        }
    }
}

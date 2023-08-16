package com.svalero.aa2.controller;

import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.ImageAPI;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.task.ImageTask;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ArtworkController {
    @FXML
    public Text artworkTypeText;
    @FXML
    public Text titleText;
    @FXML
    public ScrollPane descriptionSP;
    @FXML
    public Text descriptionText;
    @FXML
    public Text artistText;
    @FXML
    public ImageView artworkIV;

    private ImageTask imageTask;

    public void showArtwork(Artwork artwork) {
        if (artwork.getPublication_history() != null)
            descriptionText.setText(artwork.getPublication_history());
        else if (artwork.getExhibition_history() != null)
            descriptionText.setText(artwork.getExhibition_history());
        else {
            artworkIV.setFitWidth(1200);
            descriptionSP.setStyle("-fx-background-color:transparent;");
            descriptionSP.setPrefWidth(0);
        }

        artworkTypeText.setText(artwork.getArtwork_type_title());
        if (artwork.getTitle() != null)
            titleText.setText(artwork.getTitle());

        if (artwork.getArtist_title() != null)
            artistText.setText(artwork.getArtist_title() + " from " + artwork.getPlace_of_origin());
        else
            artistText.setText("Artist unkown from " + artwork.getPlace_of_origin());

        Consumer<Response<ImageAPI>> imageConsumer = (response) -> {
            Platform.runLater(() -> {
                Image image = new Image(response.getData().getIiif_url() + "/full/843,/0/default.jpg");
                artworkIV.setImage(image);
            });
        };

        Consumer<Throwable> throwable = (error) -> {
            System.out.println(error.toString());
        };

        if (artwork.getImage_id() != null) {
            imageTask = new ImageTask(artwork.getImage_id(), imageConsumer, throwable);
            new Thread(imageTask).start();
        }
    }
}

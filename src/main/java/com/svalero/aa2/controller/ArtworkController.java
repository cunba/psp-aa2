package com.svalero.aa2.controller;

import com.svalero.aa2.model.Artwork;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ArtworkController {
    @FXML
    public Text artworkTypeText;
    @FXML
    public Text titleText;
    @FXML
    public VBox descriptionVBox;
    @FXML
    public Text descriptionText;
    @FXML
    public Text artistText;
    @FXML
    public ImageView artworkIV;
    @FXML
    public Text descriptionLabel;
    @FXML
    public ScrollPane descriptionSP;

    public void showArtwork(Artwork artwork, Image image) {
        if (artwork.getPublication_history() != null)
            descriptionText.setText(artwork.getPublication_history());
        else if (artwork.getExhibition_history() != null)
            descriptionText.setText(artwork.getExhibition_history());
        else {
            artworkIV.setFitWidth(1200);
            descriptionLabel.setText(null);
            descriptionSP.setPrefWidth(0);
            descriptionSP.setStyle("-fx-background-color:transparent;");
            descriptionVBox.setPrefWidth(0);
        }

        artworkTypeText.setText(artwork.getArtwork_type_title());
        if (artwork.getTitle() != null)
            titleText.setText(artwork.getTitle());

        if (artwork.getArtist_title() != null)
            artistText.setText(artwork.getArtist_title() + " from " + artwork.getPlace_of_origin());
        else
            artistText.setText("Artist unkown from " + artwork.getPlace_of_origin());

        artworkIV.setImage(image);
    }
}

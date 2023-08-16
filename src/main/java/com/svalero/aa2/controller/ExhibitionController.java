package com.svalero.aa2.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.svalero.aa2.App;
import com.svalero.aa2.model.Artist;
import com.svalero.aa2.model.Exhibition;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.task.ArtistTaskById;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ExhibitionController implements Initializable {
    @FXML
    public Text titleText;
    @FXML
    public Text galleryTitleText;
    @FXML
    public Text descriptionText;
    @FXML
    public Text webText;
    @FXML
    public Hyperlink webHyperlink;
    @FXML
    public ListView<String> artistsLV;
    @FXML
    public ListView<String> artworksLV;
    @FXML
    public ImageView exhibitionIV;
    @FXML
    public VBox artistArtworkVBox;
    @FXML
    public Label artistsLabel;
    @FXML
    public Label artworksLabel;

    private ObservableList<String> artistList;
    private ObservableList<String> artworkList;
    private ArtistTaskById artistTask;
    private App app;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        app = new App();

        artistList = FXCollections.observableArrayList();
        artistsLV.setItems(artistList);

        artworkList = FXCollections.observableArrayList();
        artworksLV.setItems(artworkList);
    }

    public void showExhibition(Exhibition exhibition, Image image) {
        descriptionText.setText(null);
        galleryTitleText.setText(null);
        titleText.setText(null);
        webText.setText(null);
        webHyperlink.setText(null);

        Consumer<Response<Artist>> artistConsumer = (response) -> {
            Platform.runLater(() -> {
                artistList.add(response.getData().getTitle());
            });
        };

        titleText.setText(exhibition.getTitle());
        if (exhibition.getGallery_title() != null) {
            galleryTitleText.setText("Gallery " + exhibition.getGallery_title());
        }

        exhibitionIV.setImage(image);

        if (exhibition.getArtist_ids() != null && exhibition.getArtist_ids().size() != 0) {
            List<Integer> artistIds = exhibition.getArtist_ids();
            for (int artistId : artistIds) {
                artistTask = new ArtistTaskById(artistId, artistConsumer, t -> System.out.println(t.getMessage()));
                new Thread(artistTask).start();
            }
        }

        if (exhibition.getArtwork_titles() != null && exhibition.getArtwork_titles().size() != 0) {
            artworkList.addAll(exhibition.getArtwork_titles());
        }

        if (exhibition.getArtist_ids() == null && exhibition.getArtwork_titles() == null) {
            exhibitionIV.setFitWidth(1200);
            artistsLabel.setText(null);
            artworksLabel.setText(null);
            artistsLV.setPrefWidth(0);
            artistsLV.setStyle("-fx-background-color:transparent;");
            artworksLV.setPrefWidth(0);
            artworksLV.setStyle("-fx-background-color:transparent;");
            artistArtworkVBox.setPrefWidth(0);
        }

        if (exhibition.getWeb_url() != null) {
            Platform.runLater(() -> {
                webText.setText("Source: ");
                webHyperlink.setText(exhibition.getWeb_url());
            });

            webHyperlink.setOnAction(e -> {
                app.openURL(exhibition.getWeb_url());
            });
        }

        if (exhibition.getShort_description() != null)
            descriptionText
                    .setText(exhibition.getShort_description().replaceAll("<p>", "").replaceAll("</p>", ""));
    }
}

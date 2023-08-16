package com.svalero.aa2.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.svalero.aa2.App;
import com.svalero.aa2.model.Artist;
import com.svalero.aa2.model.Exhibition;
import com.svalero.aa2.model.ImageAPI;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.task.ArtistTaskById;
import com.svalero.aa2.task.ImageTask;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class ExhibitionController implements Initializable {
    @FXML
    public Text exhibitionTitleText;
    @FXML
    public Text exhibitionGalleryTitleText;
    @FXML
    public Text exhibitionDescriptionText;
    @FXML
    public Text exhibitionWebText;
    @FXML
    public Hyperlink exhibitionWebHyperlink;
    @FXML
    public ListView<String> exhibitionArtistsLV;
    @FXML
    public ListView<String> exhibitionArtworksLV;

    private ObservableList<String> artistList;
    private ObservableList<String> artworkList;
    private ArtistTaskById artistTask;
    private ImageTask imageTask;
    private App app;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        app = new App();

        artistList = FXCollections.observableArrayList();
        exhibitionArtistsLV.setItems(artistList);

        artworkList = FXCollections.observableArrayList();
        exhibitionArtworksLV.setItems(artworkList);
    }

    public void showExhibition(Exhibition exhibition) {
        exhibitionDescriptionText.setText(null);
        exhibitionGalleryTitleText.setText(null);
        exhibitionTitleText.setText(null);
        exhibitionWebText.setText(null);
        exhibitionWebHyperlink.setText(null);

        Consumer<Response<Artist>> artistConsumer = (response) -> {
            Platform.runLater(() -> {
                artistList.add(response.getData().getTitle());
            });
        };

        Consumer<Response<ImageAPI>> imageConsumer = (response) -> {
            Platform.runLater(() -> {

            });
        };

        exhibitionTitleText.setText(exhibition.getTitle());
        if (exhibition.getGallery_title() != null) {
            exhibitionGalleryTitleText.setText("Gallery " + exhibition.getGallery_title());
        }

        if (exhibition.getImage_url() == null) {
            if (exhibition.getImage_id() != null) {
                imageTask = new ImageTask(exhibition.getImage_id(), imageConsumer, null);
                new Thread(imageTask).start();
            }
        }

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

        if (exhibition.getWeb_url() != null) {
            Platform.runLater(() -> {
                exhibitionWebText.setText("Source: ");
                exhibitionWebHyperlink.setText(exhibition.getWeb_url());
            });

            exhibitionWebHyperlink.setOnAction(e -> {
                app.openURL(exhibition.getWeb_url());
            });
        }
        exhibitionDescriptionText
                .setText(exhibition.getShort_description().replaceAll("<p>", "").replaceAll("</p>", ""));
    }
}

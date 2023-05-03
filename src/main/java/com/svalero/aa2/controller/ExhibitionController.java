package com.svalero.aa2.controller;

import java.util.List;

import com.svalero.aa2.App;
import com.svalero.aa2.model.Artist;
import com.svalero.aa2.model.Exhibition;
import com.svalero.aa2.model.Image;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.task.ArtistTask;
import com.svalero.aa2.task.ExhibitionTaskById;
import com.svalero.aa2.task.ImageTask;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class ExhibitionController {

    public Text exhibitionTitleText;
    public Text exhibitionGalleryTitleText;
    public Text exhibitionDescriptionText;
    public Text exhibitionWebText;
    public Hyperlink exhibitionWebHyperlink;
    public ListView<String> exhibitionArtistsLV;
    public ListView<String> exhibitionArtworksLV;

    private ObservableList<String> artistList;
    private ObservableList<String> artworkList;

    private ExhibitionTaskById exhibitionTask;
    private ArtistTask artistTask;
    private ImageTask imageTask;
    private App app;

    @FXML
    public void initialize() {
        app = new App();

        artistList = FXCollections.observableArrayList();
        exhibitionArtistsLV.setItems(artistList);

        artworkList = FXCollections.observableArrayList();
        exhibitionArtworksLV.setItems(artworkList);
    }

    @FXML
    public void showExhibitionById(ActionEvent event, int exhibitionId) {
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

        Consumer<Response<Image>> imageConsumer = (response) -> {
            Image image = response.getData();
            Platform.runLater(() -> {

            });
            System.out.println(image.toString());
        };

        Consumer<Response<Exhibition>> consumer = (response) -> {
            Exhibition exhibition = response.getData();
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
                    artistTask = new ArtistTask(artistId, artistConsumer, t -> System.out.println(t.getMessage()));
                    new Thread(artistTask).start();
                }
            }

            if (exhibition.getArtwork_titles() != null && exhibition.getArtwork_titles().size() != 0) {
                artworkList.addAll(exhibition.getArtwork_titles());
            }

            exhibitionDescriptionText
                    .setText(exhibition.getShort_description().replaceAll("<p>", "").replaceAll("</p>", ""));

            if (exhibition.getWeb_url() != null) {
                Platform.runLater(() -> {
                    exhibitionWebText.setText("Source: ");
                    exhibitionWebHyperlink.setText(exhibition.getWeb_url());
                });

                exhibitionWebHyperlink.setOnAction(e -> {
                    app.openURL(exhibition.getWeb_url());
                });
            }
        };

        Consumer<Throwable> throwable = (error) -> {
            exhibitionTitleText.setText("Exhibition Not Found");
        };

        exhibitionTask = new ExhibitionTaskById(exhibitionId, consumer, throwable);
        new Thread(exhibitionTask).start();
    }

    @FXML
    public void showExhibition(ActionEvent event, Exhibition exhibition) {
        Consumer<Response<Artist>> artistConsumer = (response) -> {
            Platform.runLater(() -> {
                artistList.add(response.getData().getTitle());
            });
        };

        Consumer<Response<Image>> imageConsumer = (response) -> {
            Image image = response.getData();
            Platform.runLater(() -> {

            });
            System.out.println(image.toString());
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
                artistTask = new ArtistTask(artistId, artistConsumer, t -> System.out.println(t.getMessage()));
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
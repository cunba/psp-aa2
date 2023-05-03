package com.svalero.aa2.controller;

import java.io.IOException;

import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.Exhibition;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.task.ArtworkTask;
import com.svalero.aa2.task.ExhibitionTask;
import com.svalero.aa2.util.R;

import io.reactivex.functions.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

public class MainController {
    public Tab artworksTab;
    public Tab exhibitionsTab;
    public ListView<Scene> artworkLV;
    public ListView<Scene> exhibitionLV;

    private Boolean sortArtworksPageByArtworkTypePressed = false;
    private Boolean sortArtworksPageByTitlePressed = false;
    private Boolean sortArtworksPageByArtistPressed = false;
    private Boolean filterArtworkPageByArtworkTypePressed = false;
    private Boolean sortExhibitionsPageByTitlePressed = false;
    private Boolean sortExhibitionsPageByGalleryPressed = false;
    private Boolean sortExhibitionsPageByOpenDatePressed = false;

    private ObservableList<Scene> artworkList;
    private ObservableList<Scene> exhibitionList;

    private ExhibitionTask exhibitionTask;
    private ArtworkTask artworkTask;

    // private ExhibitionTaskById exhibitionTaskById;
    // private ArtworkTaskById artworkTaskById;

    @FXML
    public void initialize() {
        artworkList = FXCollections.observableArrayList();
        artworkLV.setItems(artworkList);

        exhibitionList = FXCollections.observableArrayList();
        exhibitionLV.setItems(exhibitionList);

        initializeArtworks();
        initializeExhibitions();
    }

    @FXML
    public void sortArtworksPageByArtworkType(ActionEvent event) {
        sortArtworksPageByArtworkTypePressed = sortArtworksPageByArtworkTypePressed == true ? false : true;
    }

    @FXML
    public void sortArtworksPageByTitle(ActionEvent event) {
        sortArtworksPageByTitlePressed = sortArtworksPageByTitlePressed == true ? false : true;
    }

    @FXML
    public void sortArtworksPageByArtist(ActionEvent event) {
        sortArtworksPageByArtistPressed = sortArtworksPageByArtistPressed == true ? false : true;
    }

    @FXML
    public void filterArtworkPageByArtworkType(ActionEvent event) {
        filterArtworkPageByArtworkTypePressed = filterArtworkPageByArtworkTypePressed == true ? false : true;
    }

    @FXML
    public void findArtworkById(ActionEvent event) {

    }

    @FXML
    public void sortExhibitionsPageByTitle(ActionEvent event) {
        sortExhibitionsPageByTitlePressed = sortExhibitionsPageByTitlePressed == true ? false : true;
    }

    @FXML
    public void sortExhibitionsPageByGallery(ActionEvent event) {
        sortExhibitionsPageByGalleryPressed = sortExhibitionsPageByGalleryPressed == true ? false : true;
    }

    @FXML
    public void sortExhibitionsPageByOpenDate(ActionEvent event) {
        sortExhibitionsPageByOpenDatePressed = sortExhibitionsPageByOpenDatePressed == true ? false : true;
    }

    @FXML
    public void findExhibitionById(ActionEvent event) {

    }

    @FXML
    private void initializeArtworks() {
        Consumer<ResponsePaginated<Artwork>> consumer = (response) -> {
            for (Artwork artwork : response.getData()) {
                Scene scene = initializeArtworkScene(artwork);
                exhibitionList.add(scene);
            }

        };

        Consumer<Throwable> throwable = (error) -> {
            System.out.println(error.toString());
        };

        artworkTask = new ArtworkTask(consumer, throwable);
        new Thread(artworkTask).start();
    }

    @FXML
    private void initializeExhibitions() {
        Consumer<ResponsePaginated<Exhibition>> consumer = (response) -> {
            for (Exhibition exhibition : response.getData()) {
                Scene scene = initializeExhibitionScene(exhibition);
                exhibitionList.add(scene);
            }

        };

        exhibitionTask = new ExhibitionTask(consumer, null);
        new Thread(exhibitionTask).start();
    }

    @FXML
    private Scene initializeArtworkScene(Artwork artwork) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("artwork-view.fxml"));
        ArtworkController artworkController = new ArtworkController();
        artworkController.showArtwork(new ActionEvent(), artwork);
        loader.setController(artworkController);
        VBox artworkPane = loader.load();
        Scene scene = new Scene(artworkPane);
        return scene;
    }

    @FXML
    private Scene initializeExhibitionScene(Exhibition exhibition) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("exhibition-view.fxml"));
        ExhibitionController exhibitionController = new ExhibitionController();
        exhibitionController.showExhibition(new ActionEvent(), exhibition);
        loader.setController(exhibitionController);
        VBox exhibitionPane = loader.load();
        Scene scene = new Scene(exhibitionPane);
        return scene;
    }
}

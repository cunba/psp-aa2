package com.svalero.aa2.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class MainController implements Initializable {
    public Tab artworksTab;
    public Tab exhibitionsTab;
    public ListView<VBox> artworkLV;
    public ListView<VBox> exhibitionLV;

    private Boolean sortArtworksPageByArtworkTypePressed = false;
    private Boolean sortArtworksPageByTitlePressed = false;
    private Boolean sortArtworksPageByArtistPressed = false;
    private Boolean filterArtworkPageByArtworkTypePressed = false;
    private Boolean sortExhibitionsPageByTitlePressed = false;
    private Boolean sortExhibitionsPageByGalleryPressed = false;
    private Boolean sortExhibitionsPageByOpenDatePressed = false;

    private ObservableList<VBox> artworkList;
    private ObservableList<VBox> exhibitionList;

    private ExhibitionTask exhibitionTask;
    private ArtworkTask artworkTask;

    // private ExhibitionTaskById exhibitionTaskById;
    // private ArtworkTaskById artworkTaskById;

    public MainController() {
        artworkList = FXCollections.observableArrayList();
        exhibitionList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        exhibitionLV.setItems(exhibitionList);
        artworkLV.setItems(artworkList);

        artworkLV.setCellFactory(new Callback<ListView<VBox>, ListCell<VBox>>() {
            @Override
            public ListCell<VBox> call(ListView<VBox> list) {
                ListCell<VBox> cell = new ListCell<VBox>() {
                    @Override
                    public void updateItem(VBox item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(item);
                    }
                };

                return cell;
            }
        });

        exhibitionLV.setCellFactory(new Callback<ListView<VBox>, ListCell<VBox>>() {
            @Override
            public ListCell<VBox> call(ListView<VBox> list) {
                ListCell<VBox> cell = new ListCell<VBox>() {
                    @Override
                    public void updateItem(VBox item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(item);
                    }
                };

                return cell;
            }
        });

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

    // @FXML
    private void initializeArtworks() {
        System.out.println("entra a artwork");
        Consumer<ResponsePaginated<Artwork>> consumer = (response) -> {
            System.out.println("Esta entrando aqui");
            for (Artwork artwork : response.getData()) {
                VBox vbox = initializeArtworkScene(artwork);
                exhibitionList.add(vbox);
            }
        };

        Consumer<Throwable> throwable = (error) -> {
            System.out.println(error.toString());
        };

        artworkTask = new ArtworkTask(consumer, throwable);
        new Thread(artworkTask).start();
    }

    // @FXML
    private void initializeExhibitions() {
        System.out.println("entra a exhibition");
        Consumer<ResponsePaginated<Exhibition>> consumer = (response) -> {
            for (Exhibition exhibition : response.getData()) {
                VBox vbox = initializeExhibitionScene(exhibition);
                exhibitionList.add(vbox);
            }

        };

        exhibitionTask = new ExhibitionTask(consumer, null);
        new Thread(exhibitionTask).start();
    }

    // @FXML
    private VBox initializeArtworkScene(Artwork artwork) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("artwork-view.fxml"));
        ArtworkController artworkController = new ArtworkController();
        artworkController.showArtwork(new ActionEvent(), artwork);
        loader.setController(artworkController);
        VBox artworkPane = loader.load();
        return artworkPane;
    }

    // @FXML
    private VBox initializeExhibitionScene(Exhibition exhibition) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("exhibition-view.fxml"));
        ExhibitionController exhibitionController = new ExhibitionController();
        exhibitionController.showExhibition(new ActionEvent(), exhibition);
        loader.setController(exhibitionController);
        VBox exhibitionPane = loader.load();
        return exhibitionPane;
    }

    // static class Cell extends ListCell<VBox> {
    // @Override
    // public void updateItem(VBox item, boolean empty) {
    // super.updateItem(item, empty);
    // System.out.println("entra");
    // // Rectangle rect = new Rectangle(100, 20);
    // // if (item != null) {
    // // rect.setFill(Color.web(item));
    // setGraphic(item);
    // // }
    // }
    // }
}
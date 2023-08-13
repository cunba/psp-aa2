package com.svalero.aa2.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.task.ArtworkTask;
import com.svalero.aa2.util.R;

import io.reactivex.functions.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class ArtworkTabController implements Initializable {
    @FXML
    public ListView<VBox> artworkLV;

    private Boolean sortArtworksPageByArtworkTypePressed = false;
    private Boolean sortArtworksPageByTitlePressed = false;
    private Boolean sortArtworksPageByArtistPressed = false;
    private Boolean filterArtworkPageByArtworkTypePressed = false;

    private ObservableList<VBox> artworkList;

    private ArtworkTask artworkTask;

    // private ExhibitionTaskById exhibitionTaskById;
    // private ArtworkTaskById artworkTaskById;

    public ArtworkTabController() {
        artworkList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        artworkLV.setItems(artworkList);

        // artworkLV.setCellFactory(new Callback<ListView<VBox>, ListCell<VBox>>() {
        // @Override
        // public ListCell<VBox> call(ListView<VBox> list) {
        // ListCell<VBox> cell = new ListCell<VBox>() {
        // @Override
        // public void updateItem(VBox item, boolean empty) {
        // super.updateItem(item, empty);
        // setGraphic(item);
        // }
        // };

        // return cell;
        // }
        // });

        Consumer<ResponsePaginated<Artwork>> consumer = (response) -> {
            System.out.println("Esta entrando aqui");
            for (Artwork artwork : response.getData()) {
                VBox vbox = initializeArtworkScene(artwork);
                artworkList.add(vbox);
            }
        };

        Consumer<Throwable> throwable = (error) -> {
            System.out.println(error.toString());
        };

        artworkTask = new ArtworkTask(consumer, throwable);
        new Thread(artworkTask).start();
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

    private VBox initializeArtworkScene(Artwork artwork) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("artwork-view.fxml"));
        ArtworkController artworkController = new ArtworkController();
        artworkController.showArtwork(new ActionEvent(), artwork);
        loader.setController(artworkController);
        VBox artworkPane = loader.load();
        return artworkPane;
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
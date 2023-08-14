package com.svalero.aa2.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.task.ArtworkTask;
import com.svalero.aa2.task.ArtworkTaskById;
import com.svalero.aa2.util.R;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ArtworkTabController implements Initializable {
    @FXML
    public ListView<VBox> artworkLV;
    @FXML
    public Pagination artworkPagination;
    @FXML
    public ToggleButton artworkSortByArtworkTypeTB;
    @FXML
    public ToggleButton artworkSortByTitleTB;
    @FXML
    public ToggleButton artworkSortByArtistTB;
    @FXML
    public ComboBox artworkTypesCB;
    @FXML
    public ToggleButton artworkFilterByArtworkType;
    @FXML
    public TextField artworkIdTF1;
    @FXML
    public Button findArtworkByIdButton;

    private Boolean sortArtworksPageByArtworkTypePressed = false;
    private Boolean sortArtworksPageByTitlePressed = false;
    private Boolean sortArtworksPageByArtistPressed = false;
    private Boolean filterArtworkPageByArtworkTypePressed = false;

    private ObservableList<VBox> artworkList;

    private ArtworkTask artworkTask;
    private List<ResponsePaginated<Artwork>> responses;
    private Stage primaryStage;

    // private ExhibitionTaskById exhibitionTaskById;
    // private ArtworkTaskById artworkTaskById;

    public ArtworkTabController(Stage stage) {
        artworkList = FXCollections.observableArrayList();
        responses = new ArrayList<>();
        primaryStage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        artworkLV.setItems(artworkList);

        Consumer<ResponsePaginated<Artwork>> consumer = (response) -> {
            responses.add(response);
            for (Artwork artwork : response.getData()) {
                VBox vbox = showArtwork(artwork);
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
    public void findArtworkById() {
        try {
            int id = Integer.parseInt(artworkIdTF1.getText());

            Consumer<Response<Artwork>> consumer = (response) -> {
                Platform.runLater(() -> {
                    try {
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initOwner(primaryStage);

                        VBox artwork;
                        artwork = showArtwork(response.getData());
                        Scene scene = new Scene(artwork);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            };

            Consumer<Throwable> throwable = (error) -> {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Artwork with ID " + id + " not found");
                    alert.showAndWait();
                });
            };

            ArtworkTaskById artworkTaskById = new ArtworkTaskById(id, consumer, throwable);
            new Thread(artworkTaskById).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox showArtwork(Artwork artwork) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        ArtworkController artworkController = new ArtworkController();
        loader.setLocation(R.getUI("artwork-view.fxml"));
        loader.setController(artworkController);
        VBox artworkPane = loader.load();
        artworkController.showArtwork(artwork);
        return artworkPane;
    }
}
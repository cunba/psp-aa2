package com.svalero.aa2.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.ArtworkType;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.task.ArtworkTask;
import com.svalero.aa2.task.ArtworkTaskById;
import com.svalero.aa2.task.ArtworkTypeTask;
import com.svalero.aa2.task.ArtworkTypeTaskPage;
import com.svalero.aa2.util.R;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
    public Button artworkSortByArtworkTypeButton;
    @FXML
    public Button artworkSortByTitleButton;
    @FXML
    public Button artworkSortByArtistButton;
    @FXML
    public ComboBox<ArtworkType> artworkTypesCB;
    @FXML
    public ToggleButton artworkFilterByArtworkTypeTB;
    @FXML
    public TextField artworkIdTF;
    @FXML
    public Button findArtworkByIdButton;

    private ObservableList<VBox> artworkList;
    private FilteredList<VBox> artworkListFiltered;
    private ArtworkTask artworkTask;
    private Map<Integer, ResponsePaginated<Artwork>> responses;
    private Stage primaryStage;
    private int currentPage;

    public ArtworkTabController(Stage stage) {
        artworkList = FXCollections.observableArrayList();
        artworkListFiltered = new FilteredList<>(artworkList);
        responses = new HashMap<>();
        primaryStage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        artworkLV.setItems(artworkListFiltered);

        Consumer<ResponsePaginated<Artwork>> consumerArtwork = (response) -> {
            responses.put(response.getPagination().getCurrent_page(), response);
            currentPage = response.getPagination().getCurrent_page();
            for (Artwork artwork : response.getData()) {
                VBox vbox = showArtwork(artwork);
                vbox.setId(String.valueOf(artwork.getId()));
                artworkList.add(vbox);
            }
        };

        Consumer<Throwable> throwable = (error) -> {
            System.out.println(error.toString());
        };

        Consumer<ResponsePaginated<ArtworkType>> consumerArtworkType = (response) -> {
            for (ArtworkType artworkType : response.getData()) {
                artworkTypesCB.getItems().add(artworkType);
            }

            if (response.getPagination().getNext_url() != null) {
                addArtworkTypeNextPage(response.getPagination().getCurrent_page() + 1);
            }
        };

        ArtworkTypeTask artworkTypeTask = new ArtworkTypeTask(consumerArtworkType, throwable);
        new Thread(artworkTypeTask).start();

        artworkTask = new ArtworkTask(consumerArtwork, throwable);
        new Thread(artworkTask).start();

    }

    private void addArtworkTypeNextPage(int page) {
        Consumer<ResponsePaginated<ArtworkType>> consumerArtworkType = (response) -> {
            for (ArtworkType artworkType : response.getData()) {
                artworkTypesCB.getItems().add(artworkType);
            }

            if (response.getPagination().getNext_url() != null) {
                addArtworkTypeNextPage(response.getPagination().getCurrent_page() + 1);
            }
        };

        Consumer<Throwable> throwable = (error) -> {
            System.out.println(error.toString());
        };

        ArtworkTypeTaskPage artworkTypeTaskPage = new ArtworkTypeTaskPage(page, consumerArtworkType, throwable);
        new Thread(artworkTypeTaskPage).start();
    }

    @FXML
    public void sortArtworksPageByArtworkType(ActionEvent event) {
        // sortArtworksPageByArtworkTypePressed = sortArtworksPageByArtworkTypePressed
        // == true ? false : true;
    }

    @FXML
    public void sortArtworksPageByTitle(ActionEvent event) {
        // sortArtworksPageByTitlePressed = sortArtworksPageByTitlePressed == true ?
        // false : true;
    }

    @FXML
    public void sortArtworksPageByArtist(ActionEvent event) {
        // sortArtworksPageByArtistPressed = sortArtworksPageByArtistPressed == true ?
        // false : true;
    }

    @FXML
    public void filterArtworkPageByArtworkType(ActionEvent event) {
        if (artworkFilterByArtworkTypeTB.isSelected()) {
            ResponsePaginated<Artwork> response = responses.get(currentPage);
            List<Artwork> responseListFiltered = response.getData().stream()
                    .filter(artwork -> artwork.getArtwork_type_id() == artworkTypesCB.getValue()
                            .getId())
                    .collect(Collectors.toList());

            List<String> ids = new ArrayList<>();
            responseListFiltered.forEach((artwork) -> ids.add(String.valueOf(artwork.getId())));

            Predicate<VBox> filter = vbox -> ids.contains(vbox.getId());
            artworkListFiltered.setPredicate(filter);
        } else {
            artworkListFiltered.setPredicate(null);
        }
    }

    @FXML
    public void findArtworkById() {
        try {
            int id = Integer.parseInt(artworkIdTF.getText());

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

    @FXML
    public void onArtworkTypeChange() {
        if (artworkFilterByArtworkTypeTB.isSelected()) {
            ResponsePaginated<Artwork> response = responses.get(currentPage);
            List<Artwork> responseListFiltered = response.getData().stream()
                    .filter(artwork -> artwork.getArtwork_type_id() == artworkTypesCB.getValue()
                            .getId())
                    .collect(Collectors.toList());

            List<String> ids = new ArrayList<>();
            responseListFiltered.forEach((artwork) -> ids.add(String.valueOf(artwork.getId())));

            Predicate<VBox> filter = vbox -> ids.contains(vbox.getId());
            artworkListFiltered.setPredicate(null);
            artworkListFiltered.setPredicate(filter);
        } else {
            artworkListFiltered.setPredicate(null);
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
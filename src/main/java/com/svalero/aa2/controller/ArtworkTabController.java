package com.svalero.aa2.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.ArtworkType;
import com.svalero.aa2.model.ImageAPI;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.task.ArtworkTask;
import com.svalero.aa2.task.ArtworkTaskById;
import com.svalero.aa2.task.ArtworkTaskPage;
import com.svalero.aa2.task.ArtworkTypeTask;
import com.svalero.aa2.task.ArtworkTypeTaskPage;
import com.svalero.aa2.task.ImageTask;
import com.svalero.aa2.util.R;

import io.reactivex.functions.Consumer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ArtworkTabController implements Initializable {
    @FXML
    public ListView<VBox> artworkLV;
    @FXML
    public Pagination artworkPagination;
    @FXML
    public ComboBox<ArtworkType> artworkTypesCB;
    @FXML
    public ToggleButton filterByArtworkTypeTB;
    @FXML
    public ProgressIndicator progressIndicator;
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
        progressIndicator.progressProperty().unbind();
        progressIndicator.progressProperty().setValue(0);

        artworkLV.setItems(artworkListFiltered);
        artworkPagination.currentPageIndexProperty().addListener((observableValue, oldValue, newValue) -> {
            onPageChange((int) newValue);
        });

        Consumer<ResponsePaginated<Artwork>> consumerArtwork = (response) -> {
            Platform.runLater(() -> {
                artworkPagination.setPageCount(response.getPagination().getTotal_pages());
                responses.put(response.getPagination().getCurrent_page(), response);
                currentPage = response.getPagination().getCurrent_page();
                int artworkNumber = 0;
                for (Artwork artwork : response.getData()) {
                    try {
                        artworkNumber++;
                        VBox vbox = showArtwork(artwork, artworkNumber, response.getPagination().getLimit());
                        vbox.setId(String.valueOf(artwork.getId()));
                        artworkList.add(vbox);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                progressIndicator.progressProperty().setValue(0.25);
            });
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

    private void onPageChange(int newPage) {
        artworkList.clear();
        progressIndicator.progressProperty().unbind();
        progressIndicator.progressProperty().setValue(0);

        Consumer<ResponsePaginated<Artwork>> consumer = (response) -> {
            Platform.runLater(() -> {
                responses.put(response.getPagination().getCurrent_page(), response);
                currentPage = response.getPagination().getCurrent_page();
                int artworkNumber = 0;
                for (Artwork artwork : response.getData()) {
                    try {
                        artworkNumber++;
                        VBox vbox = showArtwork(artwork, artworkNumber, response.getPagination().getLimit());
                        vbox.setId(String.valueOf(artwork.getId()));
                        artworkList.add(vbox);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                progressIndicator.progressProperty().setValue(0.25);
            });
        };

        Consumer<Throwable> throwable = (error) -> {
            System.out.println(error.toString());
        };

        ArtworkTaskPage artworkTaskPage = new ArtworkTaskPage(newPage + 1, consumer, throwable);
        new Thread(artworkTaskPage).start();
    }

    @FXML
    public void filterByArtworkType() {
        if (filterByArtworkTypeTB.isSelected() && artworkTypesCB.getValue() != null) {
            ResponsePaginated<Artwork> response = responses.get(currentPage);
            List<String> listIds = response.getData().stream()
                    .filter(artwork -> artwork.getArtwork_type_id() == artworkTypesCB.getValue()
                            .getId())
                    .map(artwork -> String.valueOf(artwork.getId()))
                    .collect(Collectors.toList());

            Predicate<VBox> filter = vbox -> listIds.contains(vbox.getId());
            artworkListFiltered.setPredicate(filter);
        } else {
            artworkListFiltered.setPredicate(null);
        }
    }

    @FXML
    public void onArtworkTypeChange() {
        if (filterByArtworkTypeTB.isSelected() && artworkTypesCB.getValue() != null) {
            ResponsePaginated<Artwork> response = responses.get(currentPage);
            List<String> listIds = response.getData().stream()
                    .filter(artwork -> artwork.getArtwork_type_id() == artworkTypesCB.getValue()
                            .getId())
                    .map(artwork -> String.valueOf(artwork.getId()))
                    .collect(Collectors.toList());

            Predicate<VBox> filter = vbox -> listIds.contains(vbox.getId());
            artworkListFiltered.setPredicate(null);
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
                        artwork = showArtwork(response.getData(), 1, 1);
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

    private VBox showArtwork(Artwork artwork, int artworkNumber, int totalArtworks) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        ArtworkController artworkController = new ArtworkController();
        loader.setLocation(R.getUI("artwork-view.fxml"));
        loader.setController(artworkController);
        VBox artworkPane = loader.load();

        Consumer<Response<ImageAPI>> imageConsumer = (response) -> {
            Platform.runLater(() -> {
                Image image = new Image(response.getData().getIiif_url() +
                        "/full/843,/0/default.jpg");
                artworkController.showArtwork(artwork, image);
                progressIndicator.progressProperty()
                        .setValue(0.25 + artworkNumber * 0.75 / totalArtworks);
            });
        };

        Consumer<Throwable> throwable = (error) -> {
            System.out.println(error.toString());
        };

        if (artwork.getImage_id() != null) {
            ImageTask imageTask = new ImageTask(artwork.getImage_id(), imageConsumer, throwable);
            new Thread(imageTask).start();
        }
        return artworkPane;
    }
}
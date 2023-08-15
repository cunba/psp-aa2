package com.svalero.aa2.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.svalero.aa2.model.Exhibition;
import com.svalero.aa2.model.Gallery;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.task.ExhibitionTask;
import com.svalero.aa2.task.ExhibitionTaskById;
import com.svalero.aa2.task.GalleryTask;
import com.svalero.aa2.task.GalleryTaskPage;
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

public class ExhibitionTabController implements Initializable {
    @FXML
    public ListView<VBox> exhibitionLV;
    @FXML
    public ComboBox<Gallery> galleriesCB;
    @FXML
    public ToggleButton filterByGalleryTB;
    @FXML
    public TextField exhibitionIdTF;
    @FXML
    public Button findExhibitionByIdButton;
    @FXML
    public Pagination exhibitionPagination;

    private ObservableList<VBox> exhibitionList;
    private FilteredList<VBox> exhibitionFilteredList;
    private Map<Integer, ResponsePaginated<Exhibition>> responses;
    private int currentPage;
    private ExhibitionTask exhibitionTask;
    private Stage primaryStage;

    public ExhibitionTabController(Stage primaryStage) {
        exhibitionList = FXCollections.observableArrayList();
        exhibitionFilteredList = new FilteredList<>(exhibitionList);
        this.primaryStage = primaryStage;
        responses = new HashMap<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exhibitionLV.setItems(exhibitionFilteredList);

        Consumer<ResponsePaginated<Exhibition>> consumer = (response) -> {
            responses.put(response.getPagination().getCurrent_page(), response);
            currentPage = response.getPagination().getCurrent_page();
            for (Exhibition exhibition : response.getData()) {
                VBox vbox = showExhibition(exhibition);
                vbox.setId(String.valueOf(exhibition.getId()));
                exhibitionList.add(vbox);
            }

        };

        Consumer<Throwable> throwable = (error) -> {
            System.out.println(error.toString());
        };

        Consumer<ResponsePaginated<Gallery>> consumerGallery = (response) -> {
            for (Gallery gallery : response.getData()) {
                galleriesCB.getItems().add(gallery);
            }

            if (response.getPagination().getNext_url() != null) {
                addGalleryNextPage(response.getPagination().getCurrent_page() + 1);
            }
        };

        GalleryTask galleryTask = new GalleryTask(consumerGallery, throwable);
        new Thread(galleryTask).start();

        exhibitionTask = new ExhibitionTask(consumer, throwable);
        new Thread(exhibitionTask).start();
    }

    private void addGalleryNextPage(int page) {
        Consumer<ResponsePaginated<Gallery>> consumerGallery = (response) -> {
            for (Gallery gallery : response.getData()) {
                galleriesCB.getItems().add(gallery);
            }

            if (response.getPagination().getNext_url() != null) {
                addGalleryNextPage(response.getPagination().getCurrent_page() + 1);
            }
        };

        Consumer<Throwable> throwable = (error) -> {
            System.out.println(error.toString());
        };

        GalleryTaskPage galleryTaskPage = new GalleryTaskPage(page, consumerGallery, throwable);
        new Thread(galleryTaskPage).start();
    }

    @FXML
    public void findExhibitionById(ActionEvent event) {
        try {
            int id = Integer.parseInt(exhibitionIdTF.getText());

            Consumer<Response<Exhibition>> consumer = (response) -> {
                Platform.runLater(() -> {
                    try {
                        Stage stage = new Stage();
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initOwner(primaryStage);

                        VBox exhibition;
                        exhibition = showExhibition(response.getData());
                        Scene scene = new Scene(exhibition);
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            };

            Consumer<Throwable> throwable = (error) -> {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Exhibition with ID " + id + " not found");
                    alert.showAndWait();
                });
            };

            ExhibitionTaskById exhibitionTaskById = new ExhibitionTaskById(id, consumer, throwable);
            new Thread(exhibitionTaskById).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox showExhibition(Exhibition exhibition) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("exhibition-view.fxml"));
        ExhibitionController exhibitionController = new ExhibitionController();
        loader.setController(exhibitionController);
        VBox exhibitionPane = loader.load();
        exhibitionController.showExhibition(exhibition);
        return exhibitionPane;
    }

    @FXML
    public void filterByGallery() {
        if (filterByGalleryTB.isSelected() && galleriesCB.getValue() != null) {
            ResponsePaginated<Exhibition> response = responses.get(currentPage);
            List<String> listIds = response.getData().stream()
                    .filter(exhibition -> exhibition.getGallery_id() == galleriesCB.getValue()
                            .getId())
                    .map(exhibition -> String.valueOf(exhibition.getId()))
                    .collect(Collectors.toList());

            Predicate<VBox> filter = vbox -> listIds.contains(vbox.getId());
            exhibitionFilteredList.setPredicate(filter);
        } else {
            exhibitionFilteredList.setPredicate(null);
        }
    }

    @FXML
    public void onGalleryChange() {
        if (filterByGalleryTB.isSelected() && galleriesCB.getValue() != null) {
            ResponsePaginated<Exhibition> response = responses.get(currentPage);
            List<String> listIds = response.getData().stream()
                    .filter(exhibition -> exhibition.getGallery_id() == galleriesCB.getValue()
                            .getId())
                    .map(exhibition -> String.valueOf(exhibition.getId()))
                    .collect(Collectors.toList());

            Predicate<VBox> filter = vbox -> listIds.contains(vbox.getId());
            exhibitionFilteredList.setPredicate(null);
            exhibitionFilteredList.setPredicate(filter);
        } else {
            exhibitionFilteredList.setPredicate(null);
        }
    }
}
package com.svalero.aa2.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.svalero.aa2.model.Exhibition;
import com.svalero.aa2.model.Gallery;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.task.ExhibitionTask;
import com.svalero.aa2.task.ExhibitionTaskById;
import com.svalero.aa2.task.GalleryTask;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ExhibitionTabController implements Initializable {
    @FXML
    public ListView<VBox> exhibitionLV;
    @FXML
    public ComboBox<Gallery> galleriesCB;
    @FXML
    public ToggleButton filterByGalleryTB;
    @FXML
    public ProgressIndicator exhibitionPI;
    @FXML
    public TextField exhibitionIdTF;
    @FXML
    public Button findExhibitionByIdButton;
    @FXML
    public Pagination exhibitionPagination;

    private ObservableList<VBox> exhibitionList;
    private FilteredList<VBox> exhibitionFilteredList;
    private ObservableMap<Integer, ResponsePaginated<Exhibition>> responses;
    private ObservableList<Gallery> galleriesList;
    private int currentPage;
    private Stage primaryStage;

    public ExhibitionTabController(Stage primaryStage) {
        exhibitionList = FXCollections.observableArrayList();
        exhibitionFilteredList = new FilteredList<>(exhibitionList);
        responses = FXCollections.observableHashMap();
        galleriesList = FXCollections.observableArrayList();
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        exhibitionPI.progressProperty().unbind();
        exhibitionPI.progressProperty().setValue(0);

        exhibitionLV.setItems(exhibitionFilteredList);
        exhibitionPagination.currentPageIndexProperty().addListener((observableValue, oldValue, newValue) -> {
            onPageChange((int) newValue);
        });

        galleriesCB.setItems(galleriesList);

        GalleryTask galleryTask = new GalleryTask(galleriesList, 1);
        new Thread(galleryTask).start();

        ExhibitionTask exhibitionTask = new ExhibitionTask(exhibitionList, responses, 1);
        new Thread(exhibitionTask).start();

        exhibitionTask.messageProperty()
                .addListener((observableValue, oldValue, newValue) -> currentPage = Integer.parseInt(newValue));

        exhibitionTask.progressProperty()
                .addListener(
                        (obervableValue, oldValue, newValue) -> exhibitionPI.progressProperty().setValue(newValue));
    }

    private void onPageChange(int newPage) {
        exhibitionList.clear();
        exhibitionPI.progressProperty().unbind();
        exhibitionPI.progressProperty().setValue(0);

        ExhibitionTask exhibitionTask = new ExhibitionTask(exhibitionList, responses, newPage + 1);
        new Thread(exhibitionTask).start();

        exhibitionTask.messageProperty()
                .addListener((observableValue, oldValue, newValue) -> currentPage = Integer.parseInt(newValue));

        exhibitionTask.progressProperty()
                .addListener(
                        (obervableValue, oldValue, newValue) -> exhibitionPI.progressProperty().setValue(newValue));
    }

    @FXML
    public void findExhibitionById(ActionEvent event) {
        try {
            int id = Integer.parseInt(exhibitionIdTF.getText());
            ExhibitionTaskById exhibitionTaskById = new ExhibitionTaskById(id, primaryStage);
            new Thread(exhibitionTaskById).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
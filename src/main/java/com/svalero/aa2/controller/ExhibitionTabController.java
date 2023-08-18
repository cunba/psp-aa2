package com.svalero.aa2.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.opencsv.CSVWriter;
import com.svalero.aa2.model.Exhibition;
import com.svalero.aa2.model.Gallery;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.task.ExhibitionTask;
import com.svalero.aa2.task.ExhibitionTaskById;
import com.svalero.aa2.task.GalleryTask;
import com.svalero.aa2.util.Util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressIndicator;
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
    public ProgressIndicator exhibitionPI;
    @FXML
    public Button exportToCSVButton;
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
                .addListener((observableValue, oldValue, newValue) -> {
                    String[] value = newValue.split(";");
                    currentPage = Integer.parseInt(value[0]);
                    exhibitionPagination.setPageCount(Integer.parseInt(value[1]));
                });

        exhibitionPI.progressProperty().bind(exhibitionTask.progressProperty());
    }

    private void onPageChange(int newPage) {
        exhibitionList.clear();
        exhibitionPI.progressProperty().unbind();
        exhibitionPI.progressProperty().setValue(0);

        ExhibitionTask exhibitionTask = new ExhibitionTask(exhibitionList, responses, newPage + 1);
        new Thread(exhibitionTask).start();

        exhibitionTask.messageProperty()
                .addListener((observableValue, oldValue,
                        newValue) -> currentPage = Integer.parseInt(newValue.split(";")[0]));

        exhibitionPI.progressProperty().bind(exhibitionTask.progressProperty());
    }

    @FXML
    public void findExhibitionById(ActionEvent event) {
        try {
            int id = Integer.parseInt(exhibitionIdTF.getText());
            ExhibitionTaskById exhibitionTaskById = new ExhibitionTaskById(id);
            new Thread(exhibitionTaskById).start();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(primaryStage);

            exhibitionTaskById.valueProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue != null) {
                    Scene scene = new Scene(newValue);
                    stage.setScene(scene);
                    stage.show();
                }
            });
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

    @FXML
    public void onExportToCSVButtonClick() {
        if (exhibitionPI.getProgress() != 1.0) {
            Alert alert = new Alert(AlertType.INFORMATION, "The data is not compleatly loaded, try again.");
            alert.show();
        } else {
            String fileName = "exhibition_page_" + currentPage + ".csv";
            File outputFile = new File(
                    System.getProperty("user.dir") + System.getProperty("file.separator") + fileName);

            try {
                FileWriter writer = new FileWriter(outputFile);
                CSVWriter csvWriter = new CSVWriter(writer, ';', CSVWriter.DEFAULT_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

                List<String[]> dataToCSV = new ArrayList<>();
                ResponsePaginated<Exhibition> exhibitions = responses.get(currentPage);
                Util util = new Util();
                dataToCSV.add(util.exportExhibitionToCSVHeaders());
                for (Exhibition exhibition : exhibitions.getData()) {
                    dataToCSV.add(exhibition.exportToCSV());
                }

                csvWriter.writeAll(dataToCSV);
                csvWriter.close();
                Alert alert = new Alert(AlertType.INFORMATION, "Page exported to CSV " + fileName);
                alert.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
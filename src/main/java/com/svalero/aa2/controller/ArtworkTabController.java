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
import com.svalero.aa2.model.Artwork;
import com.svalero.aa2.model.ArtworkType;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.task.ArtworkTask;
import com.svalero.aa2.task.ArtworkTaskById;
import com.svalero.aa2.task.ArtworkTypeTask;
import com.svalero.aa2.util.Util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    public ProgressIndicator artworkPI;
    @FXML
    public Button exportToCSVButton;
    @FXML
    public TextField artworkIdTF;
    @FXML
    public Button findArtworkByIdButton;

    private ObservableList<VBox> artworkList;
    private FilteredList<VBox> artworkListFiltered;
    private ObservableMap<Integer, ResponsePaginated<Artwork>> responses;
    private Stage primaryStage;
    private int currentPage;
    private ObservableList<ArtworkType> artworkTypesList;

    public ArtworkTabController(Stage stage) {
        artworkList = FXCollections.observableArrayList();
        artworkListFiltered = new FilteredList<>(artworkList);
        artworkTypesList = FXCollections.observableArrayList();
        responses = FXCollections.observableHashMap();
        primaryStage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        artworkPI.progressProperty().unbind();

        artworkLV.setItems(artworkListFiltered);
        artworkPagination.currentPageIndexProperty().addListener((observableValue, oldValue, newValue) -> {
            onPageChange((int) newValue);
        });

        artworkTypesCB.setItems(artworkTypesList);

        ArtworkTypeTask artworkTypeTask = new ArtworkTypeTask(artworkTypesList, 1);
        new Thread(artworkTypeTask).start();

        ArtworkTask artworkTask = new ArtworkTask(artworkList, responses, 1);
        new Thread(artworkTask).start();

        artworkTask.messageProperty()
                .addListener((observableValue, oldValue, newValue) -> {
                    String[] value = newValue.split(";");
                    currentPage = Integer.parseInt(value[0]);
                    artworkPagination.setPageCount(Integer.parseInt(value[1]));
                });

        artworkPI.progressProperty().bind(artworkTask.progressProperty());
    }

    private void onPageChange(int newPage) {
        artworkList.clear();
        artworkPI.progressProperty().unbind();

        ArtworkTask artworkTask = new ArtworkTask(artworkList, responses, newPage + 1);
        new Thread(artworkTask).start();

        artworkTask.messageProperty()
                .addListener((observableValue, oldValue,
                        newValue) -> currentPage = Integer.parseInt(newValue.split(";")[0]));

        artworkPI.progressProperty().bind(artworkTask.progressProperty());
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
    public void onExportToCSVButtonClick() {
        String fileName = "artwork_page_" + currentPage + ".csv";
        File outputFile = new File(
                System.getProperty("user.dir") + System.getProperty("file.separator") + fileName);

        try {
            FileWriter writer = new FileWriter(outputFile);
            CSVWriter csvWriter = new CSVWriter(writer, ';', CSVWriter.DEFAULT_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);

            List<String[]> dataToCSV = new ArrayList<>();
            ResponsePaginated<Artwork> artworks = responses.get(currentPage);
            Util util = new Util();
            dataToCSV.add(util.exportArtworkToCSVHeaders());
            for (Artwork artwork : artworks.getData()) {
                dataToCSV.add(artwork.exportToCSV());
            }

            csvWriter.writeAll(dataToCSV);
            csvWriter.close();
            Alert alert = new Alert(AlertType.INFORMATION, "Page exported to CSV" + fileName);
            alert.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void findArtworkById() {
        try {
            int id = Integer.parseInt(artworkIdTF.getText());
            ArtworkTaskById artworkTaskById = new ArtworkTaskById(id, primaryStage);
            new Thread(artworkTaskById).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
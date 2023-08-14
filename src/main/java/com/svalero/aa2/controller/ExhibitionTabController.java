package com.svalero.aa2.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.svalero.aa2.model.Exhibition;
import com.svalero.aa2.model.Response;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.task.ExhibitionTask;
import com.svalero.aa2.task.ExhibitionTaskById;
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
    public ToggleButton exhibitionSortByTitleTB;
    @FXML
    public ToggleButton exhibitionSortByGalleryTB;
    @FXML
    public ToggleButton exhibitionSortByOpenDateTB;
    @FXML
    public TextField exhibitionIdTF;
    @FXML
    public Button findExhibitionByIdButton;
    @FXML
    public Pagination exhibitionPagination;

    private Boolean sortExhibitionsPageByTitlePressed = false;
    private Boolean sortExhibitionsPageByGalleryPressed = false;
    private Boolean sortExhibitionsPageByOpenDatePressed = false;

    private ObservableList<VBox> exhibitionList;
    private ExhibitionTask exhibitionTask;
    private Stage primaryStage;

    // private ExhibitionTaskById exhibitionTaskById;
    // private ArtworkTaskById artworkTaskById;

    public ExhibitionTabController(Stage primaryStage) {
        exhibitionList = FXCollections.observableArrayList();
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        exhibitionLV.setItems(exhibitionList);

        Consumer<ResponsePaginated<Exhibition>> consumer = (response) -> {
            for (Exhibition exhibition : response.getData()) {
                VBox vbox = showExhibition(exhibition);
                exhibitionList.add(vbox);
            }

        };

        Consumer<Throwable> throwable = (error) -> {
            System.out.println(error.toString());
        };

        exhibitionTask = new ExhibitionTask(consumer, throwable);
        new Thread(exhibitionTask).start();
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
}
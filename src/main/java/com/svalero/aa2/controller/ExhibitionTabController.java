package com.svalero.aa2.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.svalero.aa2.model.Exhibition;
import com.svalero.aa2.model.ResponsePaginated;
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
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class ExhibitionTabController implements Initializable {
    @FXML
    public ListView<VBox> exhibitionLV;

    private Boolean sortExhibitionsPageByTitlePressed = false;
    private Boolean sortExhibitionsPageByGalleryPressed = false;
    private Boolean sortExhibitionsPageByOpenDatePressed = false;

    private ObservableList<VBox> exhibitionList;

    private ExhibitionTask exhibitionTask;

    // private ExhibitionTaskById exhibitionTaskById;
    // private ArtworkTaskById artworkTaskById;

    public ExhibitionTabController() {
        exhibitionList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        exhibitionLV.setItems(exhibitionList);

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

        Consumer<ResponsePaginated<Exhibition>> consumer = (response) -> {
            System.out.println("Entra");
            for (Exhibition exhibition : response.getData()) {
                VBox vbox = initializeExhibitionScene(exhibition);
                exhibitionList.add(vbox);
            }

        };

        exhibitionTask = new ExhibitionTask(consumer, null);
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

    }

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
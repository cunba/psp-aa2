package com.svalero.aa2.task;

import java.io.IOException;

import com.svalero.aa2.controller.ExhibitionController;
import com.svalero.aa2.model.Exhibition;
import com.svalero.aa2.model.ResponsePaginated;
import com.svalero.aa2.service.ArtService;
import com.svalero.aa2.util.R;

import io.reactivex.functions.Consumer;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class ExhibitionTask extends Task<Integer> {
    private ObservableList<VBox> exhibitions;
    private ObservableMap<Integer, ResponsePaginated<Exhibition>> responses;
    private int page;

    public ExhibitionTask(ObservableList<VBox> exhibitions,
            ObservableMap<Integer, ResponsePaginated<Exhibition>> responses,
            int page) {

        this.exhibitions = exhibitions;
        this.responses = responses;
        this.page = page;
    }

    @Override
    protected Integer call() throws Exception {
        Consumer<ResponsePaginated<Exhibition>> consumer = (response) -> {
            responses.put(response.getPagination().getCurrent_page(), response);
            updateMessage(String.valueOf(response.getPagination().getCurrent_page()));
            int exhibitionNumber = 0;
            for (Exhibition exhibition : response.getData()) {
                exhibitionNumber++;
                createVBox(exhibition, exhibitionNumber, response.getPagination().getLimit());
            }
        };

        Consumer<Throwable> throwable = (error) -> {
            System.out.println(error.toString());
        };

        ArtService artService = new ArtService();
        if (page == 1)
            artService.getAllExhibitions().subscribe(consumer, throwable);
        else
            artService.getAllExhibitionsPage(page).subscribe(consumer, throwable);

        return null;
    }

    private void createVBox(Exhibition exhibition, int exhibitionNumber, int totalExhibitions) {

        FXMLLoader loader = new FXMLLoader();
        ExhibitionController exhibitionController = new ExhibitionController();
        loader.setLocation(R.getUI("exhibition-view.fxml"));
        loader.setController(exhibitionController);

        if (exhibition.getImage_id() != null) {
            ImageTask imageTask = new ImageTask(exhibition.getImage_id());
            new Thread(imageTask).start();

            imageTask.messageProperty().addListener((observableValue, oldValue, newValue) -> {
                try {
                    Image image = new Image(R.getImage("noImage.png"));
                    exhibitionController.showExhibition(exhibition, image);
                    VBox vbox = loader.load();
                    vbox.setId(String.valueOf(exhibition.getId()));
                    exhibitions.add(vbox);
                    updateProgress(exhibitionNumber / totalExhibitions, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        } else {
            Image image = new Image(R.getImage("noImage.png"));
            exhibitionController.showExhibition(exhibition, image);
            VBox vbox;
            try {
                vbox = loader.load();
                vbox.setId(String.valueOf(exhibition.getId()));
                exhibitions.add(vbox);
                updateProgress(exhibitionNumber / totalExhibitions, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

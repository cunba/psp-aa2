package com.svalero.aa2;

import com.svalero.aa2.controller.ArtworkController;
import com.svalero.aa2.util.R;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("artwork-view.fxml"));
        ArtworkController artworkController = new ArtworkController();
        loader.setController(artworkController);
        VBox mainPane = loader.load();
        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Art Institute of Chicago API");
        primaryStage.show();
        artworkController.searchArtwork(new ActionEvent());
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }

}

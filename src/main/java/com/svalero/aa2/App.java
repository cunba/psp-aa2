package com.svalero.aa2;

import com.svalero.aa2.controller.MainController;
import com.svalero.aa2.util.R;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUI("main-view.fxml"));
        loader.setController(new MainController(primaryStage));
        TabPane mainPane = loader.load();
        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Art Institute of Chicago API");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }

    public void openURL(String url) {
        getHostServices().showDocument(url);
    }

}

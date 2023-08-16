package com.svalero.aa2.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.svalero.aa2.util.R;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController implements Initializable {
    @FXML
    public Tab artworksTab;
    @FXML
    public Tab exhibitionsTab;

    private Stage primaryStage;

    public MainController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMLLoader loaderArtworksTab = new FXMLLoader();
        FXMLLoader loaderExhibitionsTab = new FXMLLoader();

        ArtworkTabController artworkTabController = new ArtworkTabController(primaryStage);
        loaderArtworksTab.setLocation(R.getUI("artwork_tab-view.fxml"));
        loaderArtworksTab.setController(artworkTabController);

        ExhibitionTabController exhibitionTabController = new ExhibitionTabController(primaryStage);
        loaderExhibitionsTab.setLocation(R.getUI("exhibition_tab-view.fxml"));
        loaderExhibitionsTab.setController(exhibitionTabController);

        try {
            VBox artworks = loaderArtworksTab.load();
            artworksTab.setContent(artworks);
            VBox exhibitions = loaderExhibitionsTab.load();
            exhibitionsTab.setContent(exhibitions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
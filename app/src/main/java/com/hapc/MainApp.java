package com.hapc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scene.fxml"));
        Scene scene = new Scene(loader.load(), 1250, 800);
        stage.setTitle("Historical Artifact Catalog");
        stage.setScene(scene);
        stage.show();

    }
    public String getGreeting() {
        return "Welcome to Historical Artifact Catalog";
    }

    public static void main(String[] args) {
        System.out.println(new MainApp().getGreeting());
        launch(args);
    }


}

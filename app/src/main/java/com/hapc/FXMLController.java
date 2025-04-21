package com.hapc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.List;

public class FXMLController {

    @FXML
    private ListView<com.hapc.Artifact> artifactListView;

    @FXML
    private Button addButton;

    @FXML
    private Button saveButton;

    private final ObservableList<com.hapc.Artifact> artifacts = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            List<com.hapc.Artifact> loaded = com.hapc.JSONManager.load();
            artifacts.addAll(loaded);
            artifactListView.setItems(artifacts);
        } catch (Exception e) {
            e.printStackTrace();
        }

        addButton.setOnAction(event -> {
            com.hapc.Artifact newArtifact = new com.hapc.Artifact("artifact ID" + (artifacts.size() + 1),
                    "artifact Name", "category", "civilization", "discoveryLocation",
                    "composition","discoveryDate","currentPlace", "dimensions","weight","tags");
            artifacts.add(newArtifact);
        });

        saveButton.setOnAction(event -> {
            try {
                com.hapc.JSONManager.save(artifacts);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

package com.hapc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

public class FXMLController {

    @FXML
    private ListView<Artifact> artifactListView;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button saveButton;

    @FXML
    private TextField artifactIdField;

    @FXML
    private TextField artifactNameField;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField civilizationField;

    @FXML
    private TextField discoveryLocationField;

    @FXML
    private TextField compositionField;

    @FXML
    private TextField discoveryDateField;

    @FXML
    private TextField currentPlaceField;

    @FXML
    private TextField dimensionsField;

    @FXML
    private TextField weightField;

    @FXML
    private TextField tagsField;

    private final ObservableList<Artifact> artifacts = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            List<Artifact> loaded = JSONManager.load();
            artifacts.addAll(loaded);
            artifactListView.setItems(artifacts);
        } catch (Exception e) {
            e.printStackTrace();
        }

        artifactListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                artifactIdField.setText(newVal.getArtifactId());
                artifactNameField.setText(newVal.getArtifactName());
                categoryField.setText(newVal.getCategory());
                civilizationField.setText(newVal.getCivilization());
                discoveryLocationField.setText(newVal.getDiscoveryLocation());
                compositionField.setText(newVal.getComposition());
                discoveryDateField.setText(newVal.getDiscoveryDate());
                currentPlaceField.setText(newVal.getCurrentPlace());
                dimensionsField.setText(newVal.getDimensions());
                weightField.setText(newVal.getWeight());
                tagsField.setText(newVal.getTags());
            }
        });

        addButton.setOnAction(event -> {
            Artifact newArtifact = new Artifact(
                    artifactIdField.getText(),
                    artifactNameField.getText(),
                    categoryField.getText(),
                    civilizationField.getText(),
                    discoveryLocationField.getText(),
                    compositionField.getText(),
                    discoveryDateField.getText(),
                    currentPlaceField.getText(),
                    dimensionsField.getText(),
                    weightField.getText(),
                    tagsField.getText()
            );
            artifacts.add(newArtifact);
            clearInputs();
        });

        editButton.setOnAction(event -> {
            Artifact selected = artifactListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setArtifactId(artifactIdField.getText());
                selected.setArtifactName(artifactNameField.getText());
                selected.setCategory(categoryField.getText());
                selected.setCivilization(civilizationField.getText());
                selected.setDiscoveryLocation(discoveryLocationField.getText());
                selected.setComposition(compositionField.getText());
                selected.setDiscoveryDate(discoveryDateField.getText());
                selected.setCurrentPlace(currentPlaceField.getText());
                selected.setDimensions(dimensionsField.getText());
                selected.setWeight(weightField.getText());
                selected.setTags(tagsField.getText());
                artifactListView.refresh();
                clearInputs();
            }
        });

        deleteButton.setOnAction(event -> {
            Artifact selected = artifactListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                artifacts.remove(selected);
                clearInputs();
            }
        });

        saveButton.setOnAction(event -> {
            try {
                JSONManager.save(artifacts);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void clearInputs() {
        artifactIdField.clear();
        artifactNameField.clear();
        categoryField.clear();
        civilizationField.clear();
        discoveryLocationField.clear();
        compositionField.clear();
        discoveryDateField.clear();
        currentPlaceField.clear();
        dimensionsField.clear();
        weightField.clear();
        tagsField.clear();
    }
}


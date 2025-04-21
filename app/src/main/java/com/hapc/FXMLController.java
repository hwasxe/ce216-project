package com.hapc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.List;

public class FXMLController {

    @FXML
    private ListView<Artifact> artifactListView;

    @FXML
    private Button addButton;

    @FXML
    private Button saveButton;

    @FXML
    private TextField searchField;

    @FXML
    private FlowPane tagsContainer;

    private final ObservableList<Artifact> artifacts = FXCollections.observableArrayList();
    private final ObservableList<Artifact> filteredArtifacts = FXCollections.observableArrayList();
    private final SearchService searchService = new SearchService();
    private final List<String> selectedTags = new ArrayList<>();

    @FXML
    public void initialize() {
        try {
            List<Artifact> loaded = JSONManager.load();
            artifacts.addAll(loaded);
            filteredArtifacts.addAll(loaded);
            artifactListView.setItems(filteredArtifacts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Inside the initialize() method, after setting up the addButton
        saveButton.setOnAction(event -> {
            try {
                JSONManager.save(artifacts);
                System.out.println("Artifacts saved successfully!");
            } catch (Exception e) {
                System.err.println("Error saving artifacts: " + e.getMessage());
                e.printStackTrace();
            }
        });

        // Setup Add and Save buttons
        addButton.setOnAction(event -> {
            String[] civilizations = {"Roman Empire", "Ancient Greek", "Egyptian", "Mayan"};
            String[] categories = {"Sculpture", "Weapon", "Manuscript", "Jewelry"};
            String[] materials = {"Marble", "Gold", "Bronze", "Clay", "Papyrus"};
            String[] tagSets = {
                    "ancient,valuable,religious",
                    "historical,warfare,ceremonial",
                    "rare,decorative,royal",
                    "cultural,handcrafted,symbolic"
            };

            int index = artifacts.size();
            Artifact newArtifact = new Artifact(
                    "artifact" + (index + 1),
                    "Artifact " + (index + 1) + " - " + civilizations[index % civilizations.length],
                    categories[index % categories.length],
                    civilizations[index % civilizations.length],
                    "Location " + (index + 1),
                    materials[index % materials.length],
                    "2024-04-22",
                    "Museum of " + civilizations[index % civilizations.length] + " History",
                    "10x20x30 cm",
                    (5 + index) + "kg",
                    tagSets[index % tagSets.length]
            );
            artifacts.add(newArtifact);
            updateFilteredList();
            updateTagFilters(); // Refresh tag filters when new artifact is added
        });

        // Setup search functionality
        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                updateFilteredList();
            });
        }

        // Setup tag filters
        updateTagFilters();
    }

    private void updateTagFilters() {
        if (tagsContainer != null) {
            tagsContainer.getChildren().clear();

            List<String> allTags = searchService.extractAllTags(artifacts);
            for (String tag : allTags) {
                CheckBox tagCheckBox = new CheckBox(tag);
                tagCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal) {
                        selectedTags.add(tag);
                    } else {
                        selectedTags.remove(tag);
                    }
                    updateFilteredList();
                });
                tagsContainer.getChildren().add(tagCheckBox);
            }
        }
    }

    private void updateFilteredList() {
        // Apply both search and tag filtering
        List<Artifact> searchResults = searchService.searchArtifacts(artifacts,
                searchField != null ? searchField.getText() : "");

        List<Artifact> tagFilteredResults = searchService.filterByTags(searchResults, selectedTags);

        filteredArtifacts.clear();
        filteredArtifacts.addAll(tagFilteredResults);
    }
}
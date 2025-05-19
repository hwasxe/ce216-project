package com.hapc;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FXMLController {

    @FXML
    private TableView<Artifact> artifactTable;

    @FXML
    private TableColumn<Artifact, String> artifactIdColumn;

    @FXML
    private TableColumn<Artifact, String> artifactNameColumn;

    @FXML
    private TableColumn<Artifact, String> categoryColumn;

    @FXML
    private TableColumn<Artifact, String> civilizationColumn;

    @FXML
    private TableColumn<Artifact, String> discoveryLocationColumn;

    @FXML
    private TableColumn<Artifact, String> compositionColumn;

    @FXML
    private TableColumn<Artifact, String> discoveryDateColumn;

    @FXML
    private TableColumn<Artifact, String> currentPlaceColumn;

    @FXML
    private TableColumn<Artifact, String> dimensionsColumn;

    @FXML
    private TableColumn<Artifact, String> weightColumn;

    @FXML
    private TableColumn<Artifact, String> tagsColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;


    @FXML
    private Button imageSelectButton;

    @FXML
    private Button saveButton;

    @FXML
    private Label imagePathLabel;

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

    @FXML
    private Label activeFiltersLabel;

    @FXML
    private Button tagFilterButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button submitButton;

    @FXML
    private Button cancelButton;

    @FXML
    private VBox formPanel;

    @FXML
    private TextField searchField;
    @FXML
    private String selectedImagePath = null;


    private final ObservableList<Artifact> artifacts = FXCollections.observableArrayList();
    private final ObservableList<Artifact> allArtifacts = FXCollections.observableArrayList();

    private boolean editing = false;
    private Artifact artifactBeingEdited = null;

    @FXML
    public void initialize() {
        System.out.println("initialize() çalıştı.");
        try {
            List<Artifact> loaded = JSONManager.load();
            allArtifacts.addAll(loaded);
            artifacts.addAll(loaded);
            artifactTable.setItems(artifacts);
        } catch (Exception e) {
            e.printStackTrace();
        }

        artifactIdColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getArtifactId()));
        artifactNameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getArtifactName()));
        categoryColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCategory()));
        civilizationColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCivilization()));
        discoveryLocationColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDiscoveryLocation()));
        compositionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getComposition()));
        discoveryDateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDiscoveryDate()));
        currentPlaceColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCurrentPlace()));
        dimensionsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDimensions()));
        weightColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getWeight()));
        tagsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTags()));

        artifactTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                populateInputs(newVal);
            }
        });

        searchButton.setOnAction(e -> performSearch());
        searchField.setOnAction(e -> performSearch());

        addButton.setOnAction(event -> {
            editing = false;
            artifactBeingEdited = null;
            clearInputs();
            showForm(true);
        });

        editButton.setOnAction(event -> {
            Artifact selected = artifactTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                editing = true;
                artifactBeingEdited = selected;
                populateInputs(selected);
                showForm(true);

                artifactBeingEdited.setImagePath(selectedImagePath);

            }
        });

        setupTagFilterButton();

        deleteButton.setOnAction(event -> {
            Artifact selected = artifactTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                artifacts.remove(selected);
                allArtifacts.remove(selected);
                artifactTable.getSelectionModel().clearSelection();
            }
        });

        imageSelectButton.setOnAction(e -> {
            System.out.println("Select Image butonuna basıldı.");
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                File imagesDir = new File(System.getProperty("user.dir") + "/src/images");
                System.out.println("images dizini oluşturuldu: " + imagesDir.getAbsolutePath());

            if (!imagesDir.exists()) {
                    imagesDir.mkdirs();
                    System.out.println("images dizini oluşturuldu: " + imagesDir.getAbsolutePath());}

                File destFile = new File(imagesDir, selectedFile.getName());
                try {
                    Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    selectedImagePath = "src/images/" + selectedFile.getName();
                    imagePathLabel.setText("Selected: " + selectedFile.getName());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        saveButton.setOnAction(event -> {
            try {
                JSONManager.save(allArtifacts);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        submitButton.setOnAction(event -> {
            if (editing && artifactBeingEdited != null) {
                artifactBeingEdited.setImagePath(selectedImagePath);
                updateArtifact(artifactBeingEdited);
                artifactTable.refresh();
            } else {
                Artifact newArtifact = createArtifactFromInputs();
                artifacts.add(newArtifact);
                allArtifacts.add(newArtifact);
            }
            showForm(false);
            clearInputs();
        });

        cancelButton.setOnAction(event -> {
            clearInputs();
            showForm(false);
        });

        artifactTable.setRowFactory(tv -> {
            TableRow<Artifact> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Artifact selectedArtifact = row.getItem();
                    showArtifactDetails(selectedArtifact);
                }
            });
            return row;
        });


    }


    private void performSearch() {
        String keyword = searchField.getText().toLowerCase().trim();
        if (keyword.isEmpty()) {
            artifacts.setAll(allArtifacts);
        } else {
            List<Artifact> filtered = allArtifacts.stream().filter(a ->
                    a.getArtifactId().toLowerCase().contains(keyword) ||
                            a.getArtifactName().toLowerCase().contains(keyword) ||
                            a.getCategory().toLowerCase().contains(keyword) ||
                            a.getCivilization().toLowerCase().contains(keyword) ||
                            a.getDiscoveryLocation().toLowerCase().contains(keyword) ||
                            a.getComposition().toLowerCase().contains(keyword) ||
                            a.getDiscoveryDate().toLowerCase().contains(keyword) ||
                            a.getCurrentPlace().toLowerCase().contains(keyword) ||
                            a.getDimensions().toLowerCase().contains(keyword) ||
                            a.getWeight().toLowerCase().contains(keyword) ||
                            a.getTags().toLowerCase().contains(keyword)
            ).toList();
            artifacts.setAll(filtered);
        }
    }
    private Set<String> extractAllTags() {
        Set<String> allTags = new HashSet<>();

        for (Artifact artifact : allArtifacts) {
            String tagString = artifact.getTags();
            if (tagString != null && !tagString.isEmpty()) {
                String[] tags = tagString.split(",");
                for (String tag : tags) {
                    String trimmedTag = tag.trim();
                    if (!trimmedTag.isEmpty()) {
                        allTags.add(trimmedTag);
                    }
                }
            }
        }

        return allTags;
    }

    private void setupTagFilterButton() {
        tagFilterButton.setOnAction(event -> showTagFilterDialog());
    }

    private void showTagFilterDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Filter by Tags");
        dialog.initModality(Modality.APPLICATION_MODAL);

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setPrefWidth(300);

        Label instructions = new Label("Select tags to filter artifacts:");

        Set<String> allTags = extractAllTags();
        List<CheckBox> checkBoxes = new ArrayList<>();

        for (String tag : allTags) {
            CheckBox cb = new CheckBox(tag);
            checkBoxes.add(cb);
            content.getChildren().add(cb);
        }

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button applyButton = new Button("Apply Filter");
        Button cancelButton = new Button("Cancel");

        buttonBox.getChildren().addAll(cancelButton, applyButton);

        applyButton.setOnAction(e -> {
            Set<String> selectedTags = new HashSet<>();
            for (CheckBox cb : checkBoxes) {
                if (cb.isSelected()) {
                    selectedTags.add(cb.getText());
                }
            }

            if (selectedTags.isEmpty()) {
                artifacts.setAll(allArtifacts);
                activeFiltersLabel.setText("Active filters: None");
            } else {
                List<Artifact> filtered = allArtifacts.stream().filter(a -> {
                    String artifactTags = a.getTags();
                    if (artifactTags == null || artifactTags.isEmpty()) {
                        return false;
                    }

                    for (String tag : selectedTags) {
                        if (artifactTags.toLowerCase().contains(tag.toLowerCase())) {
                            return true;
                        }
                    }
                    return false;
                }).toList();

                artifacts.setAll(filtered);
                activeFiltersLabel.setText("Active filters: " + String.join(", ", selectedTags));
            }

            dialog.close();
        });

        cancelButton.setOnAction(e -> dialog.close());

        content.getChildren().add(buttonBox);

        Scene scene = new Scene(content);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void showForm(boolean show) {
        formPanel.setVisible(show);
        formPanel.setManaged(show);
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

    private void populateInputs(Artifact artifact) {
        artifactIdField.setText(artifact.getArtifactId());
        artifactNameField.setText(artifact.getArtifactName());
        categoryField.setText(artifact.getCategory());
        civilizationField.setText(artifact.getCivilization());
        discoveryLocationField.setText(artifact.getDiscoveryLocation());
        compositionField.setText(artifact.getComposition());
        discoveryDateField.setText(artifact.getDiscoveryDate());
        currentPlaceField.setText(artifact.getCurrentPlace());
        dimensionsField.setText(artifact.getDimensions());
        weightField.setText(artifact.getWeight());
        tagsField.setText(artifact.getTags());
    }

    private Artifact createArtifactFromInputs() {
        return new Artifact(
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
                tagsField.getText(),
                selectedImagePath != null ? selectedImagePath:""
        );
    }

    private void updateArtifact(Artifact artifact) {
        artifact.setArtifactId(artifactIdField.getText());
        artifact.setArtifactName(artifactNameField.getText());
        artifact.setCategory(categoryField.getText());
        artifact.setCivilization(civilizationField.getText());
        artifact.setDiscoveryLocation(discoveryLocationField.getText());
        artifact.setComposition(compositionField.getText());
        artifact.setDiscoveryDate(discoveryDateField.getText());
        artifact.setCurrentPlace(currentPlaceField.getText());
        artifact.setDimensions(dimensionsField.getText());
        artifact.setWeight(weightField.getText());
        artifact.setTags(tagsField.getText());
    }

    private void showArtifactDetails(Artifact artifact) {
        Stage detailStage = new Stage();
        detailStage.setTitle("Artifact Details");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(15));

        Label idLabel = new Label("ID: " + artifact.getArtifactId());
        Label nameLabel = new Label("Name: " + artifact.getArtifactName());
        Label categoryLabel = new Label("Category: " + artifact.getCategory());
        Label civLabel = new Label("Civilization: " + artifact.getCivilization());
        Label locLabel = new Label("Discovery Location: " + artifact.getDiscoveryLocation());
        Label composition = new Label("Composition: " + artifact.getComposition());
        Label discoveryDate = new Label("Discovery Date: " + artifact.getDiscoveryDate());
        Label currentPlace = new Label("Current Location: " + artifact.getCurrentPlace());
        Label dimensions = new Label("Dimensions: " + artifact.getDimensions());
        Label weight = new Label("Weight: " + artifact.getWeight());
        Label tags = new Label("Tags: " + artifact.getTags());

        layout.getChildren().addAll(idLabel, nameLabel, categoryLabel, civLabel, locLabel, composition, discoveryDate,
                 currentPlace, dimensions, weight, tags);

        if (artifact.getImagePath() != null && !artifact.getImagePath().isEmpty()) {
            File imgFile = new File(System.getProperty("user.dir"), artifact.getImagePath());
            if (imgFile.exists()) {
                Image image = new Image(imgFile.toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(200);
                imageView.setPreserveRatio(true);
                layout.getChildren().add(imageView);
            } else {
                System.out.println("Imahe couldnt not be found: " + imgFile.getAbsolutePath());
            }
        }

        Scene scene = new Scene(layout, 500, 600);
        detailStage.setScene(scene);
        detailStage.initModality(Modality.APPLICATION_MODAL); // focus'u kilitle
        detailStage.showAndWait();


    }

    @FXML
    private void showHelp() {
        Stage helpStage = new Stage();
        helpStage.setTitle("User Manual");

        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 20px;");
        content.setPrefWidth(600);
        content.setPrefHeight(400);

        Label titleLabel = new Label("Historical Artifact Catalog - User Manual");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextArea helpText = new TextArea();
        helpText.setEditable(false);
        helpText.setWrapText(true);
        helpText.setText(
                "HISTORICAL ARTIFACT CATALOG APPLICATION\n\n" +
                        "This application allows you to manage historical artifacts. Here's how to use it:\n\n" +
                        "BASIC OPERATIONS:\n" +
                        "- Add: Click the 'Add' button to create a new artifact entry\n" +
                        "- Edit: Select an artifact from the table and click 'Edit'\n" +
                        "- Delete: Select an artifact and click 'Delete' to remove it\n" +
                        "- Save: Click 'Save' to save all changes to the catalog file\n\n" +
                        "SEARCH:\n" +
                        "- Use the search box at the top to filter artifacts by any field\n" +
                        "- The search works across all properties including tags\n\n" +
                        "ARTIFACT PROPERTIES:\n" +
                        "- Artifact ID: A unique identifier for the artifact\n" +
                        "- Artifact Name: The name or title of the artifact\n" +
                        "- Category: The type (Sculpture, Manuscript, Weapon, Tool, Jewelry)\n" +
                        "- Civilization: The civilization or culture that created the artifact\n" +
                        "- Discovery Location: Where the artifact was found\n" +
                        "- Composition: Material makeup (Gold, Clay, Papyrus, Stone, Wood, etc.)\n" +
                        "- Discovery Date: When the artifact was discovered\n" +
                        "- Current Place: Current location (museum, collection, etc.)\n" +
                        "- Dimensions: Physical size of the artifact\n" +
                        "- Weight: Weight of the artifact\n" +
                        "- Tags: Keywords to categorize and search for artifacts"
        );

        VBox.setVgrow(helpText, Priority.ALWAYS);

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> helpStage.close());

        content.getChildren().addAll(titleLabel, helpText, closeButton);

        Scene scene = new Scene(content);
        helpStage.setScene(scene);
        helpStage.show();
    }

}


module CatalogApp {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.hapc to javafx.fxml;
    exports com.hapc;
}
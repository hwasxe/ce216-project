module CatalogApp {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.hacp to javafx.fxml;
    exports com.hacp;
}
module com.hacp {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;

    exports com.hacp;
    opens com.hacp to javafx.fxml, com.fasterxml.jackson.databind;
}

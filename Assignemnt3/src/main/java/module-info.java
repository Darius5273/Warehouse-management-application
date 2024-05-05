module com.example.assignemnt3 {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.logging;
    requires java.sql;
    requires java.desktop;

    exports Presentation;
    opens Presentation to javafx.fxml;
}
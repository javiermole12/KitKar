module com.example.demofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens com.example.sample to javafx.fxml;
    exports com.example.sample;
    exports com.example.sample.Clases;
    opens com.example.sample.Clases to javafx.fxml;
}
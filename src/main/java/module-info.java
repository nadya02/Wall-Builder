module com.example.wallbuilding {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.wallbuilding to javafx.fxml;
    exports com.example.wallbuilding;
}
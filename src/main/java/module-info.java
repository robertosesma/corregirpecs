module com.leam.corregirpecs {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;

    opens com.leam.corregirpecs to javafx.fxml;
    exports com.leam.corregirpecs;
}

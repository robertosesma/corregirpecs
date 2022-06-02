module com.leam.corregirpecs {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
	requires javafx.graphics;
	requires java.desktop;

    opens com.leam.corregirpecs to javafx.fxml;
    exports com.leam.corregirpecs;
}

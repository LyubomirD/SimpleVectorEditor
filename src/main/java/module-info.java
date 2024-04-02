module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.desktop;
    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires javafx.swing;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}


module com.dysprojekt.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.desktop;
    requires javafx.graphics;


    opens com.dysprojekt.javafx to javafx.fxml;
    exports com.dysprojekt.javafx;
}
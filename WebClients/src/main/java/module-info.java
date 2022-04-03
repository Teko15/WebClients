module zad02.webclients {
    requires javafx.controls;
    requires javafx.fxml;
    requires gson;
    requires javafx.web;


    opens zad02 to javafx.fxml;
    exports zad02;
}
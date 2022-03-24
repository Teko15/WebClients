module zad02.webclients {
    requires javafx.controls;
    requires javafx.fxml;


    opens zad02 to javafx.fxml;
    exports zad02;
}
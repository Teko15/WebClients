package zad02;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import zad02.Service;

public class HelloApplication extends Application {

    final int HEIGHT = 800;
    final int WIDTH = 600;
    Group ROOT = new Group();
    Scene SCENE = new Scene(ROOT, HEIGHT, WIDTH);

    @Override
    public void start(Stage primaryStage) {
        Stage stage = new Stage();
        stage.setTitle("HelloApplication");
        stage.setScene(SCENE);
        stage.show();
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        Service s = new Service("Germany"); //getNBPRate()
        String weatherJson = s.getWeather("Warsaw");
        Double rate1 = s.getRateFor("USD"); //Waluta s wobec argumentu
        Double rate2 = s.getNBPRate();
        // ...
        // część uruchamiająca GUI
        System.out.println(weatherJson);
        System.out.println(rate1);
        System.out.println(rate2);




        //launch();
    }
}
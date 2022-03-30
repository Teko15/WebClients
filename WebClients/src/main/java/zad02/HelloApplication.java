package zad02;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {

    final static int HEIGHT = 800;
    final static int WIDTH = 600;
    Group ROOT = new Group();
    Scene SCENE = new Scene(ROOT, HEIGHT, WIDTH);

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL wikiUrl = new URL("https://en.wikipedia.org/wiki/" + CITY);

        Stage stage = new Stage();
        stage.setTitle("Weather and Currency Service");
        stage.setScene(SCENE);
        stage.show();
        stage.setResizable(false);
    }

    static String CITY;
    static String WEATHER_JSON;
    static Double GET_RATE_FOR;
    static Double GET_NBP_RATE;


    public static void main(String[] args) {
        Service s = new Service("Germany");
        String weatherJson = s.getWeather("Bandar Seri Begawan");
        Double rate1 = s.getRateFor("USD");
        Double rate2 = s.getNBPRate();
        // ...
        // część uruchamiająca GUI
        WEATHER_JSON = weatherJson;
        GET_RATE_FOR = rate1;
        GET_NBP_RATE = rate2;
        CITY = s.getCOUNTRY();
        //launch();
    }
}
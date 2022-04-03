package zad02;

import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import javafx.scene.web.*;

public class HelloApplication extends Application {

    final static int HEIGHT = 800;
    final static int WIDTH = 600;
    Group ROOT = new Group();
    Scene SCENE = new Scene(ROOT, HEIGHT, WIDTH);

    @Override
    public void start(Stage primaryStage) throws IOException {
        /*URL wikiUrl = new URL("https://en.wikipedia.org/wiki/" + CITY);
        System.out.println(SERVICE.getRateFor(""));
        WebView wb = new WebView();
        WebEngine we = new WebEngine();

        Button changeCountryAndCity = new Button();
        Button changeCurrency = new Button();
        TextArea setNewCountry = new TextArea();
        TextArea setNewCity = new TextArea();
        TextArea setNewCurrency = new TextArea();

        changeCountryAndCity.setOnAction(e -> {
            String newCountry = setNewCountry.getText();
            String newCity = setNewCity.getText();
        });

        changeCurrency.setOnAction(e -> {
            String newCurrency = setNewCurrency.getText();
        });


        Stage stage = new Stage();
        stage.setTitle("Weather and Currency Service");
        stage.setScene(SCENE);
        stage.show();
        stage.setResizable(false);*/
    }

    static Service SERVICE;
    static String COUNTRY;
    static String CITY;
    static String CURRENCY_CODE;
    static String WEATHER_JSON;
    static Double GET_RATE_FOR;
    static Double GET_NBP_RATE;
    static String WEATHER_CLOUDS;
    static Double WEATHER_TEMPERATURE;
    static Double WEATHER_WIND_SPEED;

    public static void main(String[] args) {
        Service s = new Service("Poland");
        String weatherJson = s.getWeather("Warsaw");
        Double rate1 = s.getRateFor("USD");
        Double rate2 = s.getNBPRate();

        SERVICE = s;
        CITY = s.getCOUNTRY();
        COUNTRY = s.getCOUNTRY();
        CURRENCY_CODE = s.getCURRENCY_CODE();
        GET_RATE_FOR = rate1;
        GET_NBP_RATE = rate2;

        WEATHER_JSON = weatherJson;
        changeJSON();
        System.out.println(WEATHER_CLOUDS);
        System.out.println(WEATHER_TEMPERATURE);
        System.out.println(WEATHER_WIND_SPEED);
        //launch();
    }

    private static void changeJSON() {
        String ourBegin = WEATHER_JSON.substring(WEATHER_JSON.indexOf("\"description\"") + 15);
        String weatherClouds = ourBegin.substring(0, ourBegin.indexOf('\"'));
        ourBegin = ourBegin.substring(ourBegin.indexOf("\"temp\":") + 7);
        double weatherTemperature = Double.parseDouble(ourBegin.substring(0, ourBegin.indexOf(',')));
        weatherTemperature = (Math.round(10 * (weatherTemperature - 273d))) / 10d;
        ourBegin = ourBegin.substring(ourBegin.indexOf("\"speed\":") + 8);
        double weatherWindSpeed = Double.parseDouble(ourBegin.substring(0, ourBegin.indexOf(',')));
        weatherWindSpeed *= 3.6;
        WEATHER_CLOUDS = weatherClouds;
        WEATHER_TEMPERATURE = weatherTemperature;
        WEATHER_WIND_SPEED = weatherWindSpeed;
    }
}
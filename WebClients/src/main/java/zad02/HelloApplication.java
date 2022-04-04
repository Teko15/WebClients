package zad02;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.web.*;

public class HelloApplication extends Application {

    final static int HEIGHT = 1000;
    final static int WIDTH = 600;
    static Group ROOT = new Group();
    static Scene SCENE = new Scene(ROOT, HEIGHT, WIDTH);
    WebView WEB_VIEW = new WebView();
    final static String IMAGES_DIRECTORY = System.getProperty("user.dir") + "\\images\\";
    final static java.text.DecimalFormat DECIMAL_FORMAT = new java.text.DecimalFormat("0.0000");
    static TextArea CHANGE_TEXT_AREA;
    static Text TEMPERATURE_TEXT;
    static Text PLN_TO_COUNTRY_CURRENCY;
    static Text COUNTRY_CURRENCY_TO_USER_CURRENCY;
    static Image WEATHER_IMAGE;
    static ImageView WEATHER_IMAGE_VIEW;


    @Override
    public void start(Stage primaryStage) {
        changeCountry(DATA.getCOUNTRY());
        changeCity(DATA.getCITY());
        changeCurrency(DATA.getCURRENCY_CODE());
        refreshTextArea(true);

        Text textCentrumDowodzenia = new Text("CENTRUM\nDOWODZENIA\nPJATK-TPO v2.4");
        textCentrumDowodzenia.setFont(Font.font("COMIC SANS", 24));
        textCentrumDowodzenia.setY(25);
        textCentrumDowodzenia.setX(20);
        textCentrumDowodzenia.maxWidth(197);
        textCentrumDowodzenia.setTextAlignment(TextAlignment.CENTER);

        Text smallText = new Text("*W zasadzie to po prostu frazę");
        smallText.setFont(Font.font("ARIAL", 10));
        smallText.setY(596);
        smallText.setX(850);

        Line line = new Line();
        line.setStrokeWidth(4);
        line.setStartX(200);
        line.setEndX(200);
        line.setStartY(0);
        line.setEndY(WIDTH);

        Line lineShadow = new Line();
        lineShadow.setStroke(Color.GRAY);
        lineShadow.setStrokeWidth(6);
        lineShadow.setStartX(200);
        lineShadow.setEndX(200);
        lineShadow.setStartY(0);
        lineShadow.setEndY(WIDTH);

        Button changeButton = new Button("Change!");
        changeButton.setFont(Font.font("COMIC SANS", 24));
        changeButton.setTranslateY(548);
        changeButton.setPrefWidth(197);

        Image image = new Image(IMAGES_DIRECTORY + "logo.jpg");

        changeButton.setOnAction(e -> {
            String text = CHANGE_TEXT_AREA.getText();
            if (text.length() > 5) {
                if (text.charAt(0) == '-') {
                    if (text.charAt(1) == '-') {
                        String command = text.substring(2, text.indexOf(' '));
                        String value = text.substring(text.indexOf(' ') + 1);
                        switch (command.toLowerCase()) {
                            case "panstwo" -> changeCountry(value);
                            case "miasto" -> changeCity(value);
                            case "waluta" -> changeCurrency(value);
                            default -> CHANGE_TEXT_AREA.setText("Invalid command");
                        }
                    } else if (text.charAt(2) == ' ') {
                        char flag = text.charAt(1);
                        String value = text.substring((text.indexOf(' ')) + 1);
                        switch (Character.toLowerCase(flag)) {
                            case 'p' -> changeCountry(value);
                            case 'm' -> changeCity(value);
                            case 'w' -> changeCurrency(value);
                            default -> CHANGE_TEXT_AREA.setText("Invalid flag");
                        }
                    } else
                        System.out.println("unknown command/flag");
                } else
                    System.out.println(text);
            } else
                System.out.println(text);
        });

        ROOT.getChildren().add(textCentrumDowodzenia);
        ROOT.getChildren().add(smallText);
        ROOT.getChildren().add(lineShadow);
        ROOT.getChildren().add(line);
        ROOT.getChildren().add(changeButton);
        Stage stage = new Stage();
        stage.getIcons().add(image);
        stage.setTitle("Weather and Currency Service");
        stage.setScene(SCENE);
        stage.show();
        stage.setResizable(false);
    }

    private void changeCity(String city) {
        DATA.setCITY(city);
        WEATHER_JSON = SERVICE.getWeather(city);
        if (WEATHER_JSON.length() != 0)
            changeJSON();
        else
            WEATHER_TEMPERATURE = 0.00;

        ROOT.getChildren().remove(WEB_VIEW);
        String WIKI_URL = "https://en.wikipedia.org/wiki/" + city;
        WEB_VIEW.getEngine().load(WIKI_URL);
        WEB_VIEW.setTranslateX(200);
        ROOT.getChildren().add(WEB_VIEW);


        ROOT.getChildren().remove(TEMPERATURE_TEXT);
        TEMPERATURE_TEXT = new Text(WEATHER_TEMPERATURE + "\n°C");
        TEMPERATURE_TEXT.setX(126);
        TEMPERATURE_TEXT.setY(150);
        TEMPERATURE_TEXT.setFont(Font.font("COMIC SANS", 32));
        ROOT.getChildren().add(TEMPERATURE_TEXT);


        ROOT.getChildren().remove(WEATHER_IMAGE_VIEW);
        String path = IMAGES_DIRECTORY;
        switch (WEATHER_CLOUDS) {
            case "clear sky" -> path += "Sunny.png";
            case "few clouds" -> path += "PartlySun.png";
            case "scattered clouds" -> path += "Cloudy.png";
            case "overcast clouds", "broken clouds" -> path += "Overcast.png";
            case "shower rain" -> path += "Showers.png";
            case "rain" -> path += "HeavyRain.png";
            case "thunderstorm" -> path += "Thunderstorm.png";
            case "snow" -> path += "Fog.png";
            default -> path += "UnknownData.png";
        }

        WEATHER_IMAGE = new Image(path);
        WEATHER_IMAGE_VIEW = new ImageView(WEATHER_IMAGE);
        WEATHER_IMAGE_VIEW.setX(3);
        WEATHER_IMAGE_VIEW.setY(100);
        ROOT.getChildren().add(WEATHER_IMAGE_VIEW);
        refreshTextArea(false);
    }

    private void changeCountry(String country) {
        country = correctTypeOfCountry(country);

        SERVICE = new Service(country);
        DATA.setCOUNTRY(country);
        GET_NBP_RATE = SERVICE.getNBPRate();

        ROOT.getChildren().remove(PLN_TO_COUNTRY_CURRENCY);
        PLN_TO_COUNTRY_CURRENCY = new Text("PLN wobec " + SERVICE.getCURRENCY_CODE_COUNTRY() + ": " + DECIMAL_FORMAT.format(GET_NBP_RATE) + " [NBP]");
        PLN_TO_COUNTRY_CURRENCY.setX(3);
        PLN_TO_COUNTRY_CURRENCY.setY(250);
        PLN_TO_COUNTRY_CURRENCY.setFont(Font.font("COMIC SANS", 14));
        ROOT.getChildren().add(PLN_TO_COUNTRY_CURRENCY);
        refreshTextArea(false);
    }

    private String correctTypeOfCountry(String country) {
        country = country.toLowerCase();
        country = country.substring(0, 1).toUpperCase() + country.substring(1);
        int index = country.indexOf(' ');

        while (index >= 0) {
            country = country.substring(0, index + 1) + country.substring(index + 1, index + 2).toUpperCase() + country.substring(index + 2);
            index = country.indexOf(' ', index + 1);
        }
        return country;
    }

    private void changeCurrency(String currencyCode) {
        DATA.setCURRENCY_CODE(currencyCode);
        GET_RATE_FOR = SERVICE.getRateFor(currencyCode);

        ROOT.getChildren().remove(COUNTRY_CURRENCY_TO_USER_CURRENCY);
        COUNTRY_CURRENCY_TO_USER_CURRENCY = new Text(SERVICE.getCURRENCY_CODE_COUNTRY() + " wobec " + DATA.getCURRENCY_CODE() + ": " + DECIMAL_FORMAT.format(GET_RATE_FOR) + " [GRF]");
        COUNTRY_CURRENCY_TO_USER_CURRENCY.setX(3);
        COUNTRY_CURRENCY_TO_USER_CURRENCY.setY(270);
        COUNTRY_CURRENCY_TO_USER_CURRENCY.setFont(Font.font("COMIC SANS", 14));
        ROOT.getChildren().add(COUNTRY_CURRENCY_TO_USER_CURRENCY);
        refreshTextArea(false);
    }

    private void refreshTextArea(boolean first) {
        ROOT.getChildren().remove(CHANGE_TEXT_AREA);
        String text = "Entered data:\nCountry name - " + DATA.getCOUNTRY() +
                "\nCity - " + DATA.getCITY() +
                "*\nCurrency Code - " + DATA.getCURRENCY_CODE() +
                "\n\nTo change Country type:\n-p [COUNTRY_NAME] OR \n--panstwo [COUNTRY_NAME]" +
                "\nTo change City type:\n-m [CITY_NAME] OR \n--miasto [CITY_NAME]" +
                "\nTo change Currency type: \n-w [CURRENCY_CODE] OR --waluta [CURRENCY_CODE]";
        if (!first)
            text = text.replaceAll("[*]", "");
        CHANGE_TEXT_AREA = new TextArea(text);
        CHANGE_TEXT_AREA.setTranslateY(285);
        CHANGE_TEXT_AREA.setPrefWidth(197);
        CHANGE_TEXT_AREA.setPrefHeight(263);
        CHANGE_TEXT_AREA.setWrapText(true);
        ROOT.getChildren().add(CHANGE_TEXT_AREA);
    }

    static Data DATA;
    static Service SERVICE;
    static String WEATHER_JSON;
    static Double GET_RATE_FOR;
    static Double GET_NBP_RATE;
    static String WEATHER_CLOUDS;
    static Double WEATHER_TEMPERATURE;

    public static void main(String[] args) {
        Service s = new Service("Poland");
        String weatherJson = s.getWeather("Yakutsk");
        Double rate1 = s.getRateFor("USD");
        Double rate2 = s.getNBPRate();

        DATA = new Data(s.getCOUNTRY(), s.getCITY(), s.getCURRENCY_CODE());
        SERVICE = s;
        GET_RATE_FOR = rate1;
        GET_NBP_RATE = rate2;
        WEATHER_JSON = weatherJson;

        launch();
    }

    private static void changeJSON() {

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(WEATHER_JSON);
        JsonArray jsonArrayWeather = (JsonArray) jsonObject.get("weather");
        JsonObject jsonWeather = (JsonObject) jsonArrayWeather.get(0);
        JsonObject jsonMain = (JsonObject) jsonObject.get("main");
        double weatherTemperature = Double.parseDouble(String.valueOf(jsonMain.get("temp")));
        String weatherClouds = String.valueOf(jsonWeather.get("description")).replaceAll("\"", "");
        weatherTemperature = (Math.round(10 * (weatherTemperature - 273d))) / 10d;
        WEATHER_CLOUDS = weatherClouds;
        WEATHER_TEMPERATURE = weatherTemperature;
    }
}
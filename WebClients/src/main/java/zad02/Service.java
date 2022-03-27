package zad02;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Service {
    List<String> LINES = new ArrayList<>();
    Map<String, String> CURRENCY_NAME_AND_CODE = new HashMap<>();
    Map<String, Double> CURRENCY_CODE_AND_VALUE = new HashMap<>();
    String COUNTRY;
    String CITY;
    String ourURL;
    final String apiKey = "f563de78aff8c5006420781b71b27998";

    public String getCOUNTRY() {
        return COUNTRY;
    }

    public void setCOUNTRY(String COUNTRY) {
        this.COUNTRY = COUNTRY;
    }

    public Service(String country) {
        setCOUNTRY(country);
        try {
            URL url = new URL("http://www.nbp.pl/kursy/kursya.html");
            for (int i = 0; i < 2; i++) {
                if (i == 1)
                    url = new URL("http://www.nbp.pl/kursy/kursyb.html");
                try (java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        if (line.trim().startsWith("<td class=")) {
                            line = line.substring(line.indexOf('>')).substring(1);
                            line = line.substring(0, line.length() - 5);
                            LINES.add(line);
                        }
                    }
                }
            }

            int arraysSize = LINES.size() / 3;
            String[] currencyName = new String[arraysSize];
            String[] currencyCode = new String[arraysSize];
            double[] currencyValue = new double[arraysSize];
            int everyThree = 0;
            for (int i = 0; i < LINES.size(); i++) {
                switch (i % 3) {
                    case 0 -> currencyName[everyThree] = LINES.get(i);
                    case 1 -> currencyCode[everyThree] = LINES.get(i);
                    case 2 -> {
                        currencyValue[everyThree] = Double.parseDouble(LINES.get(i).replace(',', '.'));
                        everyThree++;
                    }
                }
            }
            for (int i = 0; i < currencyCode.length; i++) {
                currencyValue[i] /= Math.pow(10, currencyCode[i].chars().filter(e -> e == '0').count());
                currencyValue[i] *= 1000000;
                currencyValue[i] = Math.round(currencyValue[i]);
                currencyValue[i] /= 1000000;
                currencyCode[i] = currencyCode[i].substring(currencyCode[i].length() - 3);
                CURRENCY_NAME_AND_CODE.put(currencyName[i], currencyCode[i]);
                CURRENCY_CODE_AND_VALUE.put(currencyCode[i], currencyValue[i]);
            }
        } catch (java.io.IOException ignored) {
            System.out.println("Something went wrong with NBP");
        }
    }

    public String getWeather(String city) {
        //{"coord":{"lon":7.4474,"lat":46.9481},"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01n"}],
        // "base":"stations","main":{"temp":278.45,"feels_like":278.45,"temp_min":276.01,"temp_max":280.58,"pressure":1027,
        // "humidity":69},"visibility":10000,"wind":{"speed":1.16,"deg":148,"gust":1.45},"clouds":{"all":0},"dt":1648344989,
        // "sys":{"type":2,"id":19940,"country":"CH","sunrise":1648358378,"sunset":1648403485},"timezone":7200,"id":2661552,
        // "name":"Bern","cod":200}

        //{"coord":{"lon":8.8078,"lat":53.0752},"weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04n"}],
        // "base":"stations","main":{"temp":278.22,"feels_like":276.54,"temp_min":278.03,"temp_max":278.77,"pressure":1032,
        // "humidity":93},"visibility":10000,"wind":{"speed":2.06,"deg":300},"clouds":{"all":100},"dt":1648344944,
        // "sys":{"type":1,"id":1281,"country":"DE","sunrise":1648357851,"sunset":1648403359},"timezone":7200,"id":2944388,
        // "name":"Bremen","cod":200}

        //{"coord":{"lon":79.9083,"lat":6.9028},"weather":[{"id":800,"main":"Clear","description":"clear sky","icon":"01d"}],
        // "base":"stations","main":{"temp":298.2,"feels_like":298.98,"temp_min":298.2,"temp_max":298.2,"pressure":1013,
        // "humidity":85,"sea_level":1013,"grnd_level":1012},"visibility":10000,"wind":{"speed":1.82,"deg":114,"gust":2.07},"clouds":{"all":2},"dt":1648345520,
        // "sys":{"type":1,"id":9098,"country":"LK","sunrise":1648341672,"sunset":1648385418},"timezone":19800,"id":1238992,
        // "name":"Sri Jayewardenepura Kotte","cod":200}

        ourURL = "https://en.wikipedia.org/wiki/" + CITY;
        String output = "";
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey);
            try (java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = in.readLine()) != null) {
                    output = line;
                }
            }
        } catch (java.io.IOException ignored) {
            System.out.println("Something went wrong with api");
        }
        return output;
    }

    public Double getRateFor(String currencyCode) {
        return (convertCountryCurrencyToPLN() / CURRENCY_CODE_AND_VALUE.get(currencyCode));
    }

    public Double getNBPRate() {
        return (1d / convertCountryCurrencyToPLN());
    }

    private double convertCountryCurrencyToPLN() {
        Map<String, String> countriesAndCodes = new HashMap<>();
        for (String code : Locale.getISOCountries()) {
            Locale locale = new Locale("", code);
            countriesAndCodes.put(locale.getDisplayCountry(), code);
        }
        String getCountryCurrencyCode = Currency.getInstance(new Locale("", countriesAndCodes.get(getCOUNTRY()))).getCurrencyCode();
        return CURRENCY_CODE_AND_VALUE.get(getCountryCurrencyCode);
    }
}

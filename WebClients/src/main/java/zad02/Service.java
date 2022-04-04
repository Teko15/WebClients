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
    String CURRENCY_CODE;
    String CURRENCY_CODE_COUNTRY;
    final String API_KEY = "f563de78aff8c5006420781b71b27998";

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
        setCITY(city);
        String output = "";
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY);
            try (java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = in.readLine()) != null) {
                    output = line;
                }
            }
        } catch (java.io.IOException ignored) {
            System.out.println("Something went wrong with api");
            HelloApplication.WEATHER_CLOUDS = "wrong";
        }
        return output;
    }

    public Double getRateFor(String currencyCode) {
        setCURRENCY_CODE(currencyCode);

        double getRateFor = 0;
        try {
            URL url = new URL("https://api.exchangerate.host/convert?from=" + currencyCode + "&to=PLN");
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            String line = in.readLine();
            line = line.substring(line.indexOf("\"rate\":") + 7);
            getRateFor = Double.parseDouble(line.substring(0, line.indexOf("},\"")));


        } catch (java.io.IOException ignored) {
            System.out.println("Something went wrong with api.exchangerate");
        }
        return getRateFor;
    }

    public Double getNBPRate() {
        if (!COUNTRY.equalsIgnoreCase("Poland"))
            return (1d / convertCountryCurrencyToPLN());
        else {
            setCURRENCY_CODE_COUNTRY("PLN");
            return 1.0;
        }
    }

    private double convertCountryCurrencyToPLN() {
        Map<String, String> countriesAndCodes = new HashMap<>();
        for (String code : Locale.getISOCountries()) {
            Locale locale = new Locale("", code);
            countriesAndCodes.put(locale.getDisplayCountry(), code);
        }
        String getCountryCurrencyCode = Currency.getInstance(new Locale("", countriesAndCodes.get(getCOUNTRY()))).getCurrencyCode();
        setCURRENCY_CODE_COUNTRY(getCountryCurrencyCode);
        return CURRENCY_CODE_AND_VALUE.get(getCountryCurrencyCode);
    }


    public String getCOUNTRY() {
        return COUNTRY;
    }

    public void setCOUNTRY(String COUNTRY) {
        this.COUNTRY = COUNTRY;
    }

    public String getCURRENCY_CODE_COUNTRY() {
        return CURRENCY_CODE_COUNTRY;
    }

    public void setCURRENCY_CODE_COUNTRY(String CURRENCY_CODE_COUNTRY) {
        this.CURRENCY_CODE_COUNTRY = CURRENCY_CODE_COUNTRY;
    }

    public String getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public String getCURRENCY_CODE() {
        return CURRENCY_CODE;
    }

    public void setCURRENCY_CODE(String CURRENCY_CODE) {
        this.CURRENCY_CODE = CURRENCY_CODE;
    }


}

package zad02;

public class Data {
    private String COUNTRY;
    private String CITY;
    private String CURRENCY_CODE;

    public Data(String country, String city, String currencyCode) {
        this.COUNTRY = country;
        this.CITY = city;
        this.CURRENCY_CODE = currencyCode;
    }

    public void setCOUNTRY(String COUNTRY) {
        this.COUNTRY = COUNTRY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public void setCURRENCY_CODE(String CURRENCY_CODE) {
        this.CURRENCY_CODE = CURRENCY_CODE;
    }

    public String getCOUNTRY() {
        return COUNTRY;
    }

    public String getCITY() {
        return CITY;
    }

    public String getCURRENCY_CODE() {
        return CURRENCY_CODE;
    }
}
public class WeatherInfo {
    private weatherMain main;

    public weatherMain getWeatherMain(){
        return main;
    }
}

class weatherMain{
    private String temp;
    private String temp_min;
    private String temp_max;

    public String getTemp(){
        return temp;
    }

    public String getTempMin(){
        return temp_min;
    }

    public String getTempMax(){
        return temp_max;
    }
}

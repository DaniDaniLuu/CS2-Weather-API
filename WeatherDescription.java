import java.util.*;

public class WeatherDescription {
    private ArrayList<weatherStatus> weather;

    public ArrayList<weatherStatus> getWeather(){
        return weather;
    }
}

class weatherStatus{
    private String main;
    private String description;

    public String getMain(){
        return this.main;
    }

    public void setMain(String main){
        this.main = main;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }
}
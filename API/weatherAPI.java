package API;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class weatherAPI {
    private final String apiKEY = "fd44aa2af90ff0c5098737e909f9f521";
    private String weatherUrl;
    private String geoURL;
    private String zipURL;

    public String getKEY(){
        return apiKEY;
    }

    public String getWeatherUrl(){
        return weatherUrl;
    }

    public void setWeatherUrl(double lat, double lon){
        weatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + getKEY() + "&units=imperial";
    }

    public String getGeoUrl(){
        return geoURL;
    }

    public void setGeoUrl(String cityName){
        geoURL = "http://api.openweathermap.org/geo/1.0/direct?q=" + cityName + "&limit=3&appid=" + getKEY();
    }

    public String getZipUrl(){
        return zipURL;
    }

    public void setZipUrl(int zipcode){
        zipURL = "http://api.openweathermap.org/geo/1.0/zip?zip=" + zipcode + "&appid=" + getKEY();
    }
    // API call to get the weather data
    public HttpResponse<String> weatherGET(double lat, double lon) throws IOException, InterruptedException{
        setWeatherUrl(lat, lon);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(getWeatherUrl()))
            .GET()
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
    // API call to get the latitude and longitude from the zipcode
    public HttpResponse<String> latlonGET(int zipcode) throws IOException, InterruptedException{
        setZipUrl(zipcode);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(getZipUrl()))
            .GET()
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
    // API call to get the latitude and longitude from the city
    public HttpResponse<String> latlonGET(String city) throws IOException, InterruptedException{
        setGeoUrl(city);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(getGeoUrl()))
            .GET()
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
}

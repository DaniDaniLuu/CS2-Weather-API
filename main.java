import java.io.IOException;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import WeatherJson.*;
import PircBot.MyBot;

import java.util.*;

public class main {
    public static void main(String[] args) throws IOException, InterruptedException, Exception {
        Scanner input = new Scanner(System.in);
        API openWeather = new API();
        Gson gson = new Gson();
        ArrayList<WeatherLatLon> lat_lon_data = new ArrayList<WeatherLatLon>();
        while(true){
            System.out.print("Enter a city/zipcode: ");
            String userInput = input.nextLine();
            try{
                int userZipcode = Integer.parseInt(userInput);
                if (openWeather.latlonGET(userZipcode).statusCode() == 404){
                    System.out.println("Zipcode not found. Please try again");
                }
                else {
                    lat_lon_data.add(gson.fromJson(openWeather.latlonGET(userZipcode).body(), WeatherLatLon.class));
                    break;
                }
            }
            catch(java.lang.NumberFormatException a){
                if(userInput.contains(" ")){
                    System.out.println("Please enter the city/zipcode without any spaces");
                }
                else if(openWeather.latlonGET(userInput).body().equals("[]")){
                    System.out.println("City not found. Please try again");
                }
                else{
                    WeatherLatLon[] responseArray = gson.fromJson(openWeather.latlonGET(userInput).body(), WeatherLatLon[].class);
                    for(WeatherLatLon lat_lon : responseArray){
                        lat_lon_data.add(lat_lon);
                    }
                    break;
                }
            }
        }
        WeatherInfo weatherInfo = gson.fromJson(openWeather.weatherGET(lat_lon_data.get(0).getLat(), lat_lon_data.get(0).getLon()).body(), WeatherInfo.class);
        WeatherDescription weatherDescription = gson.fromJson(openWeather.weatherGET(lat_lon_data.get(0).getLat(), lat_lon_data.get(0).getLon()).body(), WeatherDescription.class);
        System.out.println("Temperature: " + weatherInfo.getWeatherMain().getTemp() + "\nMin temp: " + weatherInfo.getWeatherMain().getTempMin() + "\nMax temp: " + weatherInfo.getWeatherMain().getTempMax());
        System.out.println("Main: " + weatherDescription.getWeather().get(0).getMain()  + "\nDescription: " + weatherDescription.getWeather().get(0).getDescription());

        // Pirc Bot Stuff
        MyBot bot = new MyBot();
        bot.setVerbose(true);
        bot.connect("irc.us.libera.chat");
        bot.joinChannel("#DanielWeatherBot");
        bot.sendMessage("#DanielWeatherBot", "Hello! Enter in a city/zipcode and I will give you the weather for it!");
    }
}
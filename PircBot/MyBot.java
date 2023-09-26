package PircBot;
import org.jibble.pircbot.*;

import com.google.gson.Gson;

import WeatherJson.*;
import API.weatherAPI;
import API.gptAPI;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyBot extends PircBot {
    
    public MyBot() {
        this.setName("currentBot");
    }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        try{
            apiCall(channel, message);
        }
        catch (IOException e){
        }
        catch (InterruptedException e){
        }
        catch (Exception e){
        }
    }
    // Function that gets called when the user enters a message in the chat room. It checks the beginning of the message to see what operation to perform.
    public void apiCall(String channel,String message) throws IOException, InterruptedException, Exception{
        if (message.startsWith(".question")){
            message = message.replace(".question", "");
            gptApiCall(channel, message);
        }
        else if (message.startsWith(".weather ")) {
            message = message.replace(".weather ", "");
            weatherApiCall(channel, message);
        }
    }

    public void weatherApiCall(String channel,String message) throws IOException, InterruptedException{
        weatherAPI openWeather = new weatherAPI();
        Gson gson = new Gson();
        ArrayList<WeatherLatLon> lat_lon_data = new ArrayList<WeatherLatLon>();
        boolean validResponse = false;
        // Tries to convert the message to an int with the Integer wrapper class. If sucessful then that means the entered value was a zipcode
        try {
            int userZipcode = Integer.parseInt(message);
            if (openWeather.latlonGET(userZipcode).statusCode() == 404){
                sendMessage(channel, "Zipcode not found. Please try again");
            }
            else {
                lat_lon_data.add(gson.fromJson(openWeather.latlonGET(userZipcode).body(), WeatherLatLon.class));
                validResponse = true;
            }
        }
        // Catches the NumberFormatException which gets thrown when Integer.parseInt(message) tries to parse a string. If this gets catched then that means the entered value is a string -> city
        catch(java.lang.NumberFormatException a) {
            if(message.contains(" ")){
                sendMessage(channel,"Please enter the city/zipcode without any spaces");
            }
            else if(openWeather.latlonGET(message).body().equals("[]")){
                sendMessage(channel, "City not found. Please try again");
            }
            else{
                WeatherLatLon[] responseArray = gson.fromJson(openWeather.latlonGET(message).body(), WeatherLatLon[].class);
                for(WeatherLatLon lat_lon : responseArray){
                    lat_lon_data.add(lat_lon);
                }
                validResponse = true;
            }
        }
        // Conditional that activates only if the return json string was properly processed by gson
        if(validResponse){
            sendMessage(channel, "Weather data loading...");
            WeatherInfo weatherInfo = gson.fromJson(openWeather.weatherGET(lat_lon_data.get(0).getLat(), lat_lon_data.get(0).getLon()).body(), WeatherInfo.class);
            WeatherDescription weatherDescription = gson.fromJson(openWeather.weatherGET(lat_lon_data.get(0).getLat(), lat_lon_data.get(0).getLon()).body(), WeatherDescription.class);
            String responseMessage = "Temperature: " + weatherInfo.getWeatherMain().getTemp() + "\nMin temp: " + weatherInfo.getWeatherMain().getTempMin()
                                        + "\nMax temp: " + weatherInfo.getWeatherMain().getTempMax() + "\nMain: " + weatherDescription.getWeather().get(0).getMain()
                                        + "\nDescription: " + weatherDescription.getWeather().get(0).getDescription();
            String[] newLines = responseMessage.split("\n");
            for(String line : newLines){
                sendMessage(channel, line);
            }
        }
    }
    // Function call for the .question message. Sends a POST request to GPT api and formats it then has the bot send it
    public void gptApiCall(String channel,String message) throws Exception{
        gptAPI gpt = new gptAPI();
        Gson gson = new Gson();

        sendMessage(channel, "Loading...");
        sendMessage(channel, "Hold up...");
        sendMessage(channel, "Wait a minute...");
        GptResponse gptResponse = gson.fromJson(gpt.chat(message).body(), GptResponse.class);
        String responseMessage = gptResponse.getChoices()[0].getMessage().getContent();
        responseMessage = cleanIRCMessage(responseMessage);

        chunkSendMessage(channel, responseMessage);

    }
    // If the user request for a list the response needs to be clean up by removing \n and other special values inorder for the irc Bot to send the message
    public String cleanIRCMessage(String message) {
        String pattern = "[^\u0020-\u007E]";

        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(message);
        String cleanedMessage = matcher.replaceAll("");

        return cleanedMessage;
    }
    // GPT api response can be longer than the irc message limit so this funciton splits the message into chunks of 450 characters to send
    public void chunkSendMessage(String channel,String Message){
        int maxLength = 450;
        int startChunk = 0;
        int endChunk = Math.min(maxLength, Message.length());

        while (startChunk < Message.length()){
            String segment = Message.substring(startChunk, endChunk);
            sendMessage(channel, segment);

            startChunk = endChunk;
            endChunk = Math.min(endChunk + maxLength, Message.length());
            try{
                Thread.sleep(1000);
            }
            catch(InterruptedException e){
            }
        }
    }
    
}
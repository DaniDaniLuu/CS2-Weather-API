package PircBot;
import org.jibble.pircbot.*;
import java.util.*;

public class MyBot extends PircBot {
    
    public MyBot() {
        this.setName("tesbto123123");
    }

    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        if (message.equalsIgnoreCase("time")){
            String time = new Date().toString();
            sendMessage(channel, sender + ": The time is now " + time);
        }
    }

    
}
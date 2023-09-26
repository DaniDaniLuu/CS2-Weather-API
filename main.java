import PircBot.MyBot;

public class Main {
    public static void main(String[] args) throws Exception {
        // Creates the pircBot
        MyBot bot = new MyBot();
        bot.setVerbose(true);
        bot.connect("irc.us.libera.chat");
        bot.joinChannel("#DanielAPIPROJ");
        bot.sendMessage("#DanielAPIPROJ", "Hello User!");
        bot.sendMessage("#DanielAPIPROJ", "Use the .weather command followed by the city/zipcode to get the current weather");
        bot.sendMessage("#DanielAPIPROJ", "Alternatively use the .question command to ask anything else!");
    }
}
import java.io.IOException;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a city name: ");
        String cityName = input.nextLine();

        API test = new API();
        Gson gson = new Gson();
        test.setGeoUrl(cityName);
        System.out.println(test.geoAPI().body());

    }
}
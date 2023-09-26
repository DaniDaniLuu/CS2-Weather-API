package API;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

public class gptAPI {
    // GPT API URL
    private final String TURBO_URL = "https://api.openai.com/v1/chat/completions";
    // Template request body that includes the model which we want to use and the messages and AI temperature
    private final String REQUEST_BODY = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}], \"temperature\": 0.7}";

    public HttpResponse<String> chat(String message) throws Exception {
        // Replaces the %s in the REQUEST_BODY with the message send and assigns it to a new String
        String reqestBody = String.format(REQUEST_BODY, message);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TURBO_URL))
                .header("Content-Type", "application/json")
                // Authorization which requires Bearer followed by the API key
                .header("Authorization", "Bearer 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")
                .POST(BodyPublishers.ofString(reqestBody))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
}

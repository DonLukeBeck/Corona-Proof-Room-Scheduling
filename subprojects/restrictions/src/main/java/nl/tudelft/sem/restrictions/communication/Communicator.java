package nl.tudelft.sem.restrictions.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Service;

@Service
public abstract class Communicator {
    private static HttpClient client = HttpClient.newBuilder().build();
    public static ObjectMapper objectMapper =
            new ObjectMapper().registerModule(new JavaTimeModule());

    protected static HttpResponse<String> getResponse(String uri, String service)
            throws IOException, InterruptedException, ServerErrorException {

        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.GET().uri(URI.create(service + uri));
        builder.header("Content-Type", "application/json");
        HttpRequest request = builder.build();
        HttpResponse<String> response;

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != HttpURLConnection.HTTP_OK) {
            throw new ServerErrorException();
        }
        return response;
    }
}

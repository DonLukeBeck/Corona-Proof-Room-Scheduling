package nl.tudelft.sem.courses.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.servlet.http.HttpServletRequest;

public class RoleValidation {

    private static HttpClient client = HttpClient.newBuilder().build();
    private static String uri = "/validate";

    public static String getRole(HttpServletRequest request)
            throws IOException, InterruptedException {

        //extract authorization from request
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            //extract token from header
            token = authorizationHeader.substring(7);
        }

        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.uri(URI.create(Constants.IDENTITY_SERVER_URL + uri))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(token));
        HttpRequest identityRequest = builder.build();
        HttpResponse<String> response;
        response = client.send(identityRequest, HttpResponse.BodyHandlers.ofString());

        //check whether the response is from the identity service and not some man in the middle
        if(!response.uri().toString().equals("http://localhost:8083/validate"))
            return null;

        if (response.statusCode() == HttpURLConnection.HTTP_OK)
            return response.body();

        return null;
    }

}

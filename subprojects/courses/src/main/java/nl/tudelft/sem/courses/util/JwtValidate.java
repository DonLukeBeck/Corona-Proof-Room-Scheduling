package nl.tudelft.sem.courses.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.boot.jackson.JsonObjectSerializer;

public class JwtValidate {

    private static HttpClient client = HttpClient.newBuilder().build();
    private static String uri = "/validate";

    /**
     * Primary method to verify the role inside a jwt token from a request authorization Header.
     *
     * @param request the request to extract the jwt from
     * @return the role extracted from the jwt token
     */

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    // Found 'DD'-anomaly for variable 'token'
    // This is not a redefinition of the same variable
    // we cannot initialise the token in an if statement
    public static JSONObject jwtValidate(HttpServletRequest request)
            throws IOException, InterruptedException, JSONException {

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
        final JSONObject obj = new JSONObject(response.body());

        //check whether the response is from the identity service and not some man in the middle
        if (!response.uri().toString().equals("http://localhost:8083/validate")) {
            return null;
        }

        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            return obj;
        }

        return null;
    }

}

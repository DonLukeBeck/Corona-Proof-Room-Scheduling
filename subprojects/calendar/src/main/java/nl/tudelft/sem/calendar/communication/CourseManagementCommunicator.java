package nl.tudelft.sem.calendar.communication;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;
import nl.tudelft.sem.calendar.Constants;
import nl.tudelft.sem.calendar.exceptions.ServerErrorException;
import nl.tudelft.sem.calendar.scheduling.RequestedLecture;

public class CourseManagementCommunicator {
    public static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static HttpClient client = HttpClient.newBuilder().build();

    /**
     * This method is a mock for the connection with the Course management service.
     *
     * @return the lectures pushed by the course service that need to be scheduled
     */
    public static List<RequestedLecture> getToBeScheduledLectures(Date date)
        throws IOException, InterruptedException, ServerErrorException {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.GET().uri(URI.create(Constants.COURSE_SERVER_URL + "/lectures/date/" + date));
        builder.header("Content-Type", "application/json");
        HttpRequest request = builder.build();
        HttpResponse<String> response;

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // TODO: log the errors
        return switch (response.statusCode()) {
            case HttpURLConnection.HTTP_OK -> objectMapper.readValue(response.body(),
                new TypeReference<List<RequestedLecture>>() {
                });
            case HttpURLConnection.HTTP_NOT_FOUND -> throw new ConnectException();
            default -> throw new ServerErrorException();
        };
    }
}



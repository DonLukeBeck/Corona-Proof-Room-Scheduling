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
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import nl.tudelft.sem.calendar.Constants;
import nl.tudelft.sem.calendar.entities.BareCourse;
import nl.tudelft.sem.calendar.entities.BareEnrollment;
import nl.tudelft.sem.calendar.entities.BareLecture;
import nl.tudelft.sem.calendar.entities.Course;
import nl.tudelft.sem.calendar.entities.Enrollment;
import nl.tudelft.sem.calendar.entities.Lecture;
import nl.tudelft.sem.calendar.exceptions.ServerErrorException;

public class CourseManagementCommunicator {
    public static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static HttpClient client = HttpClient.newBuilder().build();

    /**
     * This method is a mock for the connection with the Course management service.
     *
     * @return the lectures pushed by the course service that need to be scheduled
     */
    public static List<Lecture> getToBeScheduledLectures(Date date)
        throws IOException, InterruptedException, ServerErrorException {

        var response = getResponse("/lectures/date/" + date);
        var bareLectures
            = objectMapper.readValue(response.body(), new TypeReference<List<BareLecture>>() {});
        // this isn't a DU-anomaly since we use it to store and retrieve courses inside the for loop
        @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
        var courseMap = new HashMap<String, Course>();
        var lectureList = new ArrayList<Lecture>();
        for (var l : bareLectures) {
            if (!courseMap.containsKey(l.getCourseId())) {
                courseMap.put(l.getCourseId(), courseFromId(l.getCourseId()));
            }
            lectureList.add(Lecture.builder().course(courseMap.get(l.getCourseId()))
                .courseId(l.getCourseId()).durationInMinutes(l.getDurationInMinutes())
                .date(Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault())
                    .toLocalDate()).build());
        }
        return lectureList;
    }

    private static Course courseFromId(String courseId)
        throws IOException, InterruptedException, ServerErrorException {
        var resp = objectMapper.readValue(getResponse(
            "/course/id/" + courseId).body(), new TypeReference<BareCourse>(){});
        var enrollments = objectMapper.readValue(getResponse(
            "/enrollment/course/" + courseId).body(), new TypeReference<List<BareEnrollment>>(){});

        return new Course(resp.getCourseId(), resp.getCourseName(), resp.getTeacherId(),
            enrollments.stream().map(e -> new Enrollment(e.getStudentId(),e.getCourseId()))
                .collect(Collectors.toList()));
    }

    private static HttpResponse<String> getResponse(String uri)
        throws IOException, InterruptedException, ServerErrorException {

        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.GET().uri(URI.create(Constants.COURSE_SERVER_URL + uri));
        builder.header("Content-Type", "application/json");
        HttpRequest request = builder.build();
        HttpResponse<String> response;

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != HttpURLConnection.HTTP_OK) {
            // TODO: log the errors
            throw new ServerErrorException();
        }
        return response;
    }
}


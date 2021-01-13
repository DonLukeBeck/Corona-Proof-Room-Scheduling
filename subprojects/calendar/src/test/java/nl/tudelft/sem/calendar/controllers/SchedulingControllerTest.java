package nl.tudelft.sem.calendar.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.calendar.communication.CourseAdapter;
import nl.tudelft.sem.calendar.communication.RestrictionCommunicator;
import nl.tudelft.sem.calendar.entities.Attendance;
import nl.tudelft.sem.calendar.entities.Course;
import nl.tudelft.sem.calendar.entities.Lecture;
import nl.tudelft.sem.calendar.entities.Room;
import nl.tudelft.sem.calendar.exceptions.ServerErrorException;
import nl.tudelft.sem.calendar.scheduling.LectureScheduler;
import nl.tudelft.sem.calendar.util.Validate;
import nl.tudelft.sem.shared.Constants;
import nl.tudelft.sem.shared.entity.StringMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

@AutoConfigureMockMvc
@WebMvcTest(SchedulingController.class)
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")

class SchedulingControllerTest {
    private HttpServletRequest wrongRequest;
    private HttpServletRequest studentRequest;
    private HttpServletRequest teacherRequest;
    private LocalDate[] dates;
    private Course[] courses;
    private String[] netIds;
    private List<Attendance> attendances;
    private List<Lecture> lecturesToSchedule;
    private List<Room> rooms;
    private int startTimeSec;
    private LocalTime startTime;
    private int endTimeSec;
    private LocalTime endTime;
    private int timeGapLength;

    // Mocks
    @MockBean
    private LectureScheduler lectureScheduler;

    @MockBean
    private RestrictionCommunicator restrictionCommunicator;

    @MockBean
    private Validate validate;

    @MockBean
    private CourseAdapter courseAdapter;

    @InjectMocks
    private SchedulingController schedulingController;

    @BeforeEach
    void setup() throws InterruptedException, ServerErrorException, IOException {
        dates = new LocalDate[] {
                LocalDate.of(2020, 12, 17),
                LocalDate.of(2020, 12, 22)};

        rooms = List.of(new Room(1, "Class", 2),
                new Room(2, "IZ - 2", 20));

        netIds = new String[] { "mbjdegoede", "cparlar", "abobe", "teacher1", "teacher2" };
        startTimeSec = 31500;
        startTime = LocalTime.of(8, 45);
        endTimeSec = 63900;
        endTime = LocalTime.of(17,  45);
        timeGapLength = 45;

        studentRequest = Mockito.mock(HttpServletRequest.class);
        teacherRequest = Mockito.mock(HttpServletRequest.class);
        wrongRequest = Mockito.mock(HttpServletRequest.class);

        createCourses();
        createLecturesAndAttendances();
        configureMocks();

        schedulingController = new SchedulingController(lectureScheduler, restrictionCommunicator,
            courseAdapter, validate);
    }

    private void createCourses() {
        courses = new Course[] { new Course(List.of(netIds).subList(0, 2),
                "CSE1200", "Calculus", "teacher1"),
            new Course(List.of(netIds).subList(2, 3),
                    "CSE2200", "someCourse", "teacher2") };

        courses[0].setTeacherId(netIds[3]);
        courses[1].setTeacherId(netIds[4]);
        courses[0].setCourseId("CSE1200");
        courses[1].setCourseId("CSE1500");
        courses[0].setCourseName("Calculus");
        courses[0].setCourseName("Web and Database");
    }

    private void createLecturesAndAttendances() {
        Lecture lec1 = Lecture.builder().course(courses[0])
                .roomId(rooms.get(0).getRoomId()).roomName(rooms.get(0).getName())
                .courseId(courses[0].getCourseId()).durationInMinutes(90)
                .date(dates[0]).lectureId(1).build();

        Lecture lec2 = Lecture.builder().course(courses[1])
                .roomId(rooms.get(1).getRoomId()).roomName(rooms.get(1).getName())
                .courseId(courses[1].getCourseId()).durationInMinutes(100)
                .date(dates[0]).lectureId(2).build();

        Lecture lec3 = Lecture.builder().course(courses[0])
                .roomId(rooms.get(0).getRoomId()).roomName(rooms.get(0).getName())
                .courseId(courses[0].getCourseId()).durationInMinutes(90)
                .date(dates[1]).lectureId(3).build();

        lecturesToSchedule = List.of(lec1, lec2, lec3);

        attendances = List.of(
                new Attendance(netIds[0], lecturesToSchedule.get(0).getLectureId(), true),
                new Attendance(netIds[1], lecturesToSchedule.get(0).getLectureId(), false),
                new Attendance(netIds[1], lecturesToSchedule.get(1).getLectureId(), true));

    }

    private void configureMocks() throws InterruptedException, ServerErrorException, IOException {
        when(restrictionCommunicator.getStartTime()).thenReturn(startTimeSec);
        when(restrictionCommunicator.getEndTime()).thenReturn(endTimeSec);
        when(restrictionCommunicator.getTimeGapLength()).thenReturn(timeGapLength);
        when(restrictionCommunicator.getAllRoomsWithAdjustedCapacity()).thenReturn(rooms);
        when(courseAdapter.getToBeScheduledLectures(any())).thenReturn(lecturesToSchedule);

        when(validate.validateRole(studentRequest, "student"))
                .thenReturn(netIds[1]);
        when(validate.validateRole(teacherRequest, "teacher"))
                .thenReturn(netIds[3]);
        when(validate.validateRole(wrongRequest, "teacher"))
                .thenReturn(Constants.noAccessMessage.getMessage());
        when(validate.validateRole(wrongRequest, "student"))
                .thenReturn(Constants.noAccessMessage.getMessage());
    }

    @Test
    void testScheduleLecturesSuccess()
            throws InterruptedException, ServerErrorException, IOException {

        assertThat(schedulingController.scheduleLectures(teacherRequest).getBody())
            .isEqualTo(new StringMessage("Successfully scheduled lectures."));

        verify(restrictionCommunicator, times(1)).getStartTime();
        verify(restrictionCommunicator, times(1)).getEndTime();
        verify(restrictionCommunicator, times(1)).getTimeGapLength();

        verify(restrictionCommunicator, times(1))
                .getAllRoomsWithAdjustedCapacity();
        verify(courseAdapter, times(1))
                .getToBeScheduledLectures(LocalDate.now());

        verify(lectureScheduler, times(1))
                .setFields(rooms, lecturesToSchedule, startTime, endTime, timeGapLength);
    }

    @Test
    void testScheduleLecturesFailure() throws IOException, InterruptedException,
            ServerErrorException {
        doThrow(new IOException()).when(restrictionCommunicator)
                .getAllRoomsWithAdjustedCapacity();
        assertThat(schedulingController.scheduleLectures(teacherRequest).getStatusCode())
            .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void testScheduleLecturesAccessDenied() {
        assertThat(schedulingController.scheduleLectures(wrongRequest).getBody())
            .isEqualTo(Constants.noAccessMessage);
    }
}
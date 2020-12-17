package nl.tudelft.sem.calendar.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import nl.tudelft.sem.calendar.communication.CourseCommunicator;
import nl.tudelft.sem.calendar.communication.RestrictionCommunicator;
import nl.tudelft.sem.calendar.communication.RoomCommunicator;
import nl.tudelft.sem.calendar.entities.Course;
import nl.tudelft.sem.calendar.entities.Lecture;
import nl.tudelft.sem.calendar.entities.Room;
import nl.tudelft.sem.calendar.exceptions.ServerErrorException;
import nl.tudelft.sem.calendar.repositories.AttendanceRepository;
import nl.tudelft.sem.calendar.repositories.LectureRepository;
import nl.tudelft.sem.calendar.scheduling.LectureScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@AutoConfigureMockMvc
@WebMvcTest(CalendarController.class)
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")

class CalendarControllerTest {
    private CalendarController calendarController;
    private HttpServletRequest request; // TODO: create mock to test with authentication
    private LocalDate[] dates;
    private Course[] courses;
    private String[] netIds;
    private String[] userIds;
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
    private AttendanceRepository attendanceRepository;

    @MockBean
    private LectureRepository lectureRepository;

    @MockBean
    private RestrictionCommunicator restrictionCommunicator;

    @MockBean
    private CourseCommunicator courseCommunicator;

    @MockBean
    private RoomCommunicator roomCommunicator;

    @BeforeEach
    void setup() throws InterruptedException, ServerErrorException, IOException {
        dates = new LocalDate[] {
                LocalDate.of(2020, 12, 17),
                LocalDate.of(2020, 12, 22)};

        rooms = List.of(new Room(1, "Class", 2),
                new Room(2, "IZ - 2", 20));

        netIds = new String[] { "mbjdegoede", "cparlar", "abobe" };
        userIds = new String[] { "student", "teacher1", "teacher2" };
        startTimeSec = 31500;
        startTime = LocalTime.of(8,45);
        endTimeSec = 63900;
        endTime = LocalTime.of(17, 45);
        timeGapLength = 45;

        createCourses();
        createLectures();
        configureMocks();

        calendarController = new CalendarController(lectureScheduler,
                attendanceRepository, lectureRepository, restrictionCommunicator,
                courseCommunicator, roomCommunicator);
    }

    private void createCourses() {
        courses = new Course[] { new Course(List.of(netIds).subList(0,2)),
                new Course(List.of(netIds).subList(2,3)) };

        courses[0].setTeacherId(userIds[1]);
        courses[1].setTeacherId(userIds[2]);
        courses[0].setCourseId("CSE1200");
        courses[1].setCourseId("CSE1500");
        courses[0].setCourseName("Calculus");
        courses[0].setCourseName("Web and Database");
    }

    private void createLectures() {
        Lecture lec1 = Lecture.builder().course(courses[0])
                .courseId(courses[0].getCourseId()).durationInMinutes(90)
                .date(dates[0]).build();

        Lecture lec2 = Lecture.builder().course(courses[1])
                .courseId(courses[1].getCourseId()).durationInMinutes(100)
                .date(dates[0]).build();

        Lecture lec3 = Lecture.builder().course(courses[0])
                .courseId(courses[0].getCourseId()).durationInMinutes(90)
                .date(dates[1]).build();

        lecturesToSchedule = List.of(lec1, lec2, lec3);
    }

    private void configureMocks() throws InterruptedException, ServerErrorException, IOException {
        when(restrictionCommunicator.getStartTime()).thenReturn(startTimeSec);
        when(restrictionCommunicator.getEndTime()).thenReturn(endTimeSec);
        when(restrictionCommunicator.getTimeGapLength()).thenReturn(timeGapLength);
        when(restrictionCommunicator.getAllRoomsWithAdjustedCapacity()).thenReturn(rooms);
        when(courseCommunicator.getToBeScheduledLectures(any())).thenReturn(lecturesToSchedule);
    }

    @Test
    void testScheduleLecturesAccessDenied() {
        //todo test with wrong token/request
    }

    @Test
    void testScheduleLecturesSuccess()
            throws InterruptedException, ServerErrorException, IOException {
        verify(restrictionCommunicator, times(1)).getStartTime();
        verify(restrictionCommunicator, times(1)).getEndTime();
        verify(restrictionCommunicator, times(1)).getTimeGapLength();

        verify(restrictionCommunicator, times(1))
                .getAllRoomsWithAdjustedCapacity();
        verify(courseCommunicator, times(1))
                .getToBeScheduledLectures(LocalDate.now());

        verify(lectureScheduler, times(1))
                .setFields(rooms, lecturesToSchedule, startTime, endTime, timeGapLength);

        assertEquals(ResponseEntity.ok("Successfully scheduled lectures."),
                calendarController.scheduleLectures(request));
    }

    @Test
    void testScheduleLecturesFailure() throws IOException, InterruptedException,
            ServerErrorException {
        doThrow(new IOException()).when(restrictionCommunicator).getAllRoomsWithAdjustedCapacity();
        assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error."), calendarController.scheduleLectures(request));
    }

    @Test
    void testGetMyPersonalScheduleStudentSuccess() {

    }

    @Test
    void testGetMyPersonalScheduleStudentFailure() {

    }

    @Test
    void testGetMyPersonalScheduleStudentAccessDenied() {
        //todo test with wrong token/request
    }


    // Rest of them

    @Test
    void testGetMyPersonalScheduleTeacher() {
    }

    @Test
    void testGetMyPersonalScheduleForDayStudent() {
    }

    @Test
    void testGetMyPersonalScheduleForDayTeacher() {
    }

    @Test
    void testGetMyPersonalScheduleForCourseStudent() {
    }

    @Test
    void testGetMyPersonalScheduleForCourseTeacher() {
    }

    @Test
    void testIndicateAbsence() {
    }

    @Test
    void testGetPhysicalAttendantsForLecture() {
    }

    @Test
    void testCreateLectureIdPhysicalMap() {

    }

    @Test
    void testValidateRole() {
    }
}
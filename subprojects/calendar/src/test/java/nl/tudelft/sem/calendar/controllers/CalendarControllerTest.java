package nl.tudelft.sem.calendar.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.calendar.communication.CourseCommunicator;
import nl.tudelft.sem.calendar.communication.RestrictionCommunicator;
import nl.tudelft.sem.calendar.communication.RoomCommunicator;
import nl.tudelft.sem.calendar.entities.Attendance;
import nl.tudelft.sem.calendar.entities.Course;
import nl.tudelft.sem.calendar.entities.Lecture;
import nl.tudelft.sem.calendar.entities.Room;
import nl.tudelft.sem.calendar.exceptions.ServerErrorException;
import nl.tudelft.sem.calendar.repositories.AttendanceRepository;
import nl.tudelft.sem.calendar.repositories.LectureRepository;
import nl.tudelft.sem.calendar.scheduling.LectureScheduler;
import nl.tudelft.sem.calendar.util.Validate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
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
    private transient String noAccessMessage =
            "You are not allowed to view this page. Please contact administrator.";

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

    @MockBean
    private Validate validate;

    @InjectMocks
    private CalendarController calendarController;

    @BeforeEach
    void setup() throws InterruptedException, ServerErrorException, IOException {
        dates = new LocalDate[] {
                LocalDate.of(2020, 12, 17),
                LocalDate.of(2020, 12, 22)};

        rooms = List.of(new Room(1, "Class", 2),
                new Room(2, "IZ - 2", 20));

        netIds = new String[] { "mbjdegoede", "cparlar", "abobe", "teacher1", "teacher2" };
        startTimeSec = 31500;
        startTime = LocalTime.of(8,45);
        endTimeSec = 63900;
        endTime = LocalTime.of(17, 45);
        timeGapLength = 45;

        studentRequest = Mockito.mock(HttpServletRequest.class);
        teacherRequest = Mockito.mock(HttpServletRequest.class);
        wrongRequest = Mockito.mock(HttpServletRequest.class);

        createCourses();
        createLecturesAndAttendances();
        configureMocks();

        calendarController = new CalendarController(lectureScheduler,
                attendanceRepository, lectureRepository, restrictionCommunicator,
                courseCommunicator, roomCommunicator, validate);
    }

    private void createCourses() {
        courses = new Course[] { new Course(List.of(netIds).subList(0,2)),
                new Course(List.of(netIds).subList(2,3)) };

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

        attendances = List.of(new Attendance(netIds[0], lecturesToSchedule.get(0).getLectureId(), true),
        new Attendance(netIds[1], lecturesToSchedule.get(0).getLectureId(), false),
        new Attendance(netIds[1], lecturesToSchedule.get(1).getLectureId(), true));

    }

    private void configureMocks() throws InterruptedException, ServerErrorException, IOException {
        when(restrictionCommunicator.getStartTime()).thenReturn(startTimeSec);
        when(restrictionCommunicator.getEndTime()).thenReturn(endTimeSec);
        when(restrictionCommunicator.getTimeGapLength()).thenReturn(timeGapLength);
        when(restrictionCommunicator.getAllRoomsWithAdjustedCapacity()).thenReturn(rooms);
        when(courseCommunicator.getToBeScheduledLectures(any())).thenReturn(lecturesToSchedule);

        when(validate.validateRole(studentRequest, "student"))
                .thenReturn(netIds[1]);
        when(validate.validateRole(teacherRequest, "teacher"))
                .thenReturn(netIds[3]);
        when(validate.validateRole(wrongRequest, "teacher"))
                .thenReturn(noAccessMessage);
        when(validate.validateRole(wrongRequest, "student"))
                .thenReturn(noAccessMessage);

        when(attendanceRepository.findByStudentId(netIds[1])).thenReturn(attendances.subList(1, 3));

        when(lectureRepository.findByLectureId(lecturesToSchedule.get(0).getLectureId()))
        .thenReturn(lecturesToSchedule.get(0));
        when(lectureRepository.findByLectureId(lecturesToSchedule.get(1).getLectureId()))
        .thenReturn(lecturesToSchedule.get(1));

        when(roomCommunicator.getRoomName(lecturesToSchedule.get(0).getLectureId()))
        .thenReturn(lecturesToSchedule.get(0).getRoomName());
        when(roomCommunicator.getRoomName(lecturesToSchedule.get(1).getLectureId()))
        .thenReturn(lecturesToSchedule.get(1).getRoomName());
    }

    @Test
    void testScheduleLecturesSuccess()
            throws InterruptedException, ServerErrorException, IOException {

        assertEquals(ResponseEntity.ok("Successfully scheduled lectures."),
                calendarController.scheduleLectures(teacherRequest));

        verify(restrictionCommunicator, times(1)).getStartTime();
        verify(restrictionCommunicator, times(1)).getEndTime();
        verify(restrictionCommunicator, times(1)).getTimeGapLength();

        verify(restrictionCommunicator, times(1))
                .getAllRoomsWithAdjustedCapacity();
        verify(courseCommunicator, times(1))
                .getToBeScheduledLectures(LocalDate.now());

        verify(lectureScheduler, times(1))
                .setFields(rooms, lecturesToSchedule, startTime, endTime, timeGapLength);
    }

    @Test
    void testScheduleLecturesFailure() throws IOException, InterruptedException,
            ServerErrorException {
        doThrow(new IOException()).when(restrictionCommunicator).getAllRoomsWithAdjustedCapacity();
        assertEquals(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error."), calendarController.scheduleLectures(teacherRequest));
    }

    @Test
    void testScheduleLecturesAccessDenied()
            throws IOException, InterruptedException {
        assertEquals(ResponseEntity.ok(noAccessMessage),
                calendarController.scheduleLectures(wrongRequest));
    }

    @Test
    void testGetMyPersonalScheduleStudentSuccess()
            throws InterruptedException, ServerErrorException, IOException {

        ResponseEntity<List<Lecture>> result =
        (ResponseEntity<List<Lecture>>) calendarController.getMyPersonalScheduleStudent(studentRequest);
        assertEquals(2, Objects.requireNonNull(result.getBody()).size());

        assertTrue(result.getBody().get(1).isSelectedForOnCampus());
        assertFalse(result.getBody().get(0).isSelectedForOnCampus());

        assertEquals(lecturesToSchedule.get(0).getRoomName(), result.getBody().get(0).getRoomName());
        assertEquals(lecturesToSchedule.get(1).getRoomName() , result.getBody().get(1).getRoomName());

        verify(lectureRepository, times(1)).findByLectureId(lecturesToSchedule.get(0).getLectureId());
        verify(lectureRepository, times(1)).findByLectureId(lecturesToSchedule.get(1).getLectureId());
        verify(roomCommunicator, times(1)).getRoomName(lecturesToSchedule.get(0).getLectureId());
        verify(roomCommunicator, times(1)).getRoomName(lecturesToSchedule.get(1).getLectureId());
    }


    @Test
    void testGetMyPersonalScheduleStudentAccessDenied()
            throws InterruptedException, ServerErrorException, IOException {
        assertEquals(ResponseEntity.ok(noAccessMessage),
                calendarController.getMyPersonalScheduleStudent(wrongRequest));
    }

    @Test
    void testGetMyPersonalScheduleTeacherSuccess()
        throws InterruptedException, ServerErrorException, IOException {
        List<Lecture> lectureList = new ArrayList<>();
        assertEquals(ResponseEntity.ok(lectureList),
                        calendarController.getMyPersonalScheduleTeacher(teacherRequest));

    }

    @Test
    void testGetMyPersonalScheduleTeacherAccessDenied()
            throws InterruptedException, ServerErrorException, IOException {
        assertEquals(ResponseEntity.ok(noAccessMessage),
                calendarController.getMyPersonalScheduleTeacher(wrongRequest));
    }

    @Test
    void testGetMyPersonalScheduleForDayStudentSuccess() {
    }

    @Test
    void testGetMyPersonalScheduleForDayStudentAccessDenied()
            throws InterruptedException, ServerErrorException, IOException {
        assertEquals(ResponseEntity.ok(noAccessMessage),
                calendarController.getMyPersonalScheduleForDayStudent(wrongRequest,dates[0]));
    }

    @Test
    void testGetMyPersonalScheduleForDayTeacherSuccess() {
    }

    @Test
    void testGetMyPersonalScheduleForDayTeacherAccessDenied()
            throws InterruptedException, ServerErrorException, IOException {
        assertEquals(ResponseEntity.ok(noAccessMessage),
                calendarController.getMyPersonalScheduleForDayTeacher(wrongRequest,dates[0]));
    }

    @Test
    void testGetMyPersonalScheduleForCourseStudentSuccess() {
    }

    @Test
    void testGetMyPersonalScheduleForCourseStudentAccessDenied()
            throws InterruptedException, ServerErrorException, IOException {
        assertEquals(ResponseEntity.ok(noAccessMessage),
                calendarController.getMyPersonalScheduleForCourseStudent(wrongRequest,"courseid"));
    }

    @Test
    void testGetMyPersonalScheduleForCourseTeacherSuccess() {
    }

    @Test
    void testGetMyPersonalScheduleForCourseTeacherAccessDenied()
            throws IOException, InterruptedException {
        assertEquals(ResponseEntity.ok(noAccessMessage),
                calendarController.getMyPersonalScheduleForCourseTeacher(wrongRequest,"courseid"));
    }

    @Test
    void testIndicateAbsenceSuccess() {
    }

    @Test
    void testIndicateAbsenceAccessDenied()
            throws IOException, InterruptedException {
        assertEquals(ResponseEntity.ok(noAccessMessage),
                calendarController.indicateAbsence(wrongRequest,"userid", "courseid", dates[0]));
    }

    @Test
    void testGetPhysicalAttendantsForLectureSuccess() {
    }

    @Test
    void testGetPhysicalAttendantsForLectureAccessDenied()
        throws IOException, InterruptedException {
        assertEquals(ResponseEntity.ok(noAccessMessage), calendarController.getPhysicalAttendantsForLecture(wrongRequest, "courseid", dates[0]));
    }

    @Test
    void testCreateLectureIdPhysicalMap() {
        Map<Integer, Boolean> physicalMap = new HashMap<>();

        for (Attendance attendance : attendances.subList(1, 3)) {
            physicalMap.put(attendance.getLectureId(), attendance.getPhysical());
        }
        assertEquals(physicalMap, calendarController.createLectureIdPhysicalMap(netIds[1]));
        verify(attendanceRepository, times(1)).findByStudentId(netIds[1]);
    }
}
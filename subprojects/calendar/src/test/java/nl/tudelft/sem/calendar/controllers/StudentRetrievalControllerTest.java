package nl.tudelft.sem.calendar.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.calendar.communication.RoomCommunicator;
import nl.tudelft.sem.calendar.entities.Attendance;
import nl.tudelft.sem.calendar.entities.Course;
import nl.tudelft.sem.calendar.entities.Lecture;
import nl.tudelft.sem.calendar.entities.Room;
import nl.tudelft.sem.calendar.exceptions.ServerErrorException;
import nl.tudelft.sem.calendar.repositories.AttendanceRepository;
import nl.tudelft.sem.calendar.repositories.LectureRepository;
import nl.tudelft.sem.calendar.util.Validate;
import nl.tudelft.sem.shared.Constants;
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
@WebMvcTest(StudentRetrievalController.class)
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")

class StudentRetrievalControllerTest {
    private HttpServletRequest wrongRequest;
    private HttpServletRequest studentRequest;
    private HttpServletRequest teacherRequest;
    private LocalDate[] dates;
    private Course[] courses;
    private String[] netIds;
    private List<Attendance> attendances;
    private List<Lecture> lecturesToSchedule;
    private List<Room> rooms;

    @MockBean
    private AttendanceRepository attendanceRepository;

    @MockBean
    private LectureRepository lectureRepository;

    @MockBean
    private RoomCommunicator roomCommunicator;

    @MockBean
    private Validate validate;

    @InjectMocks
    private StudentRetrievalController retrievalController;

    @BeforeEach
    void setup() throws InterruptedException, ServerErrorException, IOException {
        dates = new LocalDate[] {
                LocalDate.of(2020, 12, 17),
                LocalDate.of(2020, 12, 22)};

        rooms = List.of(new Room(1, "Class", 2),
                new Room(2, "IZ - 2", 20));

        netIds = new String[] { "mbjdegoede", "cparlar", "abobe", "teacher1", "teacher2" };

        studentRequest = Mockito.mock(HttpServletRequest.class);
        teacherRequest = Mockito.mock(HttpServletRequest.class);
        wrongRequest = Mockito.mock(HttpServletRequest.class);

        createCourses();
        createLecturesAndAttendances();
        configureMocks();

        retrievalController = new StudentRetrievalController(roomCommunicator,
                attendanceRepository, lectureRepository, validate);
    }

    private void createCourses() {
        courses = new Course[] { new Course(List.of(netIds).subList(0, 2),
                "CSE1200", "Calculus", "teacher1"),
                    new Course(List.of(netIds).subList(2, 3), "CSE2200",
                            "someCourse", "teacher2")};

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
        when(validate.validateRole(studentRequest, "student"))
                .thenReturn(netIds[1]);
        when(validate.validateRole(teacherRequest, "teacher"))
                .thenReturn(netIds[3]);
        when(validate.validateRole(wrongRequest, "teacher"))
                .thenReturn(Constants.noAccessMessage.getMessage());
        when(validate.validateRole(wrongRequest, "student"))
                .thenReturn(Constants.noAccessMessage.getMessage());

        when(attendanceRepository.findByStudentId(netIds[1])).thenReturn(attendances.subList(1, 3));

        when(lectureRepository.findByLectureId(lecturesToSchedule.get(0).getLectureId()))
            .thenReturn(lecturesToSchedule.get(0));
        when(lectureRepository.findByLectureId(lecturesToSchedule.get(1).getLectureId()))
            .thenReturn(lecturesToSchedule.get(1));
        when(lectureRepository.findByCourseId(lecturesToSchedule.get(1).getCourseId()))
            .thenReturn(Arrays.asList(lecturesToSchedule.get(1)));
        when(lectureRepository.findByDate(dates[0].plusDays(1)))
            .thenReturn(Arrays.asList(lecturesToSchedule.get(0), lecturesToSchedule.get(1)));

        when(roomCommunicator.getRoomName(lecturesToSchedule.get(0).getLectureId()))
            .thenReturn(lecturesToSchedule.get(0).getRoomName());
        when(roomCommunicator.getRoomName(lecturesToSchedule.get(1).getLectureId()))
            .thenReturn(lecturesToSchedule.get(1).getRoomName());

        when(lectureRepository.findByDateAndCourseId(any(), any()))
            .thenReturn(lecturesToSchedule.subList(0, 2));

        when(attendanceRepository.findByLectureIdAndStudentId(
                lecturesToSchedule.get(0).getLectureId(), netIds[1]))
                .thenReturn(attendances.subList(1, 2));
    }

    @Test
    void testGetMyPersonalScheduleStudentSuccess()
            throws InterruptedException, ServerErrorException, IOException {

        List<Lecture> result = (List<Lecture>) retrievalController
            .getMyPersonalScheduleStudent(studentRequest).getBody();
        assertEquals(2, Objects.requireNonNull(result).size());

        assertTrue(result.get(1).isSelectedForOnCampus());
        assertFalse(result.get(0).isSelectedForOnCampus());

        assertEquals(lecturesToSchedule.get(0).getRoomName(),
                result.get(0).getRoomName());
        assertEquals(lecturesToSchedule.get(1).getRoomName(),
                result.get(1).getRoomName());

        verify(lectureRepository, times(1))
                .findByLectureId(lecturesToSchedule.get(0).getLectureId());
        verify(lectureRepository, times(1))
                .findByLectureId(lecturesToSchedule.get(1).getLectureId());
        verify(roomCommunicator, times(1))
                .getRoomName(lecturesToSchedule.get(0).getLectureId());
        verify(roomCommunicator, times(1))
                .getRoomName(lecturesToSchedule.get(1).getLectureId());
    }

    @Test
    void testGetMyPersonalScheduleStudentAccessDenied() 
            throws InterruptedException, ServerErrorException, IOException {
        assertThat(retrievalController.getMyPersonalScheduleStudent(wrongRequest).getStatusCode())
            .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void testGetMyPersonalScheduleForDayStudentSuccess()
                throws InterruptedException, ServerErrorException, IOException {

        ResponseEntity<List<Lecture>> result =
            (ResponseEntity<List<Lecture>>) retrievalController
                .getMyPersonalScheduleForDayStudent(studentRequest, dates[0]);
        assertEquals(2, Objects.requireNonNull(result.getBody()).size());

        assertTrue(result.getBody().get(1).isSelectedForOnCampus());
        assertFalse(result.getBody().get(0).isSelectedForOnCampus());

        assertEquals(lecturesToSchedule.get(0).getRoomName(),
                result.getBody().get(0).getRoomName());
        assertEquals(lecturesToSchedule.get(1).getRoomName(),
                result.getBody().get(1).getRoomName());

        verify(lectureRepository, times(1))
                .findByDate(dates[0].plusDays(1));
        verify(roomCommunicator, times(1))
                .getRoomName(lecturesToSchedule.get(0).getLectureId());
        verify(roomCommunicator, times(1))
                .getRoomName(lecturesToSchedule.get(1).getLectureId());
    }

    @Test
    void testGetMyPersonalScheduleForDayStudentAccessDenied() 
            throws InterruptedException, ServerErrorException, IOException {
        assertEquals(ResponseEntity.ok(Constants.noAccessMessage),
                retrievalController.getMyPersonalScheduleForDayStudent(wrongRequest, dates[0]));
    }

    @Test
    void testGetMyPersonalScheduleForCourseStudentSuccess()
            throws InterruptedException, ServerErrorException, IOException {

        List<Lecture> response =
            (List<Lecture>) retrievalController.getMyPersonalScheduleForCourseStudent(
                    studentRequest, lecturesToSchedule.get(1).getCourseId()).getBody();

        assertEquals(1, Objects.requireNonNull(response).size());
        assertTrue(response.get(0).isSelectedForOnCampus());
        assertEquals(lecturesToSchedule.get(1).getRoomName(),
                response.get(0).getRoomName());

        verify(lectureRepository, times(1)).findByCourseId(
                lecturesToSchedule.get(1).getCourseId());
        verify(roomCommunicator, times(1)).getRoomName(
                lecturesToSchedule.get(1).getLectureId());
    }

    @Test
    void testGetMyPersonalScheduleForCourseStudentAccessDenied() 
            throws InterruptedException, ServerErrorException, IOException {
        assertEquals(ResponseEntity.ok(Constants.noAccessMessage),
                retrievalController.getMyPersonalScheduleForCourseStudent(
                        wrongRequest, courses[0].getCourseId()));
    }

    @Test
    void testCreateLectureIdPhysicalMap() {
        Map<Integer, Boolean> physicalMap = new HashMap<>();

        for (Attendance attendance : attendances.subList(1, 3)) {
            physicalMap.put(attendance.getLectureId(), attendance.getPhysical());
        }
        assertEquals(physicalMap, retrievalController.createLectureIdPhysicalMap(netIds[1]));
        verify(attendanceRepository, times(1)).findByStudentId(netIds[1]);
    }
}
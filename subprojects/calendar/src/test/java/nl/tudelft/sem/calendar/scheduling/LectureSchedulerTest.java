package nl.tudelft.sem.calendar.scheduling;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import nl.tudelft.sem.calendar.entities.Attendance;
import nl.tudelft.sem.calendar.entities.Course;
import nl.tudelft.sem.calendar.entities.Lecture;
import nl.tudelft.sem.calendar.entities.Room;
import nl.tudelft.sem.calendar.repositories.AttendanceRepository;
import nl.tudelft.sem.calendar.repositories.LectureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class LectureSchedulerTest {
    private transient List<Room> roomList;
    private transient List<Lecture> lecturesToSchedule;
    private transient LocalTime startTime;
    private transient LocalTime endTime;
    private transient int timeGapLengthInMinutes;

    @Autowired
    transient LectureScheduler scheduler;

    // We mock the repositories
    @MockBean
    transient LectureRepository lectureRepository;
    @MockBean
    transient AttendanceRepository attendanceRepository;

    // Objects used in the test cases, stored in arrays.
    private transient Room[] testRooms;
    private transient Lecture[] lectures;
    private transient Course[] testCourses;
    private transient LocalDate[] testDates;
    private transient String[] netIds;

    /**
     * Helper method to create a set of rooms used in the test cases.
     */
    private void createRooms() {
        testRooms = new Room[5];
        testRooms[0] = new Room(1, "Class", 2);
        testRooms[1] = new Room(2, "IZ - 2", 20);
        testRooms[2] = new Room(3, "W - 2", 30);
        testRooms[3] = new Room(4, "IZ - 4", 15);
        testRooms[4] = new Room(5, "TS - 3", 25);
        roomList = Arrays.asList(testRooms[0], testRooms[1],
                testRooms[2], testRooms[3], testRooms[4]);
    }

    /**
     * Helper method to create a set of courses used in the test cases.
     */
    private void createCourses() {
        testCourses = new Course[3];
        testCourses[0] = new Course(
                Arrays.asList("someNetId", "someNetId2"));
        testCourses[1] = new Course(
                Arrays.asList("abobe", "mbjdegoede", "cparlar"));
        testCourses[2] = new Course(Collections.singletonList("mdavid"));
    }

    /**
     * Helper method to create a set of dates used in the test cases.
     */
    private void createDates() {
        testDates = new LocalDate[3];
        testDates[0] = LocalDate.of(2020, 2, 1);
        testDates[1] = LocalDate.of(2020, 2, 13);
        testDates[2] = LocalDate.of(2020, 2, 15);
    }

    /**
     * Helper method to create a set of lecture requests and scheduled lectures used for
     * verification in the test cases.
     */
    private void createLectures() {
        lectures = new Lecture[5];
        lectures[0] = Lecture.builder()
                .course(testCourses[0]).date(testDates[0]).durationInMinutes(90).build();
        lectures[1] = Lecture.builder()
                .course(testCourses[1]).date(testDates[0]).durationInMinutes(540).build();
        lectures[2] = Lecture.builder()
                .course(testCourses[2]).date(testDates[0]).durationInMinutes(100).build();
        lectures[3] = Lecture.builder()
                .course(testCourses[0]).date(testDates[1]).durationInMinutes(90).build();
        lectures[4] = Lecture.builder()
                .course(testCourses[0]).date(testDates[2]).durationInMinutes(200).build();

        lecturesToSchedule = Arrays.asList(lectures[0], lectures[1],
                lectures[2], lectures[3], lectures[4]);
    }

    /**
     * Helper method to create a map of the lecture requests grouped by day.
     *
     * @return a map of the lecture requests grouped by day.
     */
    private HashMap<LocalDate, List<Lecture>> createMapOfLecturesByDay() {
        return new HashMap<>() {
            {
                put(testDates[0], Arrays.asList(lectures[0],
                        lectures[1],lectures[2]));
                put(testDates[1], Collections.singletonList(lectures[3]));
                put(testDates[2], Collections.singletonList(lectures[4]));
            }
        };
    }

    /**
     * Helper method to create a Map of course participants, each having their own deadline.
     *
     * @return a map with the studentIds and the deadlines of the course participants.
     */
    private HashMap<String, LocalDate> createParticipants() {
        return new HashMap<>() {
            {
                put("abobe", LocalDate.of(2020, 12, 26));
                put("cparlar", LocalDate.of(2020, 12, 24));
                put("random", LocalDate.of(2020, 12, 29));
            }
        };
    }

    /**
     * Creates a lecture scheduler next to a bunch of rooms, courses, dates and lectures used for
     * verification in all test cases.
     */
    @BeforeEach
    void setup() {
        createRooms();
        createCourses();
        createDates();
        createLectures();
        netIds = new String[] {"mbjdegoede", "abobe", "cparlar"};
        timeGapLengthInMinutes = 45;
        startTime = LocalTime.of(8, 45);
        endTime = LocalTime.of(17, 45);
        scheduler.setFields(roomList, lecturesToSchedule, startTime, endTime, timeGapLengthInMinutes);
    }

    @Test
    void testScheduleAllLectures() {

        // to be implemented. should do integration like test, we can again pass list with
        // a lot of lectures to be scheduled and then verify that the repositories are called
        // with certain attributes

        //scheduler.scheduleAllLectures();
    }


    /**
     * Tests whether all properties of the scheduler are correctly set upon initialization.
     */
    @Test
    void testSetFields() {
        assertEquals(roomList, scheduler.getRoomList());
        assertEquals(lecturesToSchedule, scheduler.getLecturesList());
        assertEquals(startTime, scheduler.getStartTime());
        assertEquals(endTime, scheduler.getEndTime());
        assertEquals(timeGapLengthInMinutes, scheduler.getTimeGapLengthInMinutes());
        assertEquals(new HashMap<String, LocalDate>(), scheduler.getAllParticipants());
        assertEquals(0, scheduler.getRoomSearchIndex());
        for(LocalTime lt: scheduler.getRoomAvailability()) {
            assertEquals(startTime, lt);
        }
    }

    /**
     * Tests whether all course participants are selected to attend the lecture on campus when the
     * lecture is scheduled in a room that has a capacity that is high enough to invite them all to
     * the campus.
     */
    @Test
    void testAssignStudentsEnoughCapacity() {

        lectures[1].setRoomId(testRooms[1].getRoomId());
        Map<String, LocalDate> allParticipants = createParticipants();
        scheduler.assignStudents(testRooms[1].getCapacity(), lectures[1], allParticipants);


        Attendance attendance1 = Attendance.builder().lectureId(lectures[1].getLectureId())
        .physical(true).studentId("abobe").build();
            verify(attendanceRepository, times(1)).saveAndFlush(attendance1);

        Attendance attendance2 = Attendance.builder().lectureId(lectures[1].getLectureId())
        .physical(true).studentId("mbjdegoede").build();
                verify(attendanceRepository, times(1)).saveAndFlush(attendance2);

        Attendance attendance3 = Attendance.builder().lectureId(lectures[1].getLectureId())
        .physical(true).studentId("cparlar").build();
                verify(attendanceRepository, times(1)).saveAndFlush(attendance3);
    }

    /**
     * Tests whether the students with the earliest deadline are selected to to attend the lecture
     * on campus when the lecture is scheduled in a room that doesn't have a capacity high enough to
     * fit all the course participants. It also checks whether the deadlines of the selected
     * students are advanced by 14 days.
     */
    @Test
    void testAssignStudentsButNotEnoughCapacity() {
        lectures[1].setRoomId(testRooms[0].getRoomId());
        Map<String, LocalDate> allParticipants = createParticipants();
        scheduler.assignStudents(testRooms[0].getCapacity(), lectures[1], allParticipants);


        Attendance attendance1 = Attendance.builder().lectureId(lectures[1].getLectureId())
                .physical(false).studentId("abobe").build();
        verify(attendanceRepository, times(1)).saveAndFlush(attendance1);

        Attendance attendance2 = Attendance.builder().lectureId(lectures[1].getLectureId())
                .physical(true).studentId("mbjdegoede").build();
        verify(attendanceRepository, times(1)).saveAndFlush(attendance2);

        Attendance attendance3 = Attendance.builder().lectureId(lectures[1].getLectureId())
                .physical(true).studentId("cparlar").build();
        verify(attendanceRepository, times(1)).saveAndFlush(attendance3);

        verify(attendanceRepository, times(3)).saveAndFlush(any());
    }

    /**
     * Tests whether the sorting of the rooms by decreasing capacity works as expected.
     */
    @Test
    void testSortRoomsByCapacity() {
        scheduler.sortRoomsByCapacity();
        assertThat(scheduler.getRoomList()).containsExactly(testRooms[2], testRooms[4],
                testRooms[1], testRooms[3], testRooms[0]);
    }

    /**
     * Tests whether the grouping of lecture requests by day works as expected.
     */
    @Test
    void testGroupLecturesByDay() {
        scheduler.groupLecturesByDay();
        Map<LocalDate, List<Lecture>> groupedByDay = createMapOfLecturesByDay();
        assertThat(scheduler.groupLecturesByDay()).isEqualTo(groupedByDay);
    }

    /**
     * Tests whether getting the sorted lectures for a specific day works as expected.
     */
    @Test
    void testGetSortedLecturesForDay() {
        assertThat(scheduler.getSortedLecturesForDay(testDates[0], createMapOfLecturesByDay()))
                .containsExactly(lectures[1], lectures[0], lectures[2]);
        assertThat(scheduler.getSortedLecturesForDay(testDates[1], createMapOfLecturesByDay()))
                .containsExactly(lectures[3]);
    }

    /**
     * Tests whether a priority queue with the right characteristics is created to select the on
     * campus students. i.e: the course participants with the earliest deadline are in front of the
     * queue. It also checks that course participants that aren't in the global map yet will be
     * added and that the deadlines of participants already in the map are taken and left
     * unchanged.
     */
    @Test
    void testCreateCandidateSelector() {
        List<String> courseParticipants = Arrays.asList(netIds[1], netIds[0], netIds[2]);
        HashMap<String, LocalDate> allParticipants = createParticipants();
        LocalDate lectureDate = LocalDate.of(2020, 12, 18);

        OnCampusCandidate[] verification = {
            new OnCampusCandidate(netIds[0],
                    LocalDate.of(2020, 12, 18)),
            new OnCampusCandidate(netIds[2],
                    LocalDate.of(2020, 12, 24)),
            new OnCampusCandidate(netIds[0],
                    LocalDate.of(2020, 12, 26))
        };

        // maybe split this test case up into multiple parts??

        @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
        // this is just the priority queue being created and checked afterwards.
        PriorityQueue<OnCampusCandidate> result =
                scheduler.createCandidateSelector(lectureDate, courseParticipants, allParticipants);

        for (OnCampusCandidate onCampusCandidate : verification) {
            OnCampusCandidate candidate = result.remove();

            assertThat(candidate.getDeadline()).isEqualTo(onCampusCandidate.getDeadline());
        }
        assertThat(allParticipants.get(netIds[1])).isEqualTo(LocalDate.of(2020, 12, 26));
        assertThat(allParticipants.get(netIds[0])).isEqualTo(LocalDate.of(2020, 12, 18));
    }

    /**
     * Tests whether the first lecture to be scheduled is scheduled in the biggest lecture room.
     */
    @Test
    void testAssignFirstLectureRoom() {
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(lectures[0], lectures[0].getDurationInMinutes());
        assertThat(lectures[0].getRoomId()).isEqualTo(testRooms[2].getRoomId());
    }

    /**
     * Tests whether the lecture is not scheduled in any room if there's no available room.
     * It also tests whether the right room capacity is returned.
     */
    @Test
    void testAssignFirstLectureNoRoomLeft() {
        scheduler.sortRoomsByCapacity();
        scheduler.setRoomSearchIndex(roomList.size());
        assertThat(scheduler.assignRoom(lectures[0],
                lectures[0].getDurationInMinutes())).isEqualTo(0);
        assertThat(lectures[0].getRoomId()).isNull();
    }

    /**
     * Tests whether a second lecture to be scheduled is scheduled in the same room if it still fits
     * for that day. It also tests whether the right room capacity is returned.
     */
    @Test
    void testAssignSecondLectureSameRoomWithinTime() {
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(lectures[0], lectures[0].getDurationInMinutes());
        assertThat(scheduler.assignRoom(lectures[2],
                lectures[2].getDurationInMinutes())).isEqualTo(testRooms[2].getCapacity());
        assertThat(lectures[2].getRoomId()).isEqualTo(testRooms[2].getRoomId());
    }

    /**
     * Tests whether a second lecture to be scheduled is scheduled in the one but biggest room if
     * the lecture doesn't fit in the largest room after the first lecture. It also tests whether
     * the right room capacity is returned.
     */
    @Test
    void testAssignSecondLectureSameRoomNotWithinTime() {
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(lectures[0], 540);
        assertThat(scheduler.assignRoom(lectures[1],
                lectures[1].getDurationInMinutes())).isEqualTo(testRooms[4].getCapacity());
        assertThat(lectures[1].getRoomId()).isEqualTo(testRooms[4].getRoomId());
    }

    /**
     * Tests whether a second lecture to be scheduled is scheduled in the same room if it just fits
     * for that day.
     */
    @Test
    void testAssignSecondLectureSameRoomJustWithinTime() {
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(lectures[0], 185);
        scheduler.assignRoom(lectures[1], 200);
        assertThat(lectures[1].getRoomId()).isEqualTo(testRooms[2].getRoomId());
    }

    /**
     * Tests whether the lecture second lecture is not scheduled in any room if there's no available
     * room after scheduling the first lecture.
     */
    @Test
    void testAssignSecondLectureNoRoomLeft() {
        scheduler.sortRoomsByCapacity();
        scheduler.setRoomSearchIndex(roomList.size() - 1);
        scheduler.assignRoom(lectures[0], 540);
        scheduler.assignRoom(lectures[1], 200);
        assertThat(lectures[1].getRoomId()).isNull();
    }

    /**
     * Tests whether the time comparison to determine if a lecture still fits in a room works as it
     * should by trying to plan a lecture with a duration that would end up on an earlier time than
     * the ending time, but the next day when it would be scheduled after the first lecture. It also
     * checks whether the room search index has advanced by one after the lecture couldn't be
     * scheduled in the room.
     */
    @Test
    void testAssignSecondLectureNoRoomLeftNextDayWrap() {
        scheduler.sortRoomsByCapacity();
        scheduler.setRoomSearchIndex(roomList.size() - 1);
        scheduler.assignRoom(lectures[0], 540);
        assertThat(scheduler.getRoomSearchIndex()).isEqualTo(roomList.size() - 1);
        scheduler.assignRoom(lectures[1], lectures[1].getDurationInMinutes());
        assertThat(lectures[1].getRoomId()).isNull();
        assertThat(scheduler.getRoomSearchIndex()).isEqualTo(roomList.size());
    }

    /**
     * Tests the getter for the room list attribute.
     */
    @Test
    void testGetRoomList() {
        assertThat(scheduler.getRoomList()).isEqualTo(roomList);
    }

    /**
     * Tests the getter and setter for the room search index attribute.
     */
    @Test
    void testGetSetRoomSearchIndex() {
        assertThat(scheduler.getRoomSearchIndex()).isEqualTo(0);
        scheduler.setRoomSearchIndex(10);
        assertThat(scheduler.getRoomSearchIndex()).isEqualTo(10);
    }
}
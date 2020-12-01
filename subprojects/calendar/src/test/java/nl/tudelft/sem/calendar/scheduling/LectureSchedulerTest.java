package nl.tudelft.sem.calendar.scheduling;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import nl.tudelft.sem.calendar.entities.Course;
import nl.tudelft.sem.calendar.entities.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LectureSchedulerTest {
    private static LectureScheduler scheduler;
    private static List<Room> roomList;
    private static List<RequestedLecture> lecturesToSchedule;
    private static LocalTime startTime;
    private static LocalTime endTime;
    private static int timeGapLengthInMinutes;

    // Objects used in the test cases, stored in arrays.
    private static Room[] testRooms;
    private static RequestedLecture[] testReqLectures;
    private static ScheduledLecture[] testSchedLectures;
    private static Course[] testCourses;
    private static LocalDate[] testDates;
    private static String[] netIds;

    /**
     * Helper method to create a set of rooms used in the test cases.
     */
    private static void createRooms() {
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
    private static void createCourses() {
        testCourses = new Course[3];
        testCourses[0] = new Course(
                Arrays.asList("someNetId", "someNetId2"));
        testCourses[1] = new Course(
                Arrays.asList("abobe", "mbjdegoede", "cparlar"));
        testCourses[2] = new Course(Arrays.asList("mdavid"));
    }

    /**
     * Helper method to create a set of dates used in the test cases.
     */
    private static void createDates() {
        testDates = new LocalDate[3];
        testDates[0] = LocalDate.of(2020, 2, 1);
        testDates[1] = LocalDate.of(2020, 2, 13);
        testDates[2] = LocalDate.of(2020, 2, 15);
    }

    /**
     * Helper method to create a set of lecture requests and scheduled lectures used for
     * verification in the test cases.
     */
    private static void createLectures() {
        testReqLectures = new RequestedLecture[5];
        testReqLectures[0] = new RequestedLecture(
                testCourses[0], testDates[0], 90);
        testReqLectures[1] = new RequestedLecture(
                testCourses[1], testDates[0], 540);
        testReqLectures[2] = new RequestedLecture(
                testCourses[2], testDates[0], 100);
        testReqLectures[3] = new RequestedLecture(
                testCourses[0], testDates[1], 90);
        testReqLectures[4] = new RequestedLecture(
                testCourses[0], testDates[2], 200);
        lecturesToSchedule = Arrays.asList(testReqLectures[0], testReqLectures[1],
                testReqLectures[2], testReqLectures[3], testReqLectures[4]);

        testSchedLectures = new ScheduledLecture[2];
        testSchedLectures[0] = new ScheduledLecture(
                testCourses[0], testDates[0]);
        testSchedLectures[1] = new ScheduledLecture(
                testCourses[1], testDates[0]);
    }

    /**
     * Helper method to create a map of the lecture requests grouped by day.
     *
     * @return a map of the lecture requests grouped by day.
     */
    private static HashMap<LocalDate, List<RequestedLecture>> createMapOfLecturesByDay() {
        return new HashMap<>() {
            {
                put(testDates[0], Arrays.asList(testReqLectures[0],
                        testReqLectures[1], testReqLectures[2]));
                put(testDates[1], Arrays.asList(testReqLectures[3]));
                put(testDates[2], Arrays.asList(testReqLectures[4]));
            }
        };
    }

    /**
     * Helper method to create a Map of course participants, each having their own deadline.
     *
     * @return a map with the studentIds and the deadlines of the course participants.
     */
    private static HashMap<String, LocalDate> createParticipants() {
        return new HashMap<>() {
            {
                put("abobe", LocalDate.of(2020, 12, 26));
                put("cparlar", LocalDate.of(2020, 12, 24));
                put("random", LocalDate.of(2020, 12, 29));
            }
        };
    }

    /**
     * Helper method to create random rooms used in the integration like
     * testCompleteSchedulingAlgorithmWithRandomValues method.
     *
     * @return a list of randomly generated rooms.
     */
    private static List<Room> createRandomRooms() {
        List<Room> realRoomList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            realRoomList.add(new Room(i,  "R - " + i, i * 10));
        }
        Collections.shuffle(realRoomList);
        return realRoomList;
    }

    /**
     * Helper method to create random participants used in the integration like
     * testCompleteSchedulingAlgorithmWithRandomValues method.
     *
     * @return a list of randomly generated netIds.
     */
    private static List<String> createRandomParticipants() {
        List<String> randomParticipants = new ArrayList<>();
        for (int i = 1; i <= 300; i++) {
            Random r = new Random();
            char c = (char) (r.nextInt(26) + 'a');
            randomParticipants.add(i + "" + c + c + c + c);
        }
        return randomParticipants;
    }

    /**
     * Helper method to create random courses with random students, used in the integration like
     * testCompleteSchedulingAlgorithmWithRandomValues method.
     *
     * @return a list of randomly generated courses, each having randomly picked students associated
     *      with them.
     */
    private static List<Course> createRandomCoursesWithRandomStudents(
            List<String> allParticipants) {
        List<Course> randomCourses = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            Collections.shuffle(allParticipants);
            Random random = new Random();
            int size = random.nextInt(300 - 30) + 30;
            List<String> courseParticipants = allParticipants.subList(0, size);
            randomCourses.add(new Course(courseParticipants));
        }
        return randomCourses;
    }

    /**
     * Helper method to create a given number of subsequent dates used in the sed in the integration
     * like testCompleteSchedulingAlgorithmWithRandomValues method.
     *
     * @param numberOfDates the number of subsequent dates to generate
     * @return a list of subsequent dates
     */
    private static List<LocalDate> createSubSequentDates(int numberOfDates) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate firstDate = LocalDate.of(2020, 9, 1);
        dates.add(firstDate);
        for (int i = 1; i <= numberOfDates; i++) {
            dates.add(firstDate.plusDays(i));
        }
        return dates;
    }

    /**
     * Helper method to create random lecture requests, used in the sed in the integration like
     * testCompleteSchedulingAlgorithmWithRandomValues method.
     *
     * @return a list of randomly generated requested lectures.
     */
    private static List<RequestedLecture> createRandomLectureRequests() {
        @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
        // there is no problem with using the realCourseList this way
        List<Course> realCourseList = createRandomCoursesWithRandomStudents(
                createRandomParticipants());
        @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
        // the same applies for the real dates
        List<LocalDate> realDates = createSubSequentDates(10);

        // Composing the lecture requests
        List<RequestedLecture> realRequestedLectures = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            Random random = new Random();
            int index = random.nextInt(240);
            realRequestedLectures.add(new RequestedLecture(realCourseList.get(index % 40),
                    realDates.get(index % 10), index));
        }
        return realRequestedLectures;
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
        scheduler = new LectureScheduler(roomList, lecturesToSchedule,
                startTime, endTime, timeGapLengthInMinutes);
    }

    /**
     * Integration like test method that tests the entire algorithm with a large number of randomly
     * generated lectures, rooms courses and students. It tests whether all individually tested
     * components work together as expected. The result list can be manually inspected to verify
     * that the algorithm performs as in the requirements.
     */
    @Test
    void testCompleteSchedulingAlgorithmWithRandomValues() {
        List<Room> realRoomList = createRandomRooms();
        List<RequestedLecture> realRequestedLectures = createRandomLectureRequests();

        LectureScheduler realLectureScheduler = new LectureScheduler(realRoomList,
                realRequestedLectures, startTime, endTime, timeGapLengthInMinutes);

        List<ScheduledLecture> result =
                realLectureScheduler.scheduleAllLectures();

        // Going to the list of scheduled lectures, checking all the subsequent elements
        for (int i = 0; i < result.size() - 2; i++) {
            if (result.get(i).getDate().equals(result.get(i+1).getDate())) {
                //assert that the rooms are sorted in descending order
                if (result.get(i).getRoom() != null && result.get(i+1).getRoom() != null) {
                    assertTrue(result.get(i).getRoom().getCapacity()
                            >= result.get(i+1).getRoom().getCapacity());
                }
                //assert that size of courses are sorted in descending order
                assertTrue(result.get(i).getStudentsOnCampus().size()
                        >= result.get(i+1).getStudentsOnCampus().size());
            }
            //assert that the dates are sorted in ascending order
            assertTrue(result.get(i).getDate().isBefore(result.get(i+1).getDate())
                    || result.get(i).getDate().isEqual(result.get(i+1).getDate()));
        }
    }

    /**
     * Tests whether all course participants are selected to attend the lecture on campus when the
     * lecture is scheduled in a room that has a capacity that is high enough to invite them all to
     * the campus.
     */
    @Test
    void testAssignStudentsEnoughCapacity() {
        Map<String, LocalDate> allParticipants = createParticipants();
        testSchedLectures[1].setRoom(testRooms[1]);
        assertThat(testSchedLectures[1].getStudentsOnCampus()).isEmpty();
        scheduler.assignStudents(testSchedLectures[1], allParticipants);
        assertThat(testSchedLectures[1].getStudentsOnCampus().size()).isEqualTo(3);
    }

    /**
     * Tests whether the students with the earliest deadline are selected to to attend the lecture
     * on campus when the lecture is scheduled in a room that doesn't have a capacity high enough to
     * fit all the course participants. It also checks whether the deadlines of the selected
     * students are advanced by 14 days.
     */
    @Test
    void testAssignStudentsButNotEnoughCapacity() {
        Map<String, LocalDate> allParticipants = createParticipants();
        testSchedLectures[1].setRoom(testRooms[0]);
        assertThat(testSchedLectures[1].getStudentsOnCampus()).isEmpty();
        scheduler.assignStudents(testSchedLectures[1], allParticipants);

        assertThat(testSchedLectures[1].getStudentsOnCampus())
                .containsExactly(netIds[0], netIds[2]);

        assertThat(allParticipants.get(netIds[0])).isEqualTo(
                testSchedLectures[1].getDate().plusDays(14));
        assertThat(allParticipants.get(netIds[2])).isEqualTo(
                testSchedLectures[1].getDate().plusDays(14));
        assertThat(allParticipants.get(netIds[1])).isEqualTo(LocalDate.of(2020, 12, 26));
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
        Map<LocalDate, List<RequestedLecture>> groupedByDay = createMapOfLecturesByDay();
        assertThat(scheduler.groupLecturesByDay()).isEqualTo(groupedByDay);
    }

    /**
     * Tests whether getting the sorted lectures for a specific day works as expected.
     */
    @Test
    void testGetSortedLecturesForDay() {
        assertThat(scheduler.getSortedLecturesForDay(testDates[0], createMapOfLecturesByDay()))
                .containsExactly(testReqLectures[1], testReqLectures[0], testReqLectures[2]);
        assertThat(scheduler.getSortedLecturesForDay(testDates[1], createMapOfLecturesByDay()))
                .containsExactly(testReqLectures[3]);
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
        scheduler.assignRoom(testSchedLectures[0], testReqLectures[0].getDurationInMinutes());
        assertThat(testSchedLectures[0].getRoom()).isEqualTo(testRooms[2]);
    }

    /**
     * Tests whether the lecture is not scheduled in any room if there's no available room.
     */
    @Test
    void testAssignFirstLectureNoRoomLeft() {
        scheduler.sortRoomsByCapacity();
        scheduler.setRoomSearchIndex(roomList.size());
        scheduler.assignRoom(testSchedLectures[0], testReqLectures[0].getDurationInMinutes());
        assertThat(testSchedLectures[0].getRoom()).isNull();
    }

    /**
     * Tests whether a second lecture to be scheduled is scheduled in the same room if it still fits
     * for that day.
     */
    @Test
    void testAssignSecondLectureSameRoomWithinTime() {
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(testSchedLectures[0], testReqLectures[0].getDurationInMinutes());
        scheduler.assignRoom(testSchedLectures[1], testReqLectures[1].getDurationInMinutes());
        assertThat(testSchedLectures[0].getRoom()).isEqualTo(testRooms[2]);
    }

    /**
     * Tests whether a second lecture to be scheduled is scheduled in the one but biggest room if
     * the lecture doesn't fit in the largest room after the first lecture.
     */
    @Test
    void testAssignSecondLectureSameRoomNotWithinTime() {
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(testSchedLectures[0], 540);
        scheduler.assignRoom(testSchedLectures[1], testReqLectures[1].getDurationInMinutes());
        assertThat(testSchedLectures[1].getRoom()).isEqualTo(testRooms[4]);
    }

    /**
     * Tests whether a second lecture to be scheduled is scheduled in the same room if it just fits
     * for that day.
     */
    @Test
    void testAssignSecondLectureSameRoomJustWithinTime() {
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(testSchedLectures[0], 185);
        scheduler.assignRoom(testSchedLectures[1], 200);
        assertThat(testSchedLectures[1].getRoom()).isEqualTo(testRooms[2]);
    }

    /**
     * Tests whether the lecture second lecture is not scheduled in any room if there's no available
     * room after scheduling the first lecture.
     */
    @Test
    void testAssignSecondLectureNoRoomLeft() {
        scheduler.sortRoomsByCapacity();
        scheduler.setRoomSearchIndex(roomList.size() - 1);
        scheduler.assignRoom(testSchedLectures[0], 540);
        scheduler.assignRoom(testSchedLectures[1], 200);
        assertThat(testSchedLectures[1].getRoom()).isNull();
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
        scheduler.assignRoom(testSchedLectures[0], 540);
        assertThat(scheduler.getRoomSearchIndex()).isEqualTo(roomList.size() - 1);
        scheduler.assignRoom(testSchedLectures[1], testReqLectures[1].getDurationInMinutes());
        assertThat(testSchedLectures[1].getRoom()).isNull();
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
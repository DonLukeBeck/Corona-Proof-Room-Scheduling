package nl.tudelft.sem.calendar.controllers;

import nl.tudelft.sem.calendar.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LectureSchedulerTest {
 private static LectureScheduler scheduler;
 private static List<Room> roomList;
 private static List<RequestedLecture> lecturesToSchedule;
 private static LocalTime startTime;
 private static LocalTime endTime;
 private static int timeGapLengthInMinutes;

 // Objects used in the test cases
 private static Room[] testRooms;
 private static RequestedLecture[] testReqLectures;
 private static ScheduledLecture[] testSchedLectures;
 private static Course[] testCourses;
 private static LocalDate[]  testDates;

    @BeforeEach
    void setup(){
        createRooms();
        createCourses();
        createDates();
        createLectures();
        timeGapLengthInMinutes = 45;
        startTime = LocalTime.of(8, 45);
        endTime = LocalTime.of(17, 45);
        scheduler = new LectureScheduler(roomList, lecturesToSchedule,
                startTime, endTime, timeGapLengthInMinutes);
    }

    private static void createRooms(){
        testRooms = new Room[5];
        testRooms[0] = new Room(1, 2, "Class");
        testRooms[1] = new Room(2, 20, "IZ-2");
        testRooms[2] = new Room(3, 30, "W-2");
        testRooms[3] = new Room(4, 15, "IZ-4");
        testRooms[4] = new Room(5, 25, "TS-3");
        roomList = Arrays.asList(testRooms[0], testRooms[1],
                testRooms[2], testRooms[3], testRooms[4]);
    }

    private static void createCourses(){
        testCourses = new Course[3];
        testCourses[0] = new Course(Arrays.asList("someNetId","someNetId2"));
        testCourses[1] = new Course(Arrays.asList("abobe","mbjdegoede","cparlar"));
        testCourses[2] = new Course(Arrays.asList("mdavid"));
    }

    private static void createDates(){
        testDates = new LocalDate[3];
        testDates[0] = LocalDate.of(2020, 2, 1);
        testDates[1] = LocalDate.of(2020, 2, 13);
        testDates[2] = LocalDate.of(2020, 2, 15);
    }

    private static void createLectures(){
        testReqLectures = new RequestedLecture[5];
        testReqLectures[0] = new RequestedLecture(testCourses[0], testDates[0], 90);
        testReqLectures[1] = new RequestedLecture(testCourses[1], testDates[0], 540);
        testReqLectures[2] = new RequestedLecture(testCourses[2], testDates[0], 100);
        testReqLectures[3] = new RequestedLecture(testCourses[0], testDates[1], 90);
        testReqLectures[4] = new RequestedLecture(testCourses[0], testDates[2], 200);
        lecturesToSchedule = Arrays.asList(testReqLectures[0], testReqLectures[1],
                testReqLectures[2], testReqLectures[3], testReqLectures[4]);

        testSchedLectures = new ScheduledLecture[2];
        testSchedLectures[0] = new ScheduledLecture(testCourses[0],testDates[0]);
        testSchedLectures[1] = new ScheduledLecture(testCourses[1], testDates[0]);
    }

    private static HashMap<LocalDate, List<RequestedLecture>> createMap(){
        return new HashMap<>() {{
            put(testDates[0], Arrays.asList(testReqLectures[0],
                    testReqLectures[1], testReqLectures[2]));
            put(testDates[1], Arrays.asList(testReqLectures[3]));
            put(testDates[2], Arrays.asList(testReqLectures[4]));
        }};
    }

    private static HashMap<String, LocalDate> createParticipants(){
        return new HashMap<>() {{
            put("abobe", LocalDate.of(2020, 12, 26));
            put("cparlar", LocalDate.of(2020, 12, 24));
            put("random", LocalDate.of(2020, 12, 29));
        }};
    }

    @Test
    void testScheduleAllLecturesMoreThanTwoWeeks() {
        List<Room> realRoomList = new ArrayList<Room>();
        for(int i = 1; i <= 20; i++) {
            realRoomList.add(new Room(i, i * 100, "R-" + i));
        }
        Collections.shuffle(realRoomList);

        List<String> realParticipants = new ArrayList<String>();
        for(int i = 1; i <= 300; i++) {
            Random r = new Random();
            char c = (char)(r.nextInt(26) + 'a');
            realParticipants.add(i + "" + c + c + c + c);
        }

        List<String> courseParticipants = new ArrayList<String>();
        List<Course> realCourseList = new ArrayList<Course>();
        for(int i = 1; i <= 40; i++) {
            Collections.shuffle(realParticipants);
            Random random = new Random();
            int size = random.nextInt(300 - 30) + 30;
            courseParticipants = realParticipants.subList(0,size);
            realCourseList.add(new Course(courseParticipants));
        }

        List<LocalDate> realDates = new ArrayList<LocalDate>();
        LocalDate firstDate = LocalDate.of(2020,9,1);
        for(int i = 1; i <= 60; i++) {
            realDates.add(firstDate.plusDays(i));
        }


        List<RequestedLecture> realRequestedLectures = new ArrayList<RequestedLecture>();
        for(int i = 1; i <= 50; i++) {
            Random random = new Random();
            int index = random.nextInt(240);
            realRequestedLectures.add(new RequestedLecture(realCourseList.get(index % 40), realDates.get(index % 60), index));
        }



        LectureScheduler realLectureScheduler = new LectureScheduler(realRoomList, realRequestedLectures,
                startTime, endTime, timeGapLengthInMinutes);
        List<ScheduledLecture> result = realLectureScheduler.scheduleAllLectures();
        for(int i = 0; i < result.size() - 1; i++) {
            for(int j = i + 1; j < result.size(); j++) {

                if(result.get(i).getDate().equals(result.get(j).getDate())) {
                    //assert that the rooms are sorted descending
                    assertTrue(result.get(i).getRoom().getCapacity() >= result.get(j).getRoom().getCapacity());

                    //assert that size of courses are sorted
                    assertTrue(result.get(i).getStudentsOnCampus().size() >= result.get(j).getStudentsOnCampus().size());
                }
            }
        }


    }

    @Test
    void testAssignStudentsEnoughCapacity() {
        Map<String, LocalDate> allParticipants = createParticipants();
        testSchedLectures[1].setRoom(testRooms[1]);
        assertThat(testSchedLectures[1].getStudentsOnCampus()).isEmpty();
        scheduler.assignStudents(testSchedLectures[1], allParticipants);
        assertThat(testSchedLectures[1].getStudentsOnCampus().size()).isEqualTo(3);
    }

    @Test
    void testAssignStudentsButNotEnoughCapacity() {
        Map<String, LocalDate> allParticipants = createParticipants();
        testSchedLectures[1].setRoom(testRooms[0]);
        assertThat(testSchedLectures[1].getStudentsOnCampus()).isEmpty();
        scheduler.assignStudents(testSchedLectures[1], allParticipants);

        assertThat(testSchedLectures[1].getStudentsOnCampus())
                .containsExactly("mbjdegoede","cparlar");

        assertThat(allParticipants.get("mbjdegoede")).isEqualTo(testSchedLectures[1].getDate().plusDays(14));
        assertThat(allParticipants.get("cparlar")).isEqualTo(testSchedLectures[1].getDate().plusDays(14));
        assertThat(allParticipants.get("abobe")).isEqualTo(LocalDate.of(2020, 12, 26));
    }

    @Test
    void testSortRoomsByCapacity() {
        scheduler.sortRoomsByCapacity();
        assertThat(scheduler.getRoomList()).containsExactly(testRooms[2],testRooms[4],testRooms[1]
                ,testRooms[3],testRooms[0]);
    }

    @Test
    void testGroupLecturesByDay() {
        scheduler.groupLecturesByDay();
        Map<LocalDate, List<RequestedLecture>> groupedByDay = createMap();
        assertThat(scheduler.groupLecturesByDay()).isEqualTo(groupedByDay);
    }

    @Test
    void testGetSortedLecturesForDay() {
        assertThat(scheduler.getSortedLecturesForDay(testDates[0], createMap()))
                .containsExactly(testReqLectures[1],testReqLectures[0],testReqLectures[2]);
        assertThat(scheduler.getSortedLecturesForDay(testDates[1], createMap()))
                .containsExactly(testReqLectures[3]);
    }

    @Test
    void testCreateCandidateSelector() {
        List<String> courseParticipants = Arrays.asList("abobe", "mbjdegoede", "cparlar");
        HashMap<String, LocalDate> allParticipants = createParticipants();
        LocalDate lectureDate = LocalDate.of(2020, 12, 18);

        OnCampusCandidate[] verification = {
                new OnCampusCandidate("mbjdegoede", LocalDate.of(2020, 12, 18)),
                new OnCampusCandidate("cparlar", LocalDate.of(2020, 12, 24)),
                new OnCampusCandidate("abobe", LocalDate.of(2020, 12, 26)) };

        PriorityQueue<OnCampusCandidate> result =
                scheduler.createCandidateSelector(lectureDate, courseParticipants, allParticipants);

        for (OnCampusCandidate onCampusCandidate : verification) {
            OnCampusCandidate candidate = result.remove();

            assertThat(candidate.getDeadline()).isEqualTo(onCampusCandidate.getDeadline());
        }
        assertThat(allParticipants.get("abobe")).isEqualTo(LocalDate.of(2020, 12, 26));
        assertThat(allParticipants.get("mbjdegoede")).isEqualTo(LocalDate.of(2020, 12, 18));
    }

    @Test
    void testAssignFirstLectureRoom() {
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(0,testSchedLectures[0],
                testReqLectures[0].getDurationInMinutes());
        assertThat(testSchedLectures[0].getRoom()).isEqualTo(testRooms[2]);
    }

    @Test
    void testAssignFirstLectureNoRoomLeft() {
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(roomList.size(),testSchedLectures[0],
                testReqLectures[0].getDurationInMinutes());
        assertThat(testSchedLectures[0].getRoom()).isNull();
    }

    @Test
    void testAssignSecondLectureSameRoomWithinTime() {
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(0,testSchedLectures[0],
                testReqLectures[0].getDurationInMinutes());
        scheduler.assignRoom(0,testSchedLectures[1],
                testReqLectures[1].getDurationInMinutes());
        assertThat(testSchedLectures[0].getRoom()).isEqualTo(testRooms[2]);
    }

    @Test
    void testAssignSecondLectureSameRoomNotWithinTime() {
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(0,testSchedLectures[0],540);
        scheduler.assignRoom(0,testSchedLectures[1],
                testReqLectures[1].getDurationInMinutes());
        assertThat(testSchedLectures[1].getRoom()).isEqualTo(testRooms[4]);
    }

    @Test
    void testAssignSecondLectureSameRoomJustWithinTime() {
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(0,testSchedLectures[0],185);
        scheduler.assignRoom(0,testSchedLectures[1],
                200);
        assertThat(testSchedLectures[1].getRoom()).isEqualTo(testRooms[2]);
    }

    @Test
    void testAssignSecondLectureNoRoomLeft() {
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(roomList.size()-1,
                testSchedLectures[0],540);
        scheduler.assignRoom(roomList.size()-1,
                testSchedLectures[1],200);
        assertThat(testSchedLectures[1].getRoom()).isNull();
    }

    @Test
    void testAssignSecondLectureNoRoomLeftNextDayWrap() {
        scheduler.sortRoomsByCapacity();
        int roomIndex = scheduler.assignRoom(roomList.size()-1,
                testSchedLectures[0],540);
        assertThat(roomIndex).isEqualTo(roomList.size()-1);
        int roomIndex2 = scheduler.assignRoom(roomIndex,
                testSchedLectures[1],testReqLectures[1].getDurationInMinutes());
        assertThat(testSchedLectures[1].getRoom()).isNull();
        assertThat(roomIndex2).isEqualTo(roomList.size());
    }
}
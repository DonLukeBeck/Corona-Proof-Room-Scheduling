package nl.tudelft.sem.calendar.controllers;

import nl.tudelft.sem.calendar.entities.Course;
import nl.tudelft.sem.calendar.entities.RequestedLecture;
import nl.tudelft.sem.calendar.entities.Room;
import nl.tudelft.sem.calendar.entities.ScheduledLecture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;

class LectureSchedulerTest {
 private static LectureScheduler scheduler;
 private static List<Room> roomList;
 private static List<RequestedLecture> lecturesToSchedule;
 private static LocalTime startTime;
 private static LocalTime endTime;
 private static int timeGapLengthInMinutes;

 private static Room room1;
 private static Room room2;
 private static Room room3;
 private static Room room4;
 private static Room room5;

 private static RequestedLecture lecture1;
 private static RequestedLecture lecture2;
 private static RequestedLecture lecture3;
 private static RequestedLecture lecture4;
 private static RequestedLecture lecture5;

 private static Course course1;
 private static Course course2;
 private static Course course3;

 private static Date date1;
 private static Date date2;
 private static Date date3;

    @BeforeEach
    void setup(){
        createRooms();
        createCourses();
        createDates();
        createLectures();
        timeGapLengthInMinutes = 45;
        startTime = LocalTime.of(8, 45);
        endTime = LocalTime.of(17, 45);
        scheduler = new LectureScheduler(roomList, lecturesToSchedule, startTime, endTime, timeGapLengthInMinutes);
    }

    private static void createRooms(){
        room1 = new Room(1, 10, "Class");
        room2 = new Room(2, 20, "IZ-2");
        room3 = new Room(3, 30, "W-2");
        room4 = new Room(4, 15, "IZ-4");
        room5 = new Room(5, 25, "TS-3");
        roomList = Arrays.asList(room1, room2, room3, room4, room5);
    }

    private static void createCourses(){
        course1 = new Course(Arrays.asList("abobe","mbjdegoede"));
        course2 = new Course(Arrays.asList("someNetId","someNetId2","slokn"));
        course3 = new Course(Arrays.asList("mdavid"));
    }

    private static void createDates(){
        date1 = new GregorianCalendar(2020, Calendar.FEBRUARY, 1).getTime();
        date2 = new GregorianCalendar(2020, Calendar.FEBRUARY, 13).getTime();
        date3 = new GregorianCalendar(2020, Calendar.FEBRUARY, 15).getTime();
    }

    private static void createLectures(){
        lecture1 = new RequestedLecture(course1, date1, 90);
        lecture2 = new RequestedLecture(course2, date1, 200);
        lecture3 = new RequestedLecture(course3, date1, 100);
        lecture4 = new RequestedLecture(course1, date2, 90);
        lecture5 = new RequestedLecture(course1, date3, 200);
        lecturesToSchedule = Arrays.asList(lecture1, lecture5, lecture3, lecture4, lecture2);
    }

    private static Map<Date, List<RequestedLecture>> createMap(){
        return new HashMap<>() {{
            put(date1, Arrays.asList(lecture1, lecture3, lecture2));
            put(date2, Arrays.asList(lecture4));
            put(date3, Arrays.asList(lecture5));
        }};
    }

    @Test
    void testScheduledAllLectures() {
    }

    @Test
    void testAssignStudents() {
    }


    @Test
    void testSortRoomsByCapacity() {
        scheduler.sortRoomsByCapacity();
        assertThat(scheduler.getRoomList()).containsExactly(room3,room5,room2,room4,room1);
    }

    @Test
    void testGroupLecturesByDay() {
        scheduler.groupLecturesByDay();
        Map<Date, List<RequestedLecture>> groupedByDay = createMap();
        assertThat(scheduler.groupLecturesByDay()).isEqualTo(groupedByDay);
    }

    @Test
    void testGetSortedLecturesForDay() {
        assertThat(scheduler.getSortedLecturesForDay(date1, createMap()))
                .containsExactly(lecture2,lecture1,lecture3);
    }

    @Test
    void testCreateCandidateSelector() {
    }

    @Test
    void testAssignFirstRoom() {
        ScheduledLecture scheduledLecture1 = new ScheduledLecture(course1,date1);
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(0,scheduledLecture1,lecture1.getDurationInMinutes());
        assertThat(scheduledLecture1.getRoom()).isEqualTo(room3);
    }

    @Test
    void testAssignSecondLectureSameRoomWithinTime() {
        ScheduledLecture scheduledLecture1 = new ScheduledLecture(course1,date1);
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(0,scheduledLecture1,lecture1.getDurationInMinutes());
        scheduler.assignRoom(0,scheduledLecture1,lecture2.getDurationInMinutes());
        assertThat(scheduledLecture1.getRoom()).isEqualTo(room3);
    }

    @Test
    void testAssignSecondLectureSameRoomNotWithinTime() {
        ScheduledLecture scheduledLecture1 = new ScheduledLecture(course1,date1);
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(0,scheduledLecture1,540);
        scheduler.assignRoom(0,scheduledLecture1,lecture1.getDurationInMinutes());
        assertThat(scheduledLecture1.getRoom()).isEqualTo(room5);
    }

    @Test
    void testAssignSecondLectureSameRoomJustWithinTime() {
        ScheduledLecture scheduledLecture1 = new ScheduledLecture(course1,date1);
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(0,scheduledLecture1,295);
        scheduler.assignRoom(0,scheduledLecture1,lecture1.getDurationInMinutes());
        assertThat(scheduledLecture1.getRoom()).isEqualTo(room3);
    }

    @Test
    void testNoRoomLeft() {
        ScheduledLecture scheduledLecture1 = new ScheduledLecture(course1,date1);
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(roomList.size(),scheduledLecture1,lecture1.getDurationInMinutes());
        assertThat(scheduledLecture1.getRoom()).isNull();
    }

    @Test
    void testSecondLectureNoRoomLeft() {
        ScheduledLecture scheduledLecture1 = new ScheduledLecture(course1,date1);
        ScheduledLecture scheduledLecture2 = new ScheduledLecture(course1,date1);
        scheduler.sortRoomsByCapacity();
        scheduler.assignRoom(roomList.size()-1,scheduledLecture1,540);
        scheduler.assignRoom(roomList.size()-1,scheduledLecture2,lecture1.getDurationInMinutes());
        assertThat(scheduledLecture2.getRoom()).isNull();
    }
}
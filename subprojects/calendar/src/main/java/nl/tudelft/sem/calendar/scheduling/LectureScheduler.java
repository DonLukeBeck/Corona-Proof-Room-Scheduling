package nl.tudelft.sem.calendar.scheduling;

import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.groupingBy;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import nl.tudelft.sem.calendar.entities.Attendance;
import nl.tudelft.sem.calendar.entities.Lecture;
import nl.tudelft.sem.calendar.entities.Room;
import nl.tudelft.sem.calendar.repositories.AttendanceRepository;
import nl.tudelft.sem.calendar.repositories.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;


@Service
@ComponentScan(basePackages = {"nl.tudelft.sem.identity.*"})
public class LectureScheduler {

    private transient List<Room> roomList;
    private transient LocalTime[] roomAvailability;
    private transient List<Lecture> lecturesToSchedule;
    private transient LocalTime startTime;
    private transient LocalTime endTime;
    private transient int timeGapLengthInMinutes;
    private transient Map<String, LocalDate> allParticipants;
    private transient int roomSearchIndex;

    @Autowired
    private transient LectureRepository lectureRepository;
    @Autowired
    private transient AttendanceRepository attendanceRepository;

    /**
     * Initializes the LectureScheduler with a given list of rooms, lectures to be
     * scheduled, scheduling start and ending times and the time gap that should be used wil
     * scheduling.
     *
     * @param roomList               the list of rooms that can be used to schedule lectures in
     * @param lecturesToSchedule     the list of lecture requests to process
     * @param startTime              the starting time of a day on campus
     * @param endTime                the ending time of a day on campus
     * @param timeGapLengthInMinutes the time gap in minutes that should be placed between any two
     *                               lectures
     */
    public void setFields(List<Room> roomList, List<Lecture> lecturesToSchedule,
                            LocalTime startTime, LocalTime endTime, int timeGapLengthInMinutes) {

        this.roomList = roomList;
        this.lecturesToSchedule = lecturesToSchedule;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeGapLengthInMinutes = timeGapLengthInMinutes;
        this.roomAvailability = new LocalTime[roomList.size()];
        this.allParticipants = new HashMap<>();
        this.roomSearchIndex = 0;
        Arrays.fill(roomAvailability, startTime);
    }

    /**
     * This method forms the core of the scheduler. It transforms all the requested lectures into
     * scheduled lectures.
     *
     */
    public void scheduleAllLectures() {

        Map<LocalDate, List<Lecture>> lecturesByDay = groupLecturesByDay();
        List<LocalDate> dates = new ArrayList<>(lecturesByDay.keySet());

        // We process the requests in sorted order by date
        Collections.sort(dates);
        // Sort the rooms decreasingly by capacity
        sortRoomsByCapacity();

        for (LocalDate date : dates) {

            // Reset room availability for each new day
            Arrays.fill(roomAvailability, startTime);
            roomSearchIndex = 0;

            // Schedule all lectures for this day
            List<Lecture> toScheduleThisDay = getSortedLecturesForDay(date, lecturesByDay);
            for (Lecture toBeScheduled : toScheduleThisDay) {

                int capacity = assignRoom(toBeScheduled, toBeScheduled.getDurationInMinutes());
                // solve a time conversion issue by adding an hour to the time before export
                toBeScheduled.setStartTime(toBeScheduled.getStartTime().plusHours(1));
                toBeScheduled.setEndTime(toBeScheduled.getEndTime().plusHours(1));
                // save lecture in database and update it with a version including an id
                toBeScheduled = lectureRepository.saveAndFlush(toBeScheduled);
                // then assign students to this lecture with id
                assignStudents(capacity, toBeScheduled, allParticipants);
            }
        }
    }

    /**
     * Assigns students to a scheduled lecture based on the capacity of the associated room and a
     * map storing the deadlines of all students. All students elected for the on-campus lecture
     * will get an attendance entry in the database with the physical bit set to true. The other
     * students will have it set to false.
     *
     * @param capacity the capacity of the room in which the lecture is scheduled
     * @param scheduledLecture the lecture being scheduled
     * @param allParticipants  a map with the netIds and deadlines of all students
     */
    // This isn't a DU-anomaly since `selectedStudents` is being read when the participants are
    // being stored in the database
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public void assignStudents(int capacity, Lecture scheduledLecture, Map<String,
            LocalDate> allParticipants) {

        // Use a priority queue to select students based on the closest deadline.
        PriorityQueue<OnCampusCandidate> candidateSelector =
                createCandidateSelector(scheduledLecture.getDate(),
                        scheduledLecture.getCourse().getNetIds(), allParticipants);

        List<String> selectedStudents = new ArrayList<>();

        int studentCounter = 0;
        while (!candidateSelector.isEmpty()
                && scheduledLecture.getRoomId() != null
                && studentCounter < capacity) {

            // Select students from the priority queue.
            String selected = candidateSelector.remove().getNetId();
            selectedStudents.add(selected);
            // When selected to attend a lecture on-campus, a student's
            // deadline is advanced with 14 days.
            allParticipants.put(selected, scheduledLecture.getDate().plusDays(14));
            studentCounter++;
        }

        // Save the attendances for this lecture in the database.
        for (String participant : scheduledLecture.getCourse().getNetIds()) {
            attendanceRepository.saveAndFlush(Attendance.builder()
                    .lectureId(scheduledLecture.getLectureId())
                    // Physical bit is true if students are selected, false if not.
                    .physical(selectedStudents.contains(participant))
                    .studentId(participant).build());
        }
    }

    /**
     * Sorts the list of rooms by decreasing capacity, so that the biggest lectures will be
     * scheduled in the biggest rooms.
     */
    public void sortRoomsByCapacity() {
        roomList.sort(Comparator.comparing(Room::getCapacity, reverseOrder()));
    }

    /**
     * Groups the lecture requests by the day they should be scheduled on to enable day by day
     * scheduling.
     *
     * @return a map grouping lecture requests by date
     */
    public Map<LocalDate, List<Lecture>> groupLecturesByDay() {
        return lecturesToSchedule.stream().collect(groupingBy(Lecture::getDate));
    }

    /**
     * Returns the lectures for a specific day, sorted by the size of their associated courses, so
     * that lecture from big courses can be scheduled first.
     *
     * @param date          the date for which to return the lecture requests
     * @param lecturesByDay the map storing all lectures grouped by day
     * @return a list of requested lectures, sorted by the corresponding course size
     */
    public List<Lecture> getSortedLecturesForDay(LocalDate date, Map<LocalDate,
            List<Lecture>> lecturesByDay) {
        lecturesByDay.get(date).sort(Comparator.comparing(
                l -> l.getCourse().getNetIds().size(), reverseOrder()));
        return lecturesByDay.get(date);
    }

    /**
     * Creates a priority queue that is used to select students to attend a lecture on campus based
     * on the lecture date, the course participants and the global map that keeps track of the
     * deadlines of all the students.
     *
     * @param lectureDate        the date at which the lecture takes place
     * @param courseParticipants a list of netIds representing the course participants of the course
     *                           associated to this lecture
     * @param allParticipants    a map storing all the students and their deadlines
     * @return a priority queue in which the students from the course associated with the lecture
     *
     *      are placed with increasing deadline, so that the students which haven't been
     *      to campus for the longest time are selected the first.
     */
    public PriorityQueue<OnCampusCandidate> createCandidateSelector(LocalDate lectureDate,
                        List<String> courseParticipants, Map<String, LocalDate> allParticipants) {

        PriorityQueue<OnCampusCandidate> candidates = new PriorityQueue<>(courseParticipants.size(),
                Comparator.comparing(OnCampusCandidate::getDeadline));

        for (String student : courseParticipants) {
            if (allParticipants.containsKey(student)) {
                candidates.add(new OnCampusCandidate(student, allParticipants.get(student)));
            } else {
                // New student that wasn't yet in the global map, so add it there too
                allParticipants.put(student, lectureDate);
                // The first deadline is equal to the date of the first lecture,
                // a student is associated with
                candidates.add(new OnCampusCandidate(student, lectureDate));
            }
        }
        return candidates;
    }

    /**
     * Assigns a room to a requested lecture in such a way that we start looking at biggest room
     * that hasn't been fully booked for the day. If the entire lecture first between the starting
     * and ending time, the lecture will be scheduled in this room, else we try the next room. When
     * a suitable room is found, the moment at which the room becomes availability again is updated
     * using the duration of the lecture and the time gap between any two lectures.
     *
     * @param scheduledLecture  the lecture to schedule
     * @param durationInMinutes the duration of the lecture to schedule
     * @return the capacity of the room in which the lecture is now scheduled,
     *      used to assign students to it.
     */
    public int assignRoom(Lecture scheduledLecture, int durationInMinutes) {
        while (roomSearchIndex < roomList.size()) {
            // Check if it fits in the current room at the current day
            if (durationInMinutes <= (int) Duration.between(
                    roomAvailability[roomSearchIndex], endTime).toMinutes()) {

                // Schedule the lecture in the currently selected room
                // update its start and ending time, and the availability of the room.
                scheduledLecture.setRoomId(roomList.get(roomSearchIndex).getRoomId());
                scheduledLecture.setStartTime(roomAvailability[roomSearchIndex]);
                scheduledLecture.setEndTime(
                        roomAvailability[roomSearchIndex].plusMinutes(durationInMinutes));

                roomAvailability[roomSearchIndex] = roomAvailability[roomSearchIndex]
                        .plusMinutes(durationInMinutes + timeGapLengthInMinutes);

                return roomList.get(roomSearchIndex).getCapacity();
            } else {
                // Move to the next room
                roomSearchIndex++;
            }
        }
        // No suitable room could be found
        return 0;
    }

    /**
     * Returns the list of rooms used for scheduling.
     *
     * @return the list of rooms used for scheduling
     */
    public List<Room> getRoomList() {
        return roomList;
    }


    /**
     * Sets the room search index for scheduling, used for testing purposes.
     *
     * @param roomSearchIndex the index of where the scheduling algorithm should begin scheduling
     */
    public void setRoomSearchIndex(int roomSearchIndex) {
        this.roomSearchIndex = roomSearchIndex;
    }

    /**
     * Returns the room search index for scheduling, used for testing purposes.
     *
     * @return the most recent index of where the scheduling algorithm tries to schedule
     */
    public int getRoomSearchIndex() {
        return this.roomSearchIndex;
    }

    /**
     * Returns the list of lectures to be scheduled, used for testing purposes.
     *
     * @return the list of lectures to be schedule
     */
    public List<Lecture> getLecturesList() {
        return this.lecturesToSchedule;
    }

    /**
     * Returns the time to start the scheduling at, used for testing purposes.
     *
     * @return the time to start the scheduling at
     */
    public LocalTime getStartTime() {
        return this.startTime;
    }

    /**
     * Returns the time to end the scheduling at, used for testing purposes.
     *
     * @return the time to end the scheduling at
     */
    public LocalTime getEndTime() {
        return this.endTime;
    }

    /**
     * Returns the time gap in minutes between any two lectures, used for testing purposes.
     *
     * @return time gap in minutes between any two lectures
     */
    public int getTimeGapLengthInMinutes() {
        return this.timeGapLengthInMinutes;
    }

    /**
     * Returns the array with availability times for each room, used for testing purposes.
     *
     * @return the array with availability times for each room
     */
    public LocalTime[] getRoomAvailability() {
        return this.roomAvailability;
    }

    /**
     * Returns the map with course participants and their deadlines, used for testing purposes.
     *
     * @return the map with course participants and their deadlines
     */
    public Map<String, LocalDate> getAllParticipants() {
        return allParticipants;
    }
}
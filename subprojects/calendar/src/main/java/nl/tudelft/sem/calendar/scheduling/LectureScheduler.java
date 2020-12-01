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
import nl.tudelft.sem.calendar.entities.Room;


public class LectureScheduler {
    private transient List<Room> roomList;
    private transient LocalTime[] roomAvailability;
    private transient List<RequestedLecture> lecturesToSchedule;
    private transient LocalTime startTime;
    private transient LocalTime endTime;
    private transient int timeGapLengthInMinutes;
    private transient Map<String, LocalDate> allParticipants;
    private transient int roomSearchIndex;

    /**
     * Creates a new instance of the LectureScheduler with a given list of rooms, lectures to be
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
    public LectureScheduler(List<Room> roomList, List<RequestedLecture> lecturesToSchedule,
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
     * @return a list of scheduled lectures
     */
    public List<ScheduledLecture> scheduleAllLectures() {
        List<ScheduledLecture> scheduledLectures = new ArrayList<>();
        if (lecturesToSchedule == null || lecturesToSchedule.size() == 0) {
            return scheduledLectures;
        }

        Map<LocalDate, List<RequestedLecture>> lecturesByDay = groupLecturesByDay();
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
            List<RequestedLecture> toScheduleThisDay = getSortedLecturesForDay(date, lecturesByDay);
            for (RequestedLecture toBeScheduled : toScheduleThisDay) {
                ScheduledLecture scheduledLecture =
                        new ScheduledLecture(toBeScheduled.getCourse(), toBeScheduled.getDate());
                assignRoom(scheduledLecture, toBeScheduled.getDurationInMinutes());
                assignStudents(scheduledLecture, allParticipants);
                scheduledLectures.add(scheduledLecture);
            }
        }
        return scheduledLectures;
    }

    /**
     * Assigns students to a scheduled lecture based on the capacity of the associated room and a
     * map storing the deadlines of all students.
     *
     * @param scheduledLecture the lecture being scheduled
     * @param allParticipants  a map with the netIds and deadlines of all students
     */
    public void assignStudents(ScheduledLecture scheduledLecture, Map<String,
            LocalDate> allParticipants) {
        PriorityQueue<OnCampusCandidate> candidateSelector =
                createCandidateSelector(scheduledLecture.getDate(),
                        scheduledLecture.getCourse().getNetIds(), allParticipants);

        int studentCounter = 0;
        while (!candidateSelector.isEmpty()
                && scheduledLecture.getRoom() != null
                && studentCounter < scheduledLecture.getRoom().getCapacity()) {
            String selected = candidateSelector.remove().getNetId();
            scheduledLecture.addStudentOnCampus(selected);
            allParticipants.put(selected, scheduledLecture.getDate().plusDays(14));
            studentCounter++;
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
    public Map<LocalDate, List<RequestedLecture>> groupLecturesByDay() {
        return lecturesToSchedule.stream().collect(groupingBy(RequestedLecture::getDate));
    }

    /**
     * Returns the lectures for a specific day, sorted by the size of their associated courses, so
     * that lecture from big courses can be scheduled first.
     *
     * @param date          the date for which to return the lecture requests
     * @param lecturesByDay the map storing all lectures grouped by day
     * @return a list of requested lectures, sorted by the corresponding course size
     */
    public List<RequestedLecture> getSortedLecturesForDay(LocalDate date, Map<LocalDate,
            List<RequestedLecture>> lecturesByDay) {
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
        if (courseParticipants == null) {
            return new PriorityQueue<>();
        }
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
     */
    public void assignRoom(ScheduledLecture scheduledLecture,
                          int durationInMinutes) {
        while (roomSearchIndex < roomList.size()) {
            if (durationInMinutes <= (int) Duration.between(
                    roomAvailability[roomSearchIndex], endTime).toMinutes()) {
                scheduledLecture.setRoom(roomList.get(roomSearchIndex));
                scheduledLecture.setStartTime(roomAvailability[roomSearchIndex]);
                scheduledLecture.setEndTime(
                        roomAvailability[roomSearchIndex].plusMinutes(durationInMinutes));
                roomAvailability[roomSearchIndex] = roomAvailability[roomSearchIndex]
                        .plusMinutes(durationInMinutes + timeGapLengthInMinutes);
                break;
            } else {
                roomSearchIndex++;
            }
        }
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
}
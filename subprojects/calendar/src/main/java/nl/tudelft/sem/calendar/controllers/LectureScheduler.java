package nl.tudelft.sem.calendar.controllers;

import nl.tudelft.sem.calendar.entities.OnCampusCandidate;
import nl.tudelft.sem.calendar.entities.RequestedLecture;
import nl.tudelft.sem.calendar.entities.Room;
import nl.tudelft.sem.calendar.entities.ScheduledLecture;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.groupingBy;

public class LectureScheduler {

    private List<Room> roomList;
    private LocalTime[] roomAvailability;
    private List<RequestedLecture> lecturesToSchedule;
    private LocalTime startTime;
    private LocalTime endTime;
    private int timeGapLengthInMinutes;

    public LectureScheduler(List<Room> roomList, List<RequestedLecture> lecturesToSchedule,
                            LocalTime startTime, LocalTime endTime, int timeGapLengthInMinutes) {

        this.roomList = roomList;
        this.lecturesToSchedule = lecturesToSchedule;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeGapLengthInMinutes = timeGapLengthInMinutes;
        this.roomAvailability = new LocalTime[roomList.size()];
        Arrays.fill(roomAvailability, startTime);
    }

    public List<ScheduledLecture> scheduleAllLectures() {
        List<ScheduledLecture> scheduledLectures = new ArrayList<>();
        if(lecturesToSchedule == null || lecturesToSchedule.size() == 0) return scheduledLectures;

        Map<String, LocalDate> allParticipants = new HashMap<>();
        Map<LocalDate, List<RequestedLecture>> lecturesByDay = groupLecturesByDay();
        List<LocalDate> dates = new ArrayList<>(lecturesByDay.keySet());

        Collections.sort(dates);
        sortRoomsByCapacity();

        for (LocalDate date: dates) {
            // Reset room availability
            Arrays.fill(roomAvailability, startTime);
            int roomIndex = 0;

            List<RequestedLecture> toScheduleThisDay = getSortedLecturesForDay(date, lecturesByDay);
            for (RequestedLecture toBeScheduled : toScheduleThisDay) {
                ScheduledLecture scheduledLecture = new ScheduledLecture(toBeScheduled.getCourse(), toBeScheduled.getDate());
                roomIndex = assignRoom(roomIndex, scheduledLecture, toBeScheduled.getDurationInMinutes());
                assignStudents(scheduledLecture, allParticipants);
                scheduledLectures.add(scheduledLecture);
            }
        }
        return scheduledLectures;
    }

    public void assignStudents(ScheduledLecture scheduledLecture, Map<String, LocalDate> allParticipants) {
        PriorityQueue<OnCampusCandidate> candidateSelector = createCandidateSelector(scheduledLecture.getDate(), scheduledLecture.getCourse().getParticipants(),allParticipants);

        //Updated students in the map should be tested for!
        int studentCounter = 0;
        while(!candidateSelector.isEmpty()
                && scheduledLecture.getRoom() != null
                && studentCounter < scheduledLecture.getRoom().getCapacity()) {
            String selected = candidateSelector.remove().getNetId();
            scheduledLecture.addStudentOnCampus(selected);
            allParticipants.put(selected, scheduledLecture.getDate().plusDays(14));
            studentCounter++;
        }
    }

    public void sortRoomsByCapacity(){
        roomList.sort(Comparator.comparing(Room::getCapacity, reverseOrder()));
    }

    public Map<LocalDate, List<RequestedLecture>> groupLecturesByDay() {
        return lecturesToSchedule.stream().collect(groupingBy(RequestedLecture::getDate));
    }

    public List<RequestedLecture> getSortedLecturesForDay(LocalDate date, Map<LocalDate, List<RequestedLecture>> lecturesByDay) {
        lecturesByDay.get(date).sort(Comparator.comparing(l -> l.getCourse().getParticipants().size(), reverseOrder()));
        return lecturesByDay.get(date);
    }

    public PriorityQueue<OnCampusCandidate> createCandidateSelector(LocalDate lectureDate, List<String> courseParticipants, Map<String, LocalDate> allParticipants){
        if(courseParticipants == null)
            return new PriorityQueue<>();

        PriorityQueue<OnCampusCandidate> candidates = new PriorityQueue<>(courseParticipants.size(),
                Comparator.comparing(OnCampusCandidate::getDeadline));

        for (String student : courseParticipants) {
            if (allParticipants.containsKey(student)) {
                candidates.add(new OnCampusCandidate(student, allParticipants.get(student)));
            } else {
                allParticipants.put(student, lectureDate);
                candidates.add(new OnCampusCandidate(student, lectureDate));
            }
        }
        return candidates;
    }

    public int assignRoom(int roomSearchIndex, ScheduledLecture scheduledLecture, int durationInMinutes) {
        while(roomSearchIndex < roomList.size()) {
            if(durationInMinutes <= (int) Duration.between(roomAvailability[roomSearchIndex], endTime).toMinutes()) {
                scheduledLecture.setRoom(roomList.get(roomSearchIndex));
                scheduledLecture.setStartTime(roomAvailability[roomSearchIndex]);
                scheduledLecture.setEndTime(roomAvailability[roomSearchIndex].plusMinutes(durationInMinutes));
                roomAvailability[roomSearchIndex] = roomAvailability[roomSearchIndex].plusMinutes(durationInMinutes + timeGapLengthInMinutes);
                break;
            } else roomSearchIndex++;
        }
        return roomSearchIndex;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public LocalTime[] getRoomAvailability() {
        return roomAvailability;
    }

    public void setRoomAvailability(LocalTime[] roomAvailability) {
        this.roomAvailability = roomAvailability;
    }

    public List<RequestedLecture> getLecturesToSchedule() {
        return lecturesToSchedule;
    }

    public void setLecturesToSchedule(List<RequestedLecture> lecturesToSchedule) {
        this.lecturesToSchedule = lecturesToSchedule;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getTimeGapLengthInMinutes() {
        return timeGapLengthInMinutes;
    }

    public void setTimeGapLengthInMinutes(int timeGapLengthInMinutes) {
        this.timeGapLengthInMinutes = timeGapLengthInMinutes;
    }
}
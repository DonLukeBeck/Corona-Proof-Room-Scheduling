package nl.tudelft.sem.calendar.controllers;

import nl.tudelft.sem.calendar.entities.OnCampusCandidate;
import nl.tudelft.sem.calendar.entities.RequestedLecture;
import nl.tudelft.sem.calendar.entities.Room;
import nl.tudelft.sem.calendar.entities.ScheduledLecture;


import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
        Map<String, Integer> allParticipants = new HashMap<>();
        Map<Date, List<RequestedLecture>> lecturesByDay = groupLecturesByDay();
        List<Date> dates = (List) lecturesByDay.keySet();

        Collections.sort(dates);
        sortRoomsByCapacity();

        Date startDate = dates.get(0);
        int roomIndex = 0;
        for (Date date: dates) {
            // Reset room availability
            Arrays.fill(roomAvailability, startTime);

            // Check if two weeks have passed since the first request
            long dayDiff = TimeUnit.DAYS.convert(Math.abs(date.getTime() - startDate.getTime()), TimeUnit.MILLISECONDS);
            if(dayDiff >= 14) {
                allParticipants = new HashMap<>();
                startDate = date;
            }

            List<RequestedLecture> toScheduleThisDay = getSortedLecturesForDay(date, lecturesByDay);
            for (RequestedLecture toBeScheduled : toScheduleThisDay) {
                ScheduledLecture scheduledLecture = new ScheduledLecture(toBeScheduled.getCourse(), toBeScheduled.getDate());
                assignRoom(roomIndex, scheduledLecture, toBeScheduled.getDurationInMinutes());
                roomIndex++;
                assignStudents(scheduledLecture, allParticipants);
            }
        }
        return scheduledLectures;
    }

    public void assignStudents(ScheduledLecture scheduledLecture, Map<String, Integer> allParticipants) {

        List<String> assignedStudents = new ArrayList<>();
        PriorityQueue<OnCampusCandidate> candidateSelector = createCandidateSelector(scheduledLecture.getCourse().getParticipants(),allParticipants);

        int studentCounter = 0;
        while(!candidateSelector.isEmpty() && scheduledLecture.getRoom().getCapacity() < studentCounter) {
            assignedStudents.add(candidateSelector.remove().getNetId());
        }
        scheduledLecture.addStudentsOnCampus(assignedStudents);
    }

    public void sortRoomsByCapacity(){
        roomList.sort(Comparator.comparing(Room::getCapacity, reverseOrder()));
    }

    public Map<Date, List<RequestedLecture>> groupLecturesByDay() {
        return lecturesToSchedule.stream().collect(groupingBy(RequestedLecture::getDate));
    }

    public List<RequestedLecture> getSortedLecturesForDay(Date date, Map<Date, List<RequestedLecture>> lecturesByDay) {
        lecturesByDay.get(date).sort(Comparator.comparing(l -> l.getCourse().getParticipants().size(), reverseOrder()));
        return lecturesByDay.get(date);
    }

    public PriorityQueue<OnCampusCandidate> createCandidateSelector(List<String> courseParticipants, Map<String, Integer> allParticipants){
        if(courseParticipants == null)
            return new PriorityQueue<>();

        PriorityQueue<OnCampusCandidate> candidates = new PriorityQueue<>(courseParticipants.size(),
                Comparator.comparing(OnCampusCandidate::getNumParticipations));

        for (String student : courseParticipants) {
            if (allParticipants.containsKey(student)) {
                candidates.add(new OnCampusCandidate(student, allParticipants.get(student)));
            } else {
                allParticipants.put(student, 0);
                candidates.add(new OnCampusCandidate(student, 0));
            }
        }
        return candidates;
    }

    public void assignRoom(int roomSearchIndex, ScheduledLecture scheduledLecture, int durationInMinutes) {
        while(roomSearchIndex < roomList.size()) {
            if(roomAvailability[roomSearchIndex].plusMinutes(durationInMinutes).isBefore(endTime) || roomAvailability[roomSearchIndex].plusMinutes(durationInMinutes).equals(endTime)) {
                scheduledLecture.setRoom(roomList.get(roomSearchIndex));
                scheduledLecture.setStartTime(roomAvailability[roomSearchIndex]);
                scheduledLecture.setEndTime(roomAvailability[roomSearchIndex].plusMinutes(durationInMinutes));
                roomAvailability[roomSearchIndex] = roomAvailability[roomSearchIndex].plusMinutes(durationInMinutes + timeGapLengthInMinutes);
                break;
            } else roomSearchIndex++;
        }
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
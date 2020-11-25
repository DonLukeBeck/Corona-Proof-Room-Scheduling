package nl.tudelft.sem.lecture_scheduling.controllers;

import nl.tudelft.sem.lecture_scheduling.entities.OnCampusCandidate;
import nl.tudelft.sem.lecture_scheduling.entities.RequestedLecture;
import nl.tudelft.sem.lecture_scheduling.entities.Room;
import nl.tudelft.sem.lecture_scheduling.entities.ScheduledLecture;

import java.time.LocalTime;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class LectureScheduler {

    private List<Room> roomList;
    private List<RequestedLecture> lecturesToSchedule;
    private LocalTime startTime;
    private LocalTime endTime;
    private int timeGapLengthInMinutes;
    private double percentageOfCourseParticipantsToAimFor;

    public LectureScheduler(List<Room> roomList, List<RequestedLecture> lecturesToSchedule,
                            LocalTime startTime, LocalTime endTime, int timeGapLengthInMinutes,
                            double percentageOfCourseParticipantsToAimFor) {

        this.roomList = roomList;
        this.lecturesToSchedule = lecturesToSchedule;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeGapLengthInMinutes = timeGapLengthInMinutes;
        this.percentageOfCourseParticipantsToAimFor = percentageOfCourseParticipantsToAimFor;
    }

    public List<ScheduledLecture> scheduledAllLectures() {
        List<ScheduledLecture> scheduledLectures = new ArrayList<>();
        // Algorithm variables
        Map<String, Integer> allParticipants = new HashMap<>(); // Global hash map to keep track of participation in any lecture within 2 weeks
        double assumedCapacity = 0.75; // the percentage of the number of course participants should fit in a room to consider

        Map<Date, List<RequestedLecture>> lecturesByDay = lecturesToSchedule.stream().collect(groupingBy(RequestedLecture::getDate));
        if (roomList == null) throw new NullPointerException("No roomList available");
        else roomList.sort(Comparator.comparing(Room::getCapacity));

        int roomIndex = 0;
        for (Map.Entry<Date, List<RequestedLecture>> schedulingDay : lecturesByDay.entrySet()) {
            schedulingDay.getValue().sort(Comparator.comparing(lecture -> lecture.getCourse().getParticipants().size()));

            LocalTime[] roomAvailability = new LocalTime[roomList.size()];
            Arrays.fill(roomAvailability, startTime);

            for (RequestedLecture toBeScheduled : schedulingDay.getValue()) {
                List<String> courseParticipants = toBeScheduled.getCourse().getParticipants();
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
                while (roomIndex < roomList.size()) {
                    Room candidateRoom = roomList.get(roomIndex);
                    if (roomAvailability[roomIndex].plusMinutes(toBeScheduled.getDurationInMinutes()).isBefore(endTime)) {
                        // schedule lecture in this room
                        roomAvailability[roomIndex] = roomAvailability[roomIndex].plusMinutes(toBeScheduled.getDurationInMinutes() + timeGapLengthInMinutes);
                        break;
                    } else {
                        roomIndex++;
                    }
                }
            }
        }
        return null;
    }
}
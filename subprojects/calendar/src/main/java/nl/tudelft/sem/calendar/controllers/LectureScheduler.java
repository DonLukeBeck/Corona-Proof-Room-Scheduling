package nl.tudelft.sem.calendar.controllers;

import nl.tudelft.sem.calendar.entities.OnCampusCandidate;
import nl.tudelft.sem.calendar.entities.RequestedLecture;
import nl.tudelft.sem.calendar.entities.Room;
import nl.tudelft.sem.calendar.entities.ScheduledLecture;

import java.time.LocalTime;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class LectureScheduler {

    private List<Room> roomList;
    LocalTime[] roomAvailability = new LocalTime[roomList.size()];
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

    public boolean scheduleInRoom(int roomSearchIndex, Date lectureDate, int numberOfStudents, int durationInMinutes) {
        boolean scheduled = false;
        while(roomSearchIndex < roomList.size() && !scheduled) {
            if(roomList.get(roomSearchIndex).getCapacity() >= numberOfStudents * percentageOfCourseParticipantsToAimFor){
                if(roomAvailability[roomSearchIndex].plusMinutes(durationInMinutes).isBefore(endTime) || roomAvailability[roomSearchIndex].plusMinutes(durationInMinutes).equals(endTime)) {
                    roomAvailability[roomSearchIndex] = roomAvailability[roomSearchIndex].plusMinutes(durationInMinutes + timeGapLengthInMinutes);
                    scheduled = true;
                } else roomSearchIndex++;
            }
            else roomSearchIndex++;
        }
        if(scheduled)
            return true;
        if(percentageOfCourseParticipantsToAimFor == 0.5)
            return false;
        else
           return scheduleInRoom(roomSearchIndex, lectureDate, numberOfStudents, durationInMinutes);
    }

    public List<ScheduledLecture> scheduledAllLectures() {
        List<ScheduledLecture> scheduledLectures = new ArrayList<>();

        // Map with to be scheduled lectures grouped by date
        Map<Date, List<RequestedLecture>> lecturesByDay =
                lecturesToSchedule.stream().collect(groupingBy(RequestedLecture::getDate));

        // Hash map to keep track of participation in any lecture within 2 week
        Map<String, Integer> allParticipants = new HashMap<>();

        // Key set from the map
        List<Date> dates = (List) lecturesByDay.keySet();
        Collections.sort(dates);
        Date startDate = dates.get(0);

        int roomIndex = 0;
        for (Date date: dates) {

            // The list of lectures to schedule for this date, sorted by course size.
            List<RequestedLecture> toScheduleThisDay = lecturesByDay.get(date);
            toScheduleThisDay.sort(Comparator.comparing(lecture -> lecture.getCourse()
                    .getParticipants().size()));

            // Reset room availability
            Arrays.fill(roomAvailability, startTime);

            for (RequestedLecture toBeScheduled : toScheduleThisDay) {
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
                percentageOfCourseParticipantsToAimFor = 1;
                scheduleInRoom(roomIndex, toBeScheduled.getDate(), courseParticipants.size(), toBeScheduled.getDurationInMinutes());
            }
        }
        return null;
    }
}
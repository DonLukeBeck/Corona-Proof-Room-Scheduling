# API Endpoint Definitions

## Identity Service
String createToken(String netId, String password)  

## Course Management Service
* createNewCourse(int courseId, String name, int teacherId, List<User> participants)
* deleteCourse(int courseId)
* planNewLecture(int courseId, int durationInMinutes, Date date)
* cancelLecture(int courseId, Date date)
* Course getCourse(int courseId)
* List<String> getCourseParticipants(int courseId) 

## Restriction Management Service
* List<Room> getAllRoomsWithAdjustedCapacity()
* setMinSeatsBig(int numberOfSeats)
* setCapacityRestriction(bool bigOrSmallRoom, float maxPercentageAllowed)
* setTimeGapLength(int gapTimeInMinutes)
* int getTimeGapLength()
* setStartTime(LocalTime startTime)
* setEndTime(LocalTime endTime)
* int getStartTime()
* int getEndTime()

## Room Management Service
* List<Room> getAllRooms()
* Room getRoom(int roomId)

## Calendar Scheduling Service
* List<nl.tudelft.sem.shared.entity.Lecture> schedulePlannedLectures(List<LectureRequest> toBeScheduledLectures)
* List<nl.tudelft.sem.shared.entity.Lecture> getMyPersonalSchedule(int userId)
* List<nl.tudelft.sem.shared.entity.Lecture> getMyPersonalScheduleForDay(int userId, Date date)
* List<nl.tudelft.sem.shared.entity.Lecture> getMyPersonalScheduleForCourse(int userId, int courseId)
* indicateAbsence(int userId, courseId, Date date)
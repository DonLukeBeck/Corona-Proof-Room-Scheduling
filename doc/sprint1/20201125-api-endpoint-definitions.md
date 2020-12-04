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
* List<Lecture> schedulePlannedLectures(List<LectureRequest> toBeScheduledLectures)
* List<Lecture> getMyPersonalSchedule(int userId)
* List<Lecture> getMyPersonalScheduleForDay(int userId, Date date)
* List<Lecture> getMyPersonalScheduleForCourse(int userId, int courseId)
* indicateAbsence(int userId, courseId, Date date)
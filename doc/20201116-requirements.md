# Requirements

### Functional 

#### Must have
- The user shall be able to log in using NetID and password (encrypted)
- The user shall be able to log in as either a student or a teacher
- A teacher shall be associated with the courses they teach
- A teacher shall be able to request a lecture for course x of duration y in hours to be scheduled on day z
- Each of the requested lectures shall be scheduled in a particular lecture room and for a particular time slot with a teacher to teach it
- Once a time slot has been taken, overlapping lectures shall be scheduled in another lecture room
- For each lecture, a subset of the students should be selected to attend it live on campus, such that each student is selected for at least one lecture every 2 weeks
- A teacher shall be able to see their own schedule for the upcoming days, that is, for each upcoming lecture, the timeslot and allocated room should be displayed
- A student shall be able to see their own schedule for the upcoming days, that is, 
  for each upcoming lecture, the timeslot and allocated room should be displayed, next to an indication of whether the student is selected to attend the lecture in person or not
- The system shall assign a 45 minute gap between each lecture in a lecture hall
- The system shall assign a percentage of seats in the lecture hall depending on the size of said lecture hall. (initially 20% for rooms < 200 seats and 30% for rooms ≥ 200 seats)

#### Should have
- A student shall be able to attend at least one lecture in person per n weeks, this number is subject to change, but is initially 2
- A student shall be able to indicate when they will refrain from attending the class in person, so that the exact number of students that will attend the physical lecture is known to the university and the responsible lecturer
- A teacher shall be able to create a new course and assign it to themselves
- A student shall be able to be assigned to a course to see its schedule

#### Could have
- A teacher shall be able to indicate to the system that the physical class is cancelled due to symptoms of the coronavirus, so that the system can notify the selected students and reschedule if needed
- A teacher shall be able to change the interval in which lectures can be scheduled every day

#### Won’t have
- A teacher shall be able to re-allocate the slots of students that indicated that they won’t be attending the online session they’re selected for
- A student shall be able to indicate a preferred lecture to attend physically
- A student shall be able to indicate that they are in quarantine as to not be scheduled for in person classes

### Non-Functional 
- The system shall be runnable on Windows, Linux, and MAC
- The system shall use a REST api for interfacing (with other applications)
- Individual components of the system shall to be organized following the microservices paradigm
- The system shall be implemented in Java (version 14)(?)
- The system shall be built using the Spring Boot framework and Gradle
- The system shall use Spring Security to handle security
- The system shall have at least 85% meaningful branch test coverage
- The system shall be able to cope with at least 300 and at most 500-600 students per course
- All functional requirements shall be delivered in the midterm week of the second quarter of academic year 2020-2021



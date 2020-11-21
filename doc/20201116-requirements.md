# Requirements

### Functional 

#### Must have
- A user, which has a name and is either a student or a teacher, shall be able to log in using NetID and password
- A teacher shall be able to request a lecture for course x of duration y in minutes to be scheduled on day z
- A lecture schedule shall be created once every quarter on the friday before the quarter starts. All lecture scheduling requests must be received before then
- Each of the requested lectures shall be scheduled in a particular lecture room and for a particular time slot with a teacher to teach it
- When the schedule is being made, rooms with the highest capacity shall be assigned to the courses with the most attending students, and smaller rooms to smaller lectures
- A teacher shall be able to retrieve their own schedule once the lectures have been scheduled, that is, for each upcoming lecture, the timeslot and allocated room should be given
- A student shall be able to retrieve their own schedule once the schedule has been created, that is, for each upcoming lecture, the timeslot and allocated room should be given, next to an indication of whether the student is selected to attend the lecture in person or not

#### Should have
- A student shall be able to attend at least one lecture in person per n weeks, this number is subject to change, but is initially 2
- The system shall assign a percentage of seats in the lecture hall depending on the size of said lecture hall. (initially 20% for rooms < 200 seats and 30% for rooms ≥ 200 seats)
- A student shall be able to indicate when they will refrain from attending the class in person, so that the exact number of students that will attend the physical lecture is known to the university and the responsible lecturer
- A teacher shall be able to create a new course, which has a name, course code and a list of participating students and will be assigned to the teacher that creates it. 
- The system shall assign a 45 minute gap between each lecture in a lecture hall

#### Could have
- A user can see on which days they have which lectures before time slots are assigned
- A teacher shall be able to indicate to the system that the physical class is cancelled due to symptoms of the coronavirus, so that the system can notify the selected students and reschedule if needed
- A course shall have a single zoom link for the students who will be attending online
- A teacher shall be able to set times between which they would prefer their lecture to be scheduled


#### Won’t have
- A teacher shall be able to re-allocate the slots of students that indicated that they won’t be attending the online session they’re selected for
- A student shall be able to indicate a preferred lecture to attend physically
- A student shall be able to indicate that they are in quarantine as to not be scheduled for in person classes
- The system shall schedule the lectures to minimize the amount of people with overlapping lectures
- A list of students to attend a course shall be able to be imported from other services such as Osiris or Brightspace


### Non-Functional 
- The system shall be runnable on Windows, Linux, and MAC
- The system shall use a REST api for interfacing (with other applications)
- Individual components of the system shall to be organized following the microservices paradigm
- The system shall use MySQL databases for the data storage backend
- The system shall be implemented in Java (version 14)
- The system shall be built using the Spring Boot framework and Gradle
- The system shall use Spring Security to handle security
- The system shall have at least 85% meaningful branch test coverage
- The system shall be able to cope with at least 300 and at most 500-600 students per course
- The system shall assume that the requirements given to the system are applicable
- All functional requirements shall be delivered in the midterm week of the second quarter of academic year 2020-2021


# User Stories
- As a user, I should be able to log into the system using NetID  and password
- As a user, I should be able to see my personal lecture schedule for the upcoming days so that I can plan my week effectively and know when I should be where
- As a teacher, I should be able to create a course and assign students to it so that I can teach it 
- As a teacher,  I should be able to request a lecture of a specific duration for one of my courses to be scheduled on a specific day
- As a student, I should get the opportunity to attend physical lectures once every 2 weeks to maintain a connection with campus life
- As a student, if I am assigned to a physical lecture, I should be able to specify if I will attend the lecture, in order to minimize the spread of the coronavirus

| Permissions                                  | Teacher  | Student  |
| -------------------------------------------- |:--------:| --------:|
| Log-in                                       | ✔        | ✔        |
| Create courses                               | ✔        |          |
| Create lecture requests                      | ✔        |          |
| Assign students to courses                   | ✔        |          |
| See schedule                                 | ✔        | ✔        |
| Refrain from attending the lecture in person | (✔)      | ✔        |


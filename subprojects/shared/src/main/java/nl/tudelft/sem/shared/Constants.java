package nl.tudelft.sem.shared;

import nl.tudelft.sem.shared.entity.StringMessage;

public class Constants {
    public static final String COURSE_SERVER_URL = "http://localhost:8585";
    public static final String RESTRICTION_SERVER_URL = "http://localhost:8084";
    public static final String IDENTITY_SERVER_URL = "http://localhost:8083";
    public static final String ROOMS_SERVER_URL = "http://localhost:8085";
    public static final String CALENDAR_SERVER_URL = "http://localhost:8081";
    public static final String teacherRole = "teacher";
    public static final String studentRole = "student";
    public static final StringMessage noAccessMessage =
            new StringMessage("You are not allowed to view this page."
                    + " Please contact administrator.");
}

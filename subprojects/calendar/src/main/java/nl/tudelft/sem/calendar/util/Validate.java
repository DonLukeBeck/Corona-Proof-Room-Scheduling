package nl.tudelft.sem.calendar.util;

import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class Validate {

    private static String noAccessMessage =
            "You are not allowed to view this page. Please contact administrator.";

    /**
     * Helper method to validate the role of a user.
     *
     * @param request the request containing jwt token information
     * @param role the desired role
     *
     * @return an error message if the user hasn't got the desired role, else its netId.
     */
    public String validateRole(HttpServletRequest request, String role) {
        try {
            JSONObject jwtInfo = JwtValidate.jwtValidate(request);
            if (!jwtInfo.getString("role").equals(role)) {
                return noAccessMessage;
            }
            return jwtInfo.getString("netid");
        } catch (Exception e) {
            return noAccessMessage;
        }
    }
}

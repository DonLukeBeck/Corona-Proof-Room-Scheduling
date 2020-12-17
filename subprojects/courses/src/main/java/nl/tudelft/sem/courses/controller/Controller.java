package nl.tudelft.sem.courses.controller;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import nl.tudelft.sem.courses.util.JwtValidate;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Controller {

    protected transient String noAccessMessage =
            "You are not allowed to view this page. Please contact administrator.";

    /**
     * Helper method to validate the role of a user.
     *
     * @param request the request containing jwt token information
     * @param role the desired role
     *
     * @return an error message if the user hasn't got the desired role, else its netId.
     */
    public String validateRole(HttpServletRequest request, String role)
            throws JSONException, IOException, InterruptedException {

        JSONObject jwtInfo = JwtValidate.jwtValidate(request);
        try {
            if (!jwtInfo.getString("role").equals(role)) {
                return noAccessMessage;
            }
        } catch (Exception e) {
            return noAccessMessage;
        }
        return jwtInfo.getString("netid");
    }
}

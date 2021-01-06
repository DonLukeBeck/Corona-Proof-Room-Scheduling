package nl.tudelft.sem.identity.design.pattern;

import io.jsonwebtoken.Claims;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import nl.tudelft.sem.identity.entity.TokenRole;

/**
 * Concrete handler that contains actual logic to verify the role.
 */
public class RoleValidator extends BaseValidator {

    /**
     * Method to verify if the token contains the specified role.
     */
    private boolean claimsContainsRole(Claims claims, String role) {
        ArrayList<LinkedHashMap<String, Object>> scope =
                claims.get("scope", ArrayList.class);
        for (var entry : scope) {
            if (entry.containsValue(role)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handle(TokenRole tokenRole) throws Exception {
        if (!this.claimsContainsRole(claims(tokenRole.getToken()), tokenRole.getRole())) {
            return false;
        }
        return super.checkNext(tokenRole);
    }
}

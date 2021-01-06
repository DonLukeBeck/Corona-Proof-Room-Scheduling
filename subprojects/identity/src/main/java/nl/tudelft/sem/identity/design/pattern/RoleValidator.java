package nl.tudelft.sem.identity.design.pattern;

import io.jsonwebtoken.Claims;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import nl.tudelft.sem.identity.entity.TokenRole;

public class RoleValidator extends BaseValidator {

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
        try {
            this.claimsContainsRole(claims(tokenRole.getToken()), tokenRole.getRole());
        } catch (Exception e) {
            return false;
        }
        return super.checkNext(tokenRole);
    }
}

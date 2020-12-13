package nl.tudelft.sem.identity.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class JwtValidate {

    private final String secret = "semcoronaproofroomreservation";

    private JwtParser parser = Jwts.parser().setSigningKey(secret);

    /**
     * Check if the string is a valid jwt
     *
     * @param jwt the compacted version of the jwt
     * @return true if the jwt is valid, otherwise false
     */
    public boolean isValid(String jwt) {
        try {
            parser.parseClaimsJws(jwt).getBody();
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Get the claims from the compact jws, a cryptographically signed jwt
     *
     * @param jwt the compacted version of the jwt
     * @return the set of claims or {@code null} if not valid
     */
    public Claims claims(String jwt) {
        return isValid(jwt) ? parser.parseClaimsJws(jwt).getBody() : null;
    }

    /**
     * Get the username from the jwt
     *
     * @param jwt the compacted version of the jwt
     * @return the username
     */
    public String username(String jwt) {
        Claims claims = this.claims(jwt);
        return claims != null ? claims.getSubject() : null;
    }

    /**
     * Check if the jwt belongs to a student
     *
     * @param jwt the compacted version of the jwt
     * @return true if the jwt belongs to a student, false otherwise
     */
    public boolean isStudent(String jwt) {
        return this.isValid(jwt) && this.isStudent(this.claims(jwt));
    }

    /**
     * Check if the claims belong to a student
     *
     * @param claims the set of claims from the jwt
     * @return true if the jwt belongs to a student, false otherwise
     */
    public boolean isStudent(Claims claims) {
        return this.claimsContainsRole(claims, "ROLE_STUDENT");
    }

    /**
     * Check if the jwt belongs to a teacher
     *
     * @param jwt the compacted version of the jwt
     * @return true if the jwt belongs to a teacher, false otherwise
     */
    public boolean isTeacher(String jwt) {
        return this.isValid(jwt) && this.isTeacher(this.claims(jwt));
    }

    /**
     * Check if the claims belong to a teacher
     *
     * @param claims the set of claims from a jwt
     * @return true if the jwt belongs to a teacher, false otherwise
     */
    public boolean isTeacher(Claims claims) {
        return this.claimsContainsRole(claims, "ROLE_TEACHER");
    }

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
}

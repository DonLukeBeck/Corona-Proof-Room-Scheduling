package nl.tudelft.sem.identity.design.pattern;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import nl.tudelft.sem.identity.entity.TokenRole;

/**
 * Code that all handlers have in common.
 */
public abstract class BaseValidator implements Validator {

    private transient Validator next;
    private transient String secret = "semcoronaproofroomreservation";
    private transient JwtParser parser = Jwts.parser().setSigningKey(secret);

    /**
     * Get the claims from the compact jws, a cryptographically signed jwt.
     *
     * @param jwt the compacted version of the jwt
     * @return the set of claims or {@code null} if not valid
     */
    public Claims claims(String jwt) {
        return parser.parseClaimsJws(jwt).getBody();
    }

    public void setNext(Validator h) {
        this.next = h;
    }

    /**
     * Runs check on the next object in chain or ends traversing
     * if we are in last object in chain.
     *
     * @param tokenRole token to be validated
     * @return next validation
     * @throws Exception if the chain is complete
     */
    protected boolean checkNext(TokenRole tokenRole) {
        if (next == null) {
            return true;
        }
        return next.handle(tokenRole);
    }
}

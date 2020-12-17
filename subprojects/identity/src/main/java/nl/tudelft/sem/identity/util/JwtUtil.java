package nl.tudelft.sem.identity.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.tudelft.sem.identity.repository.UserRepository;
import nl.tudelft.sem.identity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@ComponentScan(basePackages = {"nl.tudelft.sem.identity.service"})
public class JwtUtil {

    //key to create token with
    private String secret = "semcoronaproofroomreservation";

    @Autowired
    UserService userService;

    public String getSecret() {
        return secret;
    }

    /**
     * Method to extract netid from token.
     *
     * @param token token to extract netid from
     * @return the netid extracted from token
     */
    public String extractNetid(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Method to extract the expiration date from the token.
     *
     * @param token token to extract expiration date from
     * @return the expiration date extracted from token
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * Method to verify if the token is expired.
     *
     * @param token token to extract expiration date from
     * @return true if one hour limit is reached and the token expired
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generate jwt token from incoming username.
     *
     * @param username the username to generate token for
     * @return a jwt token for the username
     */
    public String generateToken(String username) {
        var user = userService.loadUserByUsername(username);
        var claims = new HashMap<String, Object>();
        claims.put("scope", new ArrayList<GrantedAuthority>(user.getAuthorities()));
        return createToken(claims, username);
    }

    /**
     * Create a 1 hour token for the netid on the issued date.
     *
     * @return the created token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        var now = System.currentTimeMillis();

        return Jwts.builder().setClaims(claims)
                .setSubject(subject).setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, secret).setHeaderParam("typ", "JWT").compact();
    }

    /**
     * Verify if the token received is valid.
     *
     * @param token token to be verified
     * @param userDetails details of the user to verify the token with
     * @return true if the token is valid
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String netid = extractNetid(token);
        return (netid.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}

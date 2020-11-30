package nl.tudelft.sem.identity.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
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

    //return netid
    public String extractNetid(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //return expiration date
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

    //if 1 hour limit is reached
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //create token based on username
    public String generateToken(String username) {
        var user = userService.loadUserByUsername(username);
        var claims = new HashMap<String, Object>();
        claims.put("scope", user.getAuthorities());
        return createToken(claims, username);
    }

    //create a 1 hour token for the netid on the issued date
    private String createToken(Map<String, Object> claims, String subject) {
        var now = System.currentTimeMillis();

        return Jwts.builder().setClaims(claims)
                .setSubject(subject).setIssuedAt(new Date(now))
                .setExpiration(new Date(now + 1000 * 60 * 60))
                .signWith(SignatureAlgorithm.HS256, secret).setHeaderParam("typ", "JWT").compact();
    }

    //verify if token received is valid
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String netid = extractNetid(token);
        return (netid.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}

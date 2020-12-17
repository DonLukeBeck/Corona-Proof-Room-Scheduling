package nl.tudelft.sem.identity.controller;

import lombok.extern.slf4j.Slf4j;
import nl.tudelft.sem.identity.entity.AuthenticationRequest;
import nl.tudelft.sem.identity.entity.TokenInfo;
import nl.tudelft.sem.identity.util.JwtUtil;
import nl.tudelft.sem.identity.util.JwtValidate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ComponentScan(basePackages = {"nl.tudelft.sem.identity.*"})
// The autowired beans don't need to be serialized
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
@Slf4j
public class UserController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Validates jwt tokens from incoming authorization requests.
     *
     * @param token String containing jwt token
     * @return Extracted role from token
     */
    @PostMapping(path = "/validate")
    public TokenInfo validate(@RequestBody String token) {
        JwtValidate jwtValid = new JwtValidate();
        JwtUtil jwtUtil = new JwtUtil();

        if (jwtValid.isTeacher(token)) {
            return new TokenInfo("teacher", jwtUtil.extractNetid(token));
        } else if (jwtValid.isStudent(token)) {
            return new TokenInfo("student", jwtUtil.extractNetid(token));
        } else {
            return new TokenInfo(null, jwtUtil.extractNetid(token));
        }
    }

    /**
     * Generate jwt token from incoming authentication details.
     *
     * @param authRequest Object containing username and password
     * @return a jwt token if username and password are valid
     * @throws AuthenticationException if authentication fails
     */
    @PostMapping("/login")
    public String generateToken(@RequestBody AuthenticationRequest authRequest)
            throws AuthenticationException {
        try {
            //validate username and password
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest
                    .getNetid(), authRequest.getPassword())
            );
        } catch (AuthenticationServiceException ex) {
            // service failure
            log.info("Inside login of UserController");
            return "Service failure";

        } catch (AuthenticationException ex) {
            //authentication failure
            log.info("Inside login of UserController");
            return "Authentication failure";
        }
        //generate web token if authentication successful
        return jwtUtil.generateToken(authRequest.getNetid());
    }
}

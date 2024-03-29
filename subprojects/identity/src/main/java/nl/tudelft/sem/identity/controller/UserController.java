package nl.tudelft.sem.identity.controller;

import lombok.extern.slf4j.Slf4j;
import nl.tudelft.sem.identity.design.pattern.DateValidator;
import nl.tudelft.sem.identity.design.pattern.RoleValidator;
import nl.tudelft.sem.identity.design.pattern.Validator;
import nl.tudelft.sem.identity.entity.AuthenticationRequest;
import nl.tudelft.sem.identity.entity.TokenInfo;
import nl.tudelft.sem.identity.entity.TokenRole;
import nl.tudelft.sem.identity.util.JwtUtil;
import nl.tudelft.sem.shared.entity.StringMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * This method makes use of the Chain of Responsibility design pattern.
     *
     * @param token String containing jwt token
     * @return Extracted role from token
     */
    // the `isTeacher` variable isn't actually being overwritten, it just appears after itself
    // because it can take different values
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    @PostMapping(path = "/validate")
    public ResponseEntity<?> validate(@RequestBody String token) {

        //creating the chain
        Validator handler = new DateValidator();
        handler.setNext(new RoleValidator());
        boolean isTeacher;
        boolean isValid;

        if (handler.handle(new TokenRole("ROLE_TEACHER", token))) {
            isTeacher = isValid = true;
        } else {
            isTeacher = !(isValid = handler.handle(new TokenRole("ROLE_STUDENT", token)));
        }
        if (isValid) {
            return ResponseEntity.ok(new TokenInfo(isTeacher ? "teacher" : "student",
                    jwtUtil.extractNetid(token)));
        } else {
            return ResponseEntity.ok(new TokenInfo(null, jwtUtil.extractNetid(token)));
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
    public ResponseEntity<?> generateToken(@RequestBody AuthenticationRequest authRequest) {
        try {
            //validate username and password
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest
                    .getNetid(), authRequest.getPassword())
            );
        } catch (AuthenticationServiceException ex) {
            // service failure
            log.info("Inside login of UserController");
            return ResponseEntity.status(HttpStatus
                    .INTERNAL_SERVER_ERROR).body(new StringMessage("Service failure"));

        } catch (AuthenticationException ex) {
            //authentication failure
            log.info("Inside login of UserController");
            return ResponseEntity.status(HttpStatus
                    .FORBIDDEN).body(new StringMessage("Authentication failure"));
        }
        //generate web token if authentication successful
        return ResponseEntity.ok(new StringMessage(jwtUtil.generateToken(authRequest.getNetid())));
    }
}

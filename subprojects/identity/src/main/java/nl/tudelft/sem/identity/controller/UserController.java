package nl.tudelft.sem.identity.controller;

import nl.tudelft.sem.identity.entity.AuthenticationRequest;
import nl.tudelft.sem.identity.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/")
    public String welcome() {
        return "Welcome";
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthenticationRequest authRequest) throws Exception {
        try {
            //validate username and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getNetid(), authRequest.getPassword())
            );
        } catch (Exception ex) {
            //authentication failure
            throw new Exception("Invalid netid/password");
        }
        //generate web token if authentication successful
        return jwtUtil.generateToken(authRequest.getNetid());
    }
}

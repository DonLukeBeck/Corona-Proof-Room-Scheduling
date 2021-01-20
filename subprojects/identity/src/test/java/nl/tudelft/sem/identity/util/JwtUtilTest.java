package nl.tudelft.sem.identity.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.Date;
import nl.tudelft.sem.identity.entity.AuthenticationRequest;
import nl.tudelft.sem.identity.entity.User;
import nl.tudelft.sem.identity.repository.UserRepository;
import nl.tudelft.sem.identity.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    private final String netid = "CorrectNetId";
    private final String wrongNetid = "WrongNetId";

    private final String password = "SecurePassword123";

    private String token = "";

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    User correctUser = new User(netid, encoder.encode(password), "student", false);
    User wrongUser = new User(wrongNetid, encoder.encode(password), "student", false);

    @MockBean
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        when(userRepository.findByNetid(netid)).thenReturn(correctUser);
        when(userRepository.findByNetid(wrongNetid)).thenReturn(wrongUser);
        this.token = jwtUtil.generateToken(netid);
    }

    @Test
    void extractNetid() {
        assertThat(jwtUtil.extractNetid(token)).isEqualTo(netid);
    }

    @Test
    void extractExpiration() {
        assertThat(jwtUtil.extractExpiration(token)).isAfter(new Date(System.currentTimeMillis()));
    }

    @Test
    void extractClaim() {
        assertThat(jwtUtil.extractClaim(token, Claims::getSubject)).isEqualTo(netid);
    }

    @Test
    void validateToken() {
        var userDetails = userService.loadUserByUsername(netid);
        assertThat(jwtUtil.validateToken(token, userDetails)).isTrue();
    }

    @Test
    void validateWrongToken() {
        var userDetails = userService.loadUserByUsername(wrongNetid);
        assertThat(jwtUtil.validateToken(token, userDetails)).isFalse();
    }
}
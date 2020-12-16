package nl.tudelft.sem.identity.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import nl.tudelft.sem.identity.entity.AuthenticationRequest;
import nl.tudelft.sem.identity.entity.User;
import nl.tudelft.sem.identity.repository.UserRepository;
import nl.tudelft.sem.identity.util.JwtUtil;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ContextConfiguration(classes = UserController.class)
@AutoConfigureMockMvc
@WebMvcTest
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class UserControllerTest {

    ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private JwtUtil jwtUtil;

    private MockMvc mockMvc;

    private User correctUser;

    private AuthenticationRequest authenticationRequest;

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    UserRepository userRepository;

    /**
     * The initial setup before each test.
     */
    @BeforeEach
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.correctUser = new User("CorrectNetid",
                encoder.encode("SecurePassword123"),
                "student", false);
        this.authenticationRequest = new AuthenticationRequest(correctUser.getNetid(),
            "SecurePassword123");
    }


    @Test
    public void loginCorrect() throws Exception {
        when(userRepository.findByNetid(correctUser.getNetid())).thenReturn(correctUser);

        String uri = "/login";
        String jwt = mockMvc.perform(post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();

        Claims claims = Jwts.parser().setSigningKey(jwtUtil.getSecret()).parseClaimsJws(jwt)
                .getBody();
        assertThat(claims.getSubject()).isEqualTo("CorrectNetid");
        ArrayList<LinkedHashMap<String, Object>> scope =  claims.get("scope", ArrayList.class);
        assertTrue(scope.get(0).containsValue("ROLE_STUDENT"));
        var cond = new Condition<LinkedHashMap>(
                m -> m.containsValue("ROLE_STUDENT"), "Contains student");
        assertThat(scope).haveExactly(1, cond);
        assertThat(claims.getIssuedAt()).isBefore(claims.getExpiration());
    }

    @Test
    public void loginFailedPassword() throws Exception {
        when(userRepository.findByNetid(correctUser.getNetid())).thenReturn(correctUser);
        authenticationRequest.setPassword("1234");
        String uri = "/login";
        String jwt = mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(authenticationRequest))).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals(jwt, "Authentication failure");
    }

    @Test
    public void loginFailedUsername() throws Exception {
        authenticationRequest.setNetid("fail");
        when(userRepository.findByNetid(correctUser.getNetid())).thenReturn(correctUser);
        String uri = "/login";
        String jwt = mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(authenticationRequest))).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertEquals(jwt, "Authentication failure");
    }

}



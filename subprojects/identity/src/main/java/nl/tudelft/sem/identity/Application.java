package nl.tudelft.sem.identity;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import nl.tudelft.sem.identity.entity.User;
import nl.tudelft.sem.identity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableJpaRepositories("nl.tudelft.sem.identity.repository")
// This class doesn't ever need to be serialized, so neither do it's members
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class Application {

    @Autowired
    private UserRepository repository;

    Collector<? super User, ? extends Object, List<User>> collector = Collectors.toList();

    /**
     * Initialise a list of users to be added to the database.
     */
    @PostConstruct
    void initUsers() {

       	//password encoder to encode raw password in database
       	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

       	String password = encoder.encode("12345");

       	//list of user objects to be 'registered' in the database
       	List<User> users = Stream.of(
       	    new User("luca", password, "student", false),
            new User("luka", password, "teacher", true),
            new User("testTeacher", password, "teacher", true)
       	).collect(collector);
       	repository.saveAll(users);
    }

    @Bean
    @LoadBalanced
    RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

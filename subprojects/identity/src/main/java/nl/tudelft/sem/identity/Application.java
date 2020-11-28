package nl.tudelft.sem.identity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import nl.tudelft.sem.identity.entity.User;
import nl.tudelft.sem.identity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableDiscoveryClient
@SpringBootApplication
public class Application {
	@Autowired
	private UserRepository repository;

	@PostConstruct
	public void initUsers() {

		//password encoder to encode raw password in database
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		//list of user objects to be 'registered' in the database
		List<User> users = Stream.of(
				new User("luca", encoder.encode("1234"), "student", false)
		).collect(Collectors.toList());
		repository.saveAll(users);
	}


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

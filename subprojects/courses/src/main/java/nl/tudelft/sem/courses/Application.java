package nl.tudelft.sem.courses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@SuppressWarnings("checkstyle:indentation")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}

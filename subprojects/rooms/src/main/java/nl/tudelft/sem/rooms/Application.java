package nl.tudelft.sem.rooms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@SpringBootApplication
@EntityScan("nl.tudelft.sem.restrictions")
@EnableJpaRepositories("nl.tudelft.sem.rooms")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

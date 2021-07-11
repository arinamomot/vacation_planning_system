package accountService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main entry point of a Spring Boot application.
 */
@SpringBootApplication
@EnableCaching
public class AccountServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run( AccountServiceApplication.class, args);
    }
}

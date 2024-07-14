package inje.nonabang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NonabangApplication {

    public static void main(String[] args) {
        SpringApplication.run(NonabangApplication.class, args);
    }

}

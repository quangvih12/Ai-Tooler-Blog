package com.example.productapi;

import com.example.productapi.entity.User;
import com.example.productapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@Slf4j
public class ProductApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApiApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                log.info("Initializing database with sample data...");

                User adminUser = User.builder()
                        .username("admin")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("admin123"))
                        .fullName("System Administrator")
                        .role("ADMIN")
                        .build();

                User savedAdmin = userRepository.save(adminUser);
                log.info("Created admin user: {}", savedAdmin.getUsername());

                log.info("Database initialization completed");
                log.info("Default admin credentials - Username: admin, Password: admin123");
            }
        };
    }
}
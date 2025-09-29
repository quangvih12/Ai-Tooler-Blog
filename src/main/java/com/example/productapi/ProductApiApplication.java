package com.example.productapi;

import com.example.productapi.entity.Product;
import com.example.productapi.entity.User;
import com.example.productapi.repository.ProductRepository;
import com.example.productapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
@Slf4j
public class ProductApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApiApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           ProductRepository productRepository,
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

                List<Product> sampleProducts = List.of(
                        Product.builder()
                                .name("Laptop Pro 15")
                                .description("High-performance laptop with 15-inch display, Intel Core i7, 16GB RAM, 512GB SSD")
                                .price(new BigDecimal("1299.99"))
                                .quantity(25)
                                .category("Electronics")
                                .imageUrl("https://example.com/images/laptop-pro-15.jpg")
                                .createdBy(savedAdmin)
                                .build(),

                        Product.builder()
                                .name("Wireless Mouse")
                                .description("Ergonomic wireless mouse with precision tracking and long battery life")
                                .price(new BigDecimal("29.99"))
                                .quantity(100)
                                .category("Accessories")
                                .imageUrl("https://example.com/images/wireless-mouse.jpg")
                                .createdBy(savedAdmin)
                                .build(),

                        Product.builder()
                                .name("USB-C Hub")
                                .description("7-in-1 USB-C hub with HDMI, USB 3.0, SD card reader, and PD charging")
                                .price(new BigDecimal("49.99"))
                                .quantity(50)
                                .category("Accessories")
                                .imageUrl("https://example.com/images/usb-c-hub.jpg")
                                .createdBy(savedAdmin)
                                .build(),

                        Product.builder()
                                .name("Mechanical Keyboard")
                                .description("RGB backlit mechanical gaming keyboard with blue switches")
                                .price(new BigDecimal("89.99"))
                                .quantity(35)
                                .category("Electronics")
                                .imageUrl("https://example.com/images/mechanical-keyboard.jpg")
                                .createdBy(savedAdmin)
                                .build(),

                        Product.builder()
                                .name("Monitor 27 4K")
                                .description("27-inch 4K UHD IPS monitor with HDR support and USB-C connectivity")
                                .price(new BigDecimal("449.99"))
                                .quantity(15)
                                .category("Electronics")
                                .imageUrl("https://example.com/images/monitor-27-4k.jpg")
                                .createdBy(savedAdmin)
                                .build()
                );

                productRepository.saveAll(sampleProducts);
                log.info("Created {} sample products", sampleProducts.size());

                log.info("Database initialization completed");
                log.info("Default admin credentials - Username: admin, Password: admin123");
            }
        };
    }
}
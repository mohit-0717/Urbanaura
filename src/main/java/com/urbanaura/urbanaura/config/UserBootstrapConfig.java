package com.urbanaura.urbanaura.config;

import com.urbanaura.urbanaura.model.User;
import com.urbanaura.urbanaura.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserBootstrapConfig {

    @Bean
    public CommandLineRunner seedUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${urbanaura.security.user.username:user}") String userUsername,
            @Value("${urbanaura.security.user.password:user123}") String userPassword,
            @Value("${urbanaura.security.admin.username:admin}") String adminUsername,
            @Value("${urbanaura.security.admin.password:admin123}") String adminPassword) {
        return args -> {
            createUserIfMissing(userRepository, passwordEncoder, userUsername, userPassword, "ROLE_USER");
            createUserIfMissing(userRepository, passwordEncoder, adminUsername, adminPassword, "ROLE_ADMIN");
        };
    }

    private void createUserIfMissing(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     String username,
                                     String rawPassword,
                                     String role) {
        userRepository.findByUsername(username).orElseGet(() ->
                userRepository.save(new User(username, passwordEncoder.encode(rawPassword), role)));
    }
}

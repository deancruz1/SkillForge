package com.skillforge.backend;

import com.skillforge.backend.entity.User;
import com.skillforge.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// @Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsByEmail("test@student.com")) {
            User existing = userRepository.findByEmail("test@student.com").get();
            userRepository.delete(existing);
            System.out.println("Deleted existing test user");
        }

        User testUser = new User(
                "Test Student",
                "$2a$10$placeholder",
                "test@student.com",
                User.UserRole.STUDENT
        );

        userRepository.save(testUser);

        System.out.println("User saved with ID: " + testUser.getId());

        // Read it back
        User found = userRepository.findByEmail("test@student.com")
                .orElseThrow(() -> new RuntimeException("User not found!"));

        System.out.println("Found user: " + found);
        System.out.println("Role: " + found.getRole());
        System.out.println("Created at: " + found.getCreatedAt());
    }
}
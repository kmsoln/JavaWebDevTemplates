package com.project.mvc.services;

import java.util.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * This service class, CustomUserDetailsService, implements the UserDetailsService interface
 * to handle user authentication and authorization. It provides in-memory storage for user
 * details and uses a PasswordEncoder for secure password storage.
 * <p>
 * In a real-world application, you should replace the in-memory storage with a database
 * and adapt the implementation to use JPA for interacting with user data.
 * <p>
 * This class includes methods to load user details, check if a user exists, and save a new user.
 * <p>
 * Note: This class is designed for educational purposes, and students should adapt it for use
 * with JPA and a database in a production environment.
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // In-memory storage for user details (replace with database/JPA in a real application)
    private final Map<String, User> users = new HashMap<>();

    // Password encoder for secure password storage
    private final PasswordEncoder passwordEncoder;

    // Constructor with PasswordEncoder injection
    public CustomUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;

        // Populate some dummy users with encoded passwords
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        users.put("user", new User("user", passwordEncoder.encode("user"), authorities));

        authorities.add(new SimpleGrantedAuthority("ADMIN"));
        users.put("admin", new User("admin", passwordEncoder.encode("admin"), authorities));
    }

    // Load user details by username during authentication
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Build and return a UserDetails object
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();
    }

    // Save a new user with the provided username, password, and roles
    public void save(String username, String password, String... roles) {
        // Check if the user already exists
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("User with username " + username + " already exists.");
        }

        // Create authorities from provided roles
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        // Encode the password before saving
        String encodedPassword = passwordEncoder.encode(password);

        // Create a new User and save it
        User user = new User(username, encodedPassword, authorities);
        users.put(username, user);
    }

    // Check if a user with the given username already exists
    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    // Retrieves all users stored in memory.
    public Collection<User> getAllUsers() {
        return users.values();
    }
}

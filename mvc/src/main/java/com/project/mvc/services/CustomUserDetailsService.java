package com.project.mvc.services;

import java.util.*;

import com.project.mvc.repositories.UserDetailsRepository;
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
 * Note: This class is designed for educational purposes, and students should adapt it for use
 * with JPA and a database in a production environment.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    //  in-memory repository to store user data ( no any database. just simulation for repository )
    private final UserDetailsRepository repo;

    // Password encoder for secure password storage
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserDetailsRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;

        // Populate some dummy users with encoded passwords
        // Admin Account
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("USER"));

        // Save dummy user "user" to the repository
        this.repo.save(new User(
                "user",
                this.passwordEncoder.encode("user"),
                authorities)
        );

        // User Account
        authorities.clear();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));

        // Save dummy user "admin" to the repository
        this.repo.save(new User(
                "admin",
                this.passwordEncoder.encode("admin"),
                authorities)
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Load user from the repository by username
        User user = repo.loadUserByUsername(username);

        // If user is not found, throw exception
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Build and return a UserDetails object
        return user;
    }

    public void createUser(User user) {
        String username = user.getUsername();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        Collection<GrantedAuthority> authorities = user.getAuthorities();

        // Create a new User object with hashed password and save to repository
        User finalUser = new User(username, hashedPassword, authorities);
        repo.save(finalUser);
    }

    public boolean userExists(String username) {
        return repo.existsByUsername(username);
    }

    public Collection<User> getAllUsers(){
        return repo.findAll();
    }
}

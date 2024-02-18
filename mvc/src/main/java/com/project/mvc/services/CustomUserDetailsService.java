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
        // Check if user exists by username
        boolean isExists = this.userExists(username);

        // If user is not found, throw exception
        if (!isExists) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Build and return a UserDetails object
        return repo.loadUserByUsername(username);
    }

    public void createUser(User user) {
        // Check if user exists by username
        boolean isExists = this.userExists(user);

        // Get Username
        String username = user.getUsername();

        // If user is found, throw exception
        if (isExists) {
            throw new UsernameNotFoundException("User found with username: " + user.getUsername());
        }

        // Hashing user password
        String hashedPassword = passwordEncoder.encode(user.getPassword());

        // Get the authorities
        Collection<GrantedAuthority> authorities = user.getAuthorities();

        // Create a new User object with hashed password and save to repository
        User finalUser = new User(username, hashedPassword, authorities);
        repo.save(finalUser);
    }

    public void updateUser(User user) throws UsernameNotFoundException{
        // Check if user exists by username
        boolean isExists = this.userExists(user);

        // If user is not found, throw exception
        if (!isExists) {
            throw new UsernameNotFoundException("User not found with username: " + user.getUsername());
        }

        repo.save(user);
    }

    public void deleteUser(String username) {
        repo.deleteByUsername(username);
    }

    public boolean userExists(String username) {
        return repo.existsByUsername(username);
    }

    public boolean userExists(User user) {
        return this.userExists(user.getUsername());
    }

    public Collection<User> getAllUsers(){
        return repo.findAll();
    }
}

package com.example.repository;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository {

    // In-memory storage for user details (replace with database/JPA in a real application)
    private static final Map<String, User> users = new HashMap<>();

    public User loadUserByUsername(String username) throws UsernameNotFoundException { return users.get(username); }

    public boolean existsByUsername(String username) { return users.containsKey(username); }

    public Collection<User> findAll() { return users.values(); }

    public void save(User user) {
        String username = user.getUsername();

        // Save the user to the in-memory repository
        users.put(username, user);
    }

    public void deleteByUsername(String username) {
        // Check if the user not exists
        if (!users.containsKey(username)) throw new IllegalArgumentException("User with username " +  username + " is not exists.");

        users.remove(username);
    }
}
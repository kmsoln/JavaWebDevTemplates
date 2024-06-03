package com.project.mvc.controllers;

import com.project.mvc.models.user.UserRole;
import com.project.mvc.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Collection;

@Controller
@RequestMapping("/user")
public class UserController {

    private final CustomUserDetailsService service;

    @Autowired
    public UserController(CustomUserDetailsService userDetailsService) {
        this.service = userDetailsService;
    }

    @GetMapping("/allUsers")
    public String showUsers(Model model) {
        Collection<User> users = service.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("viewPath", "user/all_users");
        return "shared/_layout";
    }

    @GetMapping("/modify/{username}")
    public String showModifyForm(@PathVariable String username, Model model) {
        model.addAttribute("viewPath", "user/modify_user");
        model.addAttribute("error", "Username is not exists. Please choose a different username.");
        UserDetails user = service.loadUserByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("roles", UserRole.values());
        return "shared/_layout";
    }

    @GetMapping("/modify/{username}/deleteAuthority/{authority}")
    public String deleteAuthority(@PathVariable String username,
                                  @PathVariable String authority) {
        try {
            // Check if authority is valid
            UserRole[] roles = UserRole.values();
            boolean roleIsExist = Arrays.asList(roles).contains(UserRole.valueOf(authority));

            if (!roleIsExist) {
                // Handle invalid authority
                return "redirect:/user/modify/" + username + "?roleNotFound";
            }

            // Check if user exists
            if (!service.userExists(username)) {
                // Handle non-existing user
                return "redirect:/user/modify/" + username + "?userNotFound";
            }

            UserDetails user = service.loadUserByUsername(username);

            // Delete the user authority
            service.deleteFromUserAuthorities(user, authority);
            return "redirect:/user/modify/" + username + "?roleDeletedSuccess";
        } catch (Exception e) {
            // Handle exception
            return "redirect:/user/modify/" + username + "?roleDeleteFailed";
        }
    }

    @GetMapping("/modify/{username}/addAuthority/{authority}")
    public String addAuthority(@PathVariable String username,
                               @PathVariable String authority) {
        try {
            // Check if authority is valid
            UserRole[] roles = UserRole.values();
            boolean roleIsExist = Arrays.asList(roles).contains(UserRole.valueOf(authority));

            if (!roleIsExist) {
                // Handle invalid authority
                return "redirect:/user/modify/" + username + "?roleNotFound";
            }

            // Check if user exists
            if (!service.userExists(username)) {
                // Handle non-existing user
                return "redirect:/user/modify/" + username + "?userNotFound";
            }

            UserDetails user = service.loadUserByUsername(username);
            service.addToUserAuthorities(user, authority);
            return "redirect:/user/modify/" + username + "?roleAddedSuccess";
        } catch (Exception e) {
            // Handle exception
            return "redirect:/user/modify/" + username + "?roleAddFailed";
        }
    }

    @GetMapping("/delete/{username}")
    public String deleteUser(@PathVariable String username, Model model) {
        if (!service.userExists(username)) {
            model.addAttribute("error", "Username does not exist. Please choose a different username.");
            return "redirect:/user/allUsers";
        }

        // Delete the user
        service.deleteUser(username);
        return "redirect:/user/allUsers";
    }
}

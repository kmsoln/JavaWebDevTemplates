package com.project.mvc.controllers;

import com.project.mvc.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}

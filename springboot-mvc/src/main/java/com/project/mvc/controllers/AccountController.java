package com.project.mvc.controllers;

import com.project.mvc.models.account.SignupModel;
import com.project.mvc.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Controller
public class AccountController {

    private final CustomUserDetailsService service;

    @Autowired
    public AccountController(CustomUserDetailsService userDetailsService) {
        this.service = userDetailsService;
    }

    @GetMapping("/login")
    public String loginView(Model model){
        model.addAttribute("title", "Login");
        model.addAttribute("viewPath", "account/login");
        return "shared/_layout";
    }

    @GetMapping("/signup")
    public String signupView(Model model){
        model.addAttribute("title", "Signup");
        model.addAttribute("model", new SignupModel());
        model.addAttribute("viewPath", "account/signup");
        return "shared/_layout";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute("model") @Valid SignupModel model,
                         Model pageModel) {

        // Check if the user already exists
        if (service.userExists(model.getUsername())) {
            pageModel.addAttribute("viewPath", "account/signup");
            pageModel.addAttribute("error", "Username already exists. Please choose a different username.");
            return "redirect:/signup?userExists";
        }

        // Set default role(s) for new users
        String[] roles = {"USER"};

        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        User user = new User(model.getUsername(), model.getPassword(), authorities);

        // Save the new user using the CustomUserDetailsService
        service.createUser(user);

        // Redirect to the login page or any other page as needed
        return "redirect:/login?registrationSuccess";
    }

}

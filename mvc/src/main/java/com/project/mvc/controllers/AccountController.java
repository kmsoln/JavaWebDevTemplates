package com.project.mvc.controllers;

import com.project.mvc.models.SignupModel;
import com.project.mvc.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AccountController {

    private final CustomUserDetailsService service;

    @Autowired
    public AccountController(CustomUserDetailsService userDetailsService) {
        this.service = userDetailsService;
    }

    @GetMapping("/login")
    public String loginView(Model model){
        model.addAttribute("viewPath", "account/login");
        return "shared/_layout";
    }

    @GetMapping("/signup")
    public String signupView(Model model){
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
            return "account/signup";
        }

        // Set default role(s) for new users
        String[] authorities = {"USER"};

        // Save the new user using the CustomUserDetailsService
        service.save(model.getUsername(), model.getPassword(), authorities);

        // Redirect to the login page or any other page as needed
        return "redirect:/login?registrationSuccess";
    }

}

package com.project.mvc.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    @GetMapping("/index")
    public String homeView(Model model, Authentication authentication){
        if (authentication != null && authentication.isAuthenticated()) {
            // User is authenticated, return a different view path
            return "redirect:/home/dashboard";
        }

        // User is not authenticated, return the default view path
        model.addAttribute("viewPath", "home/index");
        return "shared/_layout";
    }


    @GetMapping("/dashboard")
    public String dashboardView(Model model){
        model.addAttribute("viewPath", "home/dashboard");
        return "shared/_layout";
    }
}

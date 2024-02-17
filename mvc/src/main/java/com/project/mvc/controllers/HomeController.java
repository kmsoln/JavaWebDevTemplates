package com.project.mvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
    @GetMapping("/index")
    public String homeView(Model model){
        model.addAttribute("viewPath", "home/index");
        return "shared/_layout";
    }

    @GetMapping("/dashboard")
    public String dashboardView(Model model){
        model.addAttribute("viewPath", "home/dashboard");
        return "shared/_layout";
    }
}

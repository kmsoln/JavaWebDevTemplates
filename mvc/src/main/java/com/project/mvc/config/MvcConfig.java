package com.project.mvc.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    
    public void addViewControllers(ViewControllerRegistry registry) {
        // Home
        registry.addRedirectViewController("/", "/home/index");
        registry.addRedirectViewController("/home", "/home/index");
        registry.addViewController("/home/index").setViewName("/home/index");
        registry.addViewController("/home/dashboard").setViewName("/home/dashboard");

        // Account
        registry.addViewController("/login").setViewName("/account/login");
        registry.addViewController("/signup").setViewName("");
    }

}

package com.project.mvc.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * Configuration class for customizing Spring MVC behavior.
 *
 * <p>
 * The main purpose of this class is to define view controllers, which map URLs directly to view names without the need for a controller class.
 * By using view controllers, you can easily redirect URLs to specific views or templates, simplifying navigation and improving code readability.
 * </p>
 *
 * <p>
 * View controllers are particularly useful when you have simple URL mappings that do not require additional logic in a controller class.
 * This class uses {@link ViewControllerRegistry} to register view controllers for various URLs.
 * </p>
 *
 * <p>
 * Example usage:
 * </p>
 * <pre>
 * {@code
 * @Configuration
 * public class MvcConfig implements WebMvcConfigurer {
 *
 *     public void addViewControllers(ViewControllerRegistry registry) {
 *         registry.addRedirectViewController("/", "/home/index");
 *         registry.addViewController("/home/index").setViewName("/home/index");
 *     }
 * }
 * }
 * </pre>
 *
 * <p>
 * In this example, view controllers are defined for different URLs within the application.
 * The home-related URLs are redirected to the "/home/index" view, and a specific view is assigned to "/home/index".
 * </p>
 *
 * <p>
 * It is important to note that this class is part of the Spring MVC configuration and works in conjunction with other configuration classes,
 * such as those defining Spring Security or handling static resources.
 * </p>
 *
 * @see WebMvcConfigurer
 * @see ViewControllerRegistry
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    public void addViewControllers(ViewControllerRegistry registry) {
        // Home
        registry.addRedirectViewController("/", "/home/index");

        // Account
        registry.addRedirectViewController("/register","/signup");
        registry.addRedirectViewController("/account/logout","/logout");
    }

    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");

        return templateResolver;
    }
}

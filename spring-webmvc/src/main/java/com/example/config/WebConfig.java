package com.example.config;

import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
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
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    public static final String DEFAULT_CHAR_ENCODING = "UTF-8";
    private static final String TEMPLATE_RESOLVER_PREFIX = "/templates/";
    private static final String TEMPLATE_RESOLVER_SUFFIX = ".html";
    private static final String TEMPLATE_RESOLVER_TEMPLATE_MODE = "HTML5";
    private static final String TEMPLATE_RESOLVER_CHAR_ENCODING = DEFAULT_CHAR_ENCODING;

    /**
     * Register view controllers for simple URL mappings.
     *
     * @param registry the ViewControllerRegistry to use
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Home
        registry.addRedirectViewController("/", "/home/index");

        // Account
        registry.addRedirectViewController("/register", "/signup");
        registry.addRedirectViewController("/account/logout", "/logout");
    }

    /**
     * Configure the locations of static resources such as CSS, JavaScript, and images.
     *
     * @param registry the ResourceHandlerRegistry to use
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    /**
     * Configures the Thymeleaf template resolver.
     *
     * <p>
     * The template resolver is responsible for loading Thymeleaf templates from the specified location.
     * </p>
     *
     * @return the configured ClassLoaderTemplateResolver bean
     */
    @Bean
    public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(TEMPLATE_RESOLVER_PREFIX);
        templateResolver.setSuffix(TEMPLATE_RESOLVER_SUFFIX);
        templateResolver.setTemplateMode(TEMPLATE_RESOLVER_TEMPLATE_MODE);
        templateResolver.setCharacterEncoding(TEMPLATE_RESOLVER_CHAR_ENCODING);
        templateResolver.setCacheable(false);
        templateResolver.setOrder(1);
        // Enable Thymeleaf debug mode
        templateResolver.setCheckExistence(true);
        return templateResolver;
    }

    /**
     * Configures the Thymeleaf template engine.
     *
     * <p>
     * The template engine processes Thymeleaf templates and applies the configured dialects.
     * </p>
     *
     * @param templateResolver the template resolver to use
     * @return the configured SpringTemplateEngine bean
     */
    @Bean
    public SpringTemplateEngine templateEngine(ClassLoaderTemplateResolver templateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.addDialect(new LayoutDialect());
        templateEngine.addDialect(new SpringSecurityDialect());

        return templateEngine;
    }

    /**
     * Configures the Thymeleaf view resolver.
     *
     * <p>
     * The view resolver is responsible for resolving view names to Thymeleaf templates.
     * </p>
     *
     * @param templateEngine the template engine to use
     * @return the configured ThymeleafViewResolver bean
     */
    @Bean
    public ThymeleafViewResolver thymeleafViewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setCharacterEncoding(DEFAULT_CHAR_ENCODING);
        viewResolver.setOrder(1);
        return viewResolver;
    }

    /**
     * Configures view resolvers.
     *
     * <p>
     * This method registers the Thymeleaf view resolver with the ViewResolverRegistry.
     * </p>
     *
     * @param registry the ViewResolverRegistry to use
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(thymeleafViewResolver(templateEngine(templateResolver())));
    }
}

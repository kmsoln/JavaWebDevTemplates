package com.example.config;

import com.example.repository.UserRepository;
import com.example.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for Spring Security.
 *
 * <p>
 * This class extends {@link WebSecurityConfigurerAdapter} to provide custom security configurations.
 * It defines security rules for HTTP requests, login, logout, and user authentication.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Configures HTTP security for the application.
     *
     * <p>
     * This method sets up security rules for different URL patterns, specifying which URLs are accessible to which roles.
     * It also configures the login page, default success URL, and logout settings.
     * </p>
     *
     * @param http the {@link HttpSecurity} to modify
     * @throws Exception if an error occurs while configuring security
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(requests -> requests
                        .antMatchers(
                                "/",                      // Home page
                                "/home/index",            // Index page
                                "/signup/**",             // Signup related URLs
                                "/login/**",              // Login related URLs
                                "/lib/**",                // Library resources (e.g., Bootstrap, jQuery)
                                "/src/**",                // Source files
                                "/js/**",                 // JavaScript files
                                "/css/**",                // CSS files
                                "/error/**"               // Error pages
                        ).permitAll()                    // Allow access to these URLs without authentication
                        .antMatchers("/user/**").hasAuthority("ADMIN")  // Restrict access to URLs under /user/ to users with ADMIN role
                        .anyRequest().authenticated()    // Require authentication for any other request
                )
                .formLogin(form -> form
                        .loginPage("/login")            // Custom login page
                        .defaultSuccessUrl("/home/dashboard") // Redirect to dashboard on successful login
                        .permitAll()                    // Allow everyone to see the login page
                )
                .logout(LogoutConfigurer::permitAll)    // Allow everyone to log out
                .userDetailsService(customUserDetailsService()); // Use custom UserDetailsService for authentication
    }

    /**
     * Configures the password encoder bean.
     *
     * <p>
     * This method returns a {@link BCryptPasswordEncoder} bean for encoding passwords.
     * </p>
     *
     * @return the {@link PasswordEncoder} bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the user repository bean.
     *
     * <p>
     * This method returns a {@link UserRepository} bean for accessing user data.
     * </p>
     *
     * @return the {@link UserRepository} bean
     */
    @Bean
    public UserRepository userDetailsRepository() {
        return new UserRepository();
    }

    /**
     * Configures the custom user details service bean.
     *
     * <p>
     * This method returns a {@link CustomUserDetailsService} bean for loading user-specific data.
     * </p>
     *
     * @return the {@link CustomUserDetailsService} bean
     */
    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService(userDetailsRepository(), passwordEncoder());
    }
}

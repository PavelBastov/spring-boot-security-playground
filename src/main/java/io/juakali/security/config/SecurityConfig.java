package io.juakali.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

/**
 * Central place to experiment with Spring Security.
 * <p>
 * The {@link SecurityFilterChain} bean replaces the old WebSecurityConfigurerAdapter (removed back
 * in Spring Security 6). Everything is configured with the lambda DSL.
 */
@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) {
    http
        .authorizeHttpRequests(auth -> auth
            // Public pages — no authentication required.
            .requestMatchers("/", "/public", "/css/**").permitAll()
            // Anything under /admin requires the ADMIN role.
            .requestMatchers("/admin/**").hasRole("ADMIN")
            // Every other request needs an authenticated user.
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")        // our custom Thymeleaf login page
            .permitAll()
        )
        .logout(logout -> logout
            .logoutSuccessUrl("/?logout")
            .permitAll()
        )
        .addFilterBefore(new ProhibitedFilter(), AuthorizationFilter.class)
        .addFilterBefore(new RobotAuthenticationFilter(), AuthorizationFilter.class);
    return http.build();
  }

  /**
   * Two in-memory users so you can immediately try role-based access. Swap this out for a
   * JdbcUserDetailsManager or a custom UserDetailsService once you want to play with persistence.
   */
  @Bean
  UserDetailsService userDetailsService(PasswordEncoder encoder) {
    UserDetails user = User.withUsername("user")
        .password(encoder.encode("password"))
        .roles("USER")
        .build();

    UserDetails admin = User.withUsername("admin")
        .password(encoder.encode("password"))
        .roles("USER", "ADMIN")
        .build();

    return new InMemoryUserDetailsManager(user, admin);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    // Delegating encoder: stores hashes with a {bcrypt}-style prefix so you can
    // migrate algorithms later without breaking existing passwords.
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}

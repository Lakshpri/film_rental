package com.example.film_rental_app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.*;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    // 🔐 Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 👤 Users & Roles
    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        PasswordEncoder enc = passwordEncoder();

        UserDetails master = User.builder()
                .username("padmaprabha")
                .password(enc.encode("1234"))
                .roles("MASTER_DATA")
                .build();

        UserDetails staff = User.builder()
                .username("madhumita")
                .password(enc.encode("1234"))
                .roles("LOCATION_STAFF")
                .build();

        UserDetails customer = User.builder()
                .username("subbalakshmi")
                .password(enc.encode("1234"))
                .roles("CUSTOMER_RENTAL")
                .build();

        UserDetails film = User.builder()
                .username("krishnaprakash")
                .password(enc.encode("1234"))
                .roles("FILM_CATALOG")
                .build();

        UserDetails reports = User.builder()
                .username("lakshmipriya")
                .password(enc.encode("1234"))
                .roles("PAYMENT_REPORTS")
                .build();

        return new InMemoryUserDetailsManager(
                master, staff, customer, film, reports
        );
    }

    // 🔒 Security Rules
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // MASTER DATA
                        .requestMatchers("/api/countries/**", "/api/cities/**",
                                "/api/languages/**", "/api/categories/**")
                        .hasRole("MASTER_DATA")

                        // LOCATION
                        .requestMatchers("/api/addresses/**", "/api/stores/**",
                                "/api/staff/**")
                        .hasRole("LOCATION_STAFF")


                        // CUSTOMER + RENTALS (broad rule comes AFTER the specific one above)
                        .requestMatchers("/api/customers/**", "/api/rentals/**",
                                "/api/inventory/**")
                        .hasRole("CUSTOMER_RENTAL")

                        // FILMS
                        .requestMatchers("/api/films/**", "/api/actors/**",
                                "/api/film-texts/**")
                        .hasRole("FILM_CATALOG")

                        // PAYMENTS + REPORTS + ANALYTICS
                        .requestMatchers("/api/payments/**", "/api/reports/**",
                                "/api/analytics/**")
                        .hasRole("PAYMENT_REPORTS")

                        // SWAGGER
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // 🌐 CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of("http://localhost:*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
package com.example.microbankingproject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public static final String[] AUTH_WHITELIST = {
            "/api/auth/**",
            "/api/neighborhood/**",
            "/api/neighborhood/image/**",
            "/api/blog/**",
            "/api/blog/image/**",
            "/api/banner/**",
            "/api/banner/image/**",
            "/api/announcement/**",
            "/api/favorite/**",
            "/api/offer/**",
            "v3/api-docs/**",
            "swagger-ui/**",
            "swagger-ui.html"
    };

    public static final String[] USER_AUTH_WHITELIST = {
    };

    public static final String[] AGENT_AUTH_WHITELIST = {

    };

    public static final String[] MODERATOR_AUTH_WHITELIST = {
    };

    public static final String[] ADMINISTRATOR_AUTH_WHITELIST = {

    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(AUTH_WHITELIST).permitAll().
                                requestMatchers(USER_AUTH_WHITELIST).
                                hasRole("USER").
                                requestMatchers(AGENT_AUTH_WHITELIST).
                                hasRole("AGENT").
                                requestMatchers(MODERATOR_AUTH_WHITELIST).
                                hasRole("MODERATOR").
                                requestMatchers(ADMINISTRATOR_AUTH_WHITELIST).
                                hasRole("ADMINISTRATOR").
                                anyRequest().
                                authenticated());
        http.authenticationProvider(authenticationProvider);
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

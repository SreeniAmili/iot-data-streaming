package com.relay42.iot.stream.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Security configuration class for the IoT Data Streaming API.
 * Configures HTTP security settings, including CSRF protection, frame options, and request authorization.
 * Enables basic authentication for securing API endpoints.
 */
@Configuration
public class SecurityConfig {

    /**
     * Configures the security filter chain for the application.
     * - Disables CSRF protection to allow non-browser clients to interact with the API.
     * - Disables frame options to enable access to the H2 console.
     * - Configures basic authentication for securing API endpoints.
     * - Permits unrestricted access to the H2 console, Swagger UI, and API documentation.
     * - Requires authentication for all other requests.
     *
     * @param http the HttpSecurity object used to configure security settings.
     * @return the configured SecurityFilterChain instance.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .httpBasic(withDefaults());

        return http.build();
    }
}

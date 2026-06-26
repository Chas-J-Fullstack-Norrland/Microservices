package org.chasapi.microservices.gatewayapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@EnableWebFlux
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // Tillåt trafik till Swagger UI och Eureka-upptäckter utan token
                        .pathMatchers("/v3/api-docs/**", "/swagger-ui/**", "/webjars/**", "/eureka/**").permitAll()
                        // All annan trafik kräver validerad JWT
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {}) // Aktiverar JWT-validering mot secret-key i api-gateway.yml
                );

        return http.build();
    }
}

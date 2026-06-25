package org.chasapi.microservices.gatewayapi.auth;

import org.chasapi.microservices.gatewayapi.auth.UserClient;
import org.chasapi.microservices.gatewayapi.auth.UserClientImpl;
import org.chasapi.microservices.gatewayapi.auth.WebClientServiceCallInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class UserClientConfig {

    @Bean
    public WebClient userServiceWebClient() {
        return WebClient.builder()
                .baseUrl("http://user-service")
                .filter(new WebClientServiceCallInterceptor("gateway-service").filter())
                .build();
    }

    @Bean
    public UserClient userClient(WebClient webClient) {
        return new UserClientImpl(webClient);
    }
}
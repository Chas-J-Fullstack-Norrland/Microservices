package org.chasapi.microservices.gatewayapi.auth;

import org.chasapi.microservices.servicesecurity.JwtTokenGenerator;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

public class WebClientServiceCallInterceptor {


    private final String tokenSubject;

    public WebClientServiceCallInterceptor(String tokenSubject) {
        this.tokenSubject = tokenSubject;
    }

    public ExchangeFilterFunction filter() {
        return (request, next) -> {

            if (request.headers().containsHeader("Authorization")) {
                return next.exchange(request);
            }

            String token = JwtTokenGenerator.generateToken(
                    tokenSubject,
                    "SERVICE",
                    "ROLE_SERVICE",
                    30000
            );

            ClientRequest newRequest = ClientRequest.from(request)
                    .header("Authorization", "Bearer " + token)
                    .build();

            return next.exchange(newRequest);
        };
    }

}

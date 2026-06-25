package org.chasapi.microservices.gatewayapi.auth;

import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@Component
public class GatewayAuthFilterGatewayFilterFactory extends AbstractGatewayFilterFactory<GatewayAuthFilterGatewayFilterFactory.Config> {
    public static class Config{ }

    public GatewayAuthFilterGatewayFilterFactory(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (auth == null|| ! auth.startsWith("Bearer ")){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        });
    }



}

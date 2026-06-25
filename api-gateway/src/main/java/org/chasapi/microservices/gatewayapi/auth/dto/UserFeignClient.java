package org.chasapi.microservices.gatewayapi.auth.dto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserFeignClient {

    @GetMapping("/users/{username}")
    UserDetailsDTO fetchDetailsForUserName(@PathVariable String username);


}

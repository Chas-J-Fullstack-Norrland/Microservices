package org.chasapi.microservices.gatewayapi.auth;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.chasapi.microservices.gatewayapi.auth.dto.LoginRequest;
import org.chasapi.microservices.gatewayapi.auth.dto.UserDetailsDTO;
import org.chasapi.microservices.gatewayapi.auth.dto.UserFeignClient;
import org.chasapi.microservices.servicesecurity.JwtTokenGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GateWayAuthService {

    private final UserFeignClient userClient;

    public String login(LoginRequest request){
        UserDetailsDTO user;

        try{
            user = userClient.fetchDetailsForUserName(request.username());
        } catch (FeignException.NotFound e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid password or username");
        }

        if(!Objects.equals(user.password(), request.password())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid password or username");
        }

        return JwtTokenGenerator.generateToken(user.username(),"user-token",user.role());


    }


}

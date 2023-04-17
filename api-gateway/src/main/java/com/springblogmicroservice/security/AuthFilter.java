package com.springblogmicroservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final RouteValidator routeValidator;
    private final JwtUtils jwtUtils;

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) ->{

            if(routeValidator.isSecured.test(exchange.getRequest())){
                if(exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new RuntimeException("missing auth header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                if(authHeader!=null && authHeader.startsWith("Bearer ")){
                    authHeader=authHeader.substring(7);
                }

                try{
                    jwtUtils.isTokenValid(authHeader);
                }catch (Exception e){
                    System.out.println("invalid access!");
                    throw new RuntimeException("unauthorized access to application!");
                }

            }

            return  chain.filter(exchange);
        });
    }

    public static class Config{

    }
}

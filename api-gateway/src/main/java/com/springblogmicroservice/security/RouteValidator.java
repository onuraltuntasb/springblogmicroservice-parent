package com.springblogmicroservice.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> allowedApiEndpoints = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/api/auth/check",
            "/api/auth/refreshtoken",
            "/eureka"

    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> allowedApiEndpoints
                    .stream()
                    .noneMatch(uri->request.getURI().getPath().contains(uri));

}

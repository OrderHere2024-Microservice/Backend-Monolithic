package com.backend.orderhere.config;

public class StaticConfig {
    public final static String[] ignoreUrl = new String[]{
            // -- Swagger UI v2
            "/v3/api-docs/**",
            "/swagger-ui/**",
            // -- API
            "/v1/public/users/**",
            "/graphiql",
            "/graphql",

    };

    public final static String [] getOnlyUrl = new String[]{
            "/v1/public/dish/**",
            "/v1/public/restaurants/**",
            "/v1/public/ingredients/**",
            "/v1/public/category/**",
            "/health-check",
    };

    // JWT
    public final static String JwtPrefix = "Bearer ";
    public final static String JwtSecretKey = "Xh8wJZcPZfa7t2WdKr4zr9TnRfWP8xYhV1a8dYkHfTw=";
}

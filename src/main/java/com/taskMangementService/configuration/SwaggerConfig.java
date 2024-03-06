package com.taskMangementService.configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final String DEV_URL = "http://localhost:8090";

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(DEV_URL);
        devServer.setDescription("Local Server");

        Info info = new Info()
                .title("Task Management")
                .version("1.0")
                .description("Task Management API Documentation");

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}

package com.taskMangementService.enterypoint;

import com.taskMangementService.utils.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Configuration
@Slf4j
public class AccessDeniedEntryPoint implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("Access Denied. Unauthorized access attempt to resource {} Authorization header: {}",
                request.getRequestURI(),
                request.getHeader(Constants.HEADER_AUTHORIZATION));

        response.sendError(HttpStatus.FORBIDDEN.value(),
                "Access Denied. It seems you're trying to access a resource that you are not allowed! ");

    }
}

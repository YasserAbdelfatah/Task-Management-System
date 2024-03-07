package com.taskMangementService.enterypoint;

import com.taskMangementService.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.Optional;

@Configuration
@Slf4j
public class UnauthenticatedEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        log.info("Access Denied. Unauthenticated access attempt to resource {} from {} Authorization header: {}",
                httpServletRequest.getRequestURI(),
                Optional.ofNullable(httpServletRequest.getHeader(Constants.HEADER_AUTHORIZATION))
                        .orElse(httpServletRequest.getRemoteAddr()),
                httpServletRequest.getHeader(Constants.HEADER_AUTHORIZATION));

        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "It seems whether you're trying to access a resource without authenticating. Please authenticate first then try again!");
    }
}

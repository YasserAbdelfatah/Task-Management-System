package com.taskMangementService.filter;

import com.taskMangementService.error.exceptions.UserNotFoundException;
import com.taskMangementService.model.entities.User;
import com.taskMangementService.repositories.UserRepository;
import com.taskMangementService.utils.Constants;
import com.taskMangementService.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> token = jwtUtil.getTokenWithoutBearer(request);
            token.ifPresent(t -> verifyAndAuthenticateUser(request, t));
        } catch (Exception e) {
            log.error(Constants.MESSAGE_AUTHENTICATION_FAILED);
        }

        filterChain.doFilter(request, response);
    }

    private void verifyAndAuthenticateUser(HttpServletRequest request, String token) {
        if (jwtUtil.verifyToken(token)) {
            Claims claims = jwtUtil.extractAllClaims(token);
            String username = claims.getSubject();
            User user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new UserNotFoundException(username));

            Set<SimpleGrantedAuthority> roles = Set.of(new SimpleGrantedAuthority(user.getRole().name()));

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, roles);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }
}
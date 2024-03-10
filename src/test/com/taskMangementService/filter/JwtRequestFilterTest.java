package com.taskMangementService.filter;

import com.taskMangementService.model.entities.User;
import com.taskMangementService.repositories.UserRepository;
import com.taskMangementService.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private MockHttpServletRequest request;
    @Mock
    private MockHttpServletResponse response;
    @Mock
    private MockFilterChain filterChain;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal() throws Exception {


        String token = "validToken";

        when(jwtUtil.getTokenWithoutBearer(request)).thenReturn(Optional.of(token));
        when(jwtUtil.verifyToken(token)).thenReturn(true);

        Claims claims = mock(Claims.class);
        when(jwtUtil.extractAllClaims(token)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("testUser");

        User user = new User();
        user.setUserName("testUser");
        user.setRole(User.Role.USER);

        when(userRepository.findByUserName("testUser")).thenReturn(Optional.of(user));

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(userRepository, times(1)).findByUserName("testUser");

        Set<SimpleGrantedAuthority> roles = Set.of(new SimpleGrantedAuthority(user.getRole().name()));
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, roles);
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        verify(jwtUtil, times(1)).verifyToken(token);
        verify(jwtUtil, times(1)).extractAllClaims(token);
        verify(jwtUtil, times(1)).getTokenWithoutBearer(request);
        verify(userRepository, times(1)).findByUserName("testUser");

        // Check if the user is authenticated
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        assert authentication.isAuthenticated();
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws Exception {

        String invalidToken = "invalidToken";

        when(jwtUtil.getTokenWithoutBearer(request)).thenReturn(Optional.of(invalidToken));
        when(jwtUtil.verifyToken(invalidToken)).thenReturn(false);

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtUtil, times(1)).verifyToken(invalidToken);
        verify(jwtUtil, times(1)).getTokenWithoutBearer(request);

        // Check if the user is not authenticated
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }

    @Test
    void testDoFilterInternal_UserNotFound() throws Exception {

        String validToken = "validToken";

        when(jwtUtil.getTokenWithoutBearer(request)).thenReturn(Optional.of(validToken));
        when(jwtUtil.verifyToken(validToken)).thenReturn(true);

        Claims claims = mock(Claims.class);
        when(jwtUtil.extractAllClaims(validToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("nonExistingUser");

        when(userRepository.findByUserName("nonExistingUser")).thenReturn(Optional.empty());

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        verify(jwtUtil, times(1)).verifyToken(validToken);
        verify(jwtUtil, times(1)).extractAllClaims(validToken);
        verify(jwtUtil, times(1)).getTokenWithoutBearer(request);
        verify(userRepository, times(1)).findByUserName("nonExistingUser");

        // Check if the user is not authenticated
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }
}

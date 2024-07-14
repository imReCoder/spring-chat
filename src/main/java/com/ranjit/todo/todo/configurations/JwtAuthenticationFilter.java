package com.ranjit.todo.todo.configurations;

import com.ranjit.todo.todo.exceptions.ExceptionResolver;
import com.ranjit.todo.todo.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private HandlerExceptionResolver exceptionResolver;
    private final Logger _logger;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.exceptionResolver = exceptionResolver;
        this._logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Credentials", "true");
        String requrestUri = request.getRequestURI();
        _logger.info("Request URL: {}", requrestUri);
        if (requrestUri.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }


         String authHeader = request.getHeader("Authorization");
        if (requrestUri.startsWith("/ws-message")) {
            authHeader = request.getParameter("access_token");
        }
        _logger.info("Auth Header: {}", authHeader);
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                _logger.error("Authorization header is missing");
                throw new AuthenticationException("Authorization header is missing");
            }
            final String jwt = authHeader.substring(7);
            final String userId = jwtService.extractUserId(jwt);
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);
            _logger.info("User Details: {}", userDetails);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                request.setAttribute("user", userDetails);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
            }

        } catch (Exception e) {
            _logger.error("Error: {}-{}", e.getClass(), e.getMessage());
            AuthenticationException ex = new AuthenticationException(e.getMessage());
            exceptionResolver.resolveException(request, response, null, ex);
            return;
        }
    }

}

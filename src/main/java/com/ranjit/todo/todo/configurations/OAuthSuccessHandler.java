package com.ranjit.todo.todo.configurations;

import com.ranjit.todo.todo.dtos.LoginUserResponse;
import com.ranjit.todo.todo.dtos.RegisterUserDTO;
import com.ranjit.todo.todo.services.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthService _authService;
    OAuthSuccessHandler(AuthService authService) {
        this._authService = authService;
    }

    Logger _logger = LoggerFactory.getLogger(OAuthSuccessHandler.class);
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DefaultOidcUser principal = (DefaultOidcUser) authentication.getPrincipal();
        _logger.info("OAuth Success Handler"+ principal);
        RegisterUserDTO user = new RegisterUserDTO();
        user.setEmail(principal.getAttribute("email"));
        user.setName(principal.getAttribute("name"));
        user.setProfileImage(principal.getAttribute("picture"));
        user.setPassword("password");
        user.setProvider("google");
        ResponseEntity<LoginUserResponse> res =  _authService.oauthLogin(user);
        String token = Objects.requireNonNull(res.getBody()).getToken();
        response.sendRedirect("http://localhost:4200/auth/google?token="+token);
    }
}

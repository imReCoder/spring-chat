package com.ranjit.todo.todo.configurations;

import com.ranjit.todo.todo.services.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    final AuthenticationProvider _authenticationProvider;
    final JwtAuthenticationFilter _jwtAuthenticationFilter;
    final OAuthSuccessHandler oAuthSuccessHandler;

    public SecurityConfiguration(AuthenticationProvider _authenticationProvider, JwtAuthenticationFilter _jwtAuthenticationFilter,OAuthSuccessHandler oAuthSuccessHandler) {
        this._authenticationProvider = _authenticationProvider;
        this._jwtAuthenticationFilter = _jwtAuthenticationFilter;
        this.oAuthSuccessHandler = oAuthSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf-> csrf.disable())
                .authorizeHttpRequests(auth-> auth.requestMatchers("auth/**","login/**").permitAll().anyRequest().authenticated())
                .sessionManagement(sess-> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .cors(cors-> cors.disable())
//                .cors(cors-> cors.configurationSource(corsConfigurationSource()))
                .authenticationProvider(_authenticationProvider)
                .addFilterBefore(_jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

//        oAth login configuration
        http.oauth2Login(oauth-> {
//            oauth.loginPage("auth/login");
            oauth.successHandler(oAuthSuccessHandler);
        });


        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:8005"));
        configuration.setAllowedMethods(List.of("GET","POST"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",configuration);

        return source;
    }


}

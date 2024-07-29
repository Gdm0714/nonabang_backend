package inje.nonabang.config;

import inje.nonabang.handler.MyAuthenticationFailureHandler;
import inje.nonabang.handler.MyAuthenticationSuccessHandler;
import inje.nonabang.jwt.JwtAccessDeniedHandler;
import inje.nonabang.jwt.JwtAuthenticationEntryPoint;
import inje.nonabang.jwt.JwtSecurityConfig;

import inje.nonabang.jwt.TokenProvider;
import inje.nonabang.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    private final MyAuthenticationFailureHandler myAuthenticationFailureHandler;
    private final OAuthService oAuthService;
    private final String[] allowedUrls = {"/", "/v2/api-docs", "/swagger-resources", "/swagger-resources/**", "/configuration/ui", "/configuration/security", "/swagger-ui.html", "/webjars/**",
            "/v3/api-docs/**", "/swagger-ui/**"};


    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))


                // Use stateless session management
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/oauth2/authorization") // 사용자 정의 로그인 페이지 URL
                        .successHandler(myAuthenticationSuccessHandler)
                        .failureHandler(myAuthenticationFailureHandler)
                        .userInfoEndpoint(userInfoEndpoint ->
                                userInfoEndpoint.userService(oAuthService)
                        )
                )
                // Configure authorization rules
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(allowedUrls).permitAll()
                        .anyRequest().permitAll())

                .apply(new JwtSecurityConfig(tokenProvider)); // JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig class 적용

        return httpSecurity.build();


    }
}
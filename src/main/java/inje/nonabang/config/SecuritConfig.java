package inje.nonabang.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecuritConfig {

    private final String[] allowedUrls = {};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionFixation().migrateSession())
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(allowedUrls).permitAll()
                                .requestMatchers("/admin/members", "/admin/admins").hasAuthority("ADMIN")// requestMatchers의 인자로 전달된 url은 모두에게 허용
                                //                .requestMatchers(PathRequest.toH2Console()).permitAll()	// H2 콘솔 접속은 모두에게 허용
                                .anyRequest().authenticated()    // 그 외의 모든 요청은 인증 필요
                )
                .oauth2Login(oauth2Login ->
                        oauth2Login.loginPage("/api/auth/login")
                                .defaultSuccessUrl("/api/auth/login/success", true)
                                .failureUrl("/api/auth/login/failure"));
        return http.build();
    }
}

package com.example.config;

import com.example.repository.RoleRepository;
import com.example.security.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers("/myChatGpt/**").permitAll()
                                .requestMatchers("/actuator/**").permitAll()
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/user/create").permitAll()
                                .requestMatchers("/role/create").permitAll()
                                .requestMatchers("/account-holder/create").permitAll()
                                .requestMatchers("/batch/**").permitAll()
                                .requestMatchers("/account/create").permitAll()
                                .requestMatchers("/account/{accountId}/transactions").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/account/{accountId}/transactions/scroll").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/account/{accountId}/getTransactionTransfer").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/account/{accountId}/amount").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/account/**").hasRole("ADMIN")
                                .requestMatchers("/transaction/create").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("/transaction/createMultiple").hasRole("ADMIN")
                                .requestMatchers("/transaction/**").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthFilter(), UsernamePasswordAuthenticationFilter.class)
//                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->
//                        httpSecurityOAuth2ResourceServerConfigurer.jwt(jwtConfigurer ->
//                        jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .sessionManagement(sessionManagementConfigurer
                        -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
        ;
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthoritiesClaimName("realm_access.roles");
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
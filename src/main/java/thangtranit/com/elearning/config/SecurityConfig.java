package thangtranit.com.elearning.config;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import thangtranit.com.elearning.controller.user.CustomJwtDecoder;
import thangtranit.com.elearning.controller.user.CustomOAuth2LoginFailureHandler;
import thangtranit.com.elearning.controller.user.CustomOAuth2LoginSuccessHandler;
import thangtranit.com.elearning.repository.user.UserRepository;
import thangtranit.com.elearning.service.user.SecUserService;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository repository;

    @Value("${FE_ORIGIN}")
    private String FE_ORIGIN;

    private final CustomJwtDecoder customJwtDecoder;
    private final PasswordEncoder passwordEncoder;
    @NonFinal
    private final String[] PUBLIC_URL = new String[]
            {
                    "/api/auth/login",
                    "api/auth/refresh",
                    "/api/auth/register/**",
                    "/api/auth/forgot-password/**",
                    "/api/users/{id}/**",
                    "/api/quizzes/hottest",
                    "/api/quizzes/search",
                    "/api/courses/search",
                    "/api/courses/{id}",
                    "/api/courses/{id}/reviews/**",
                    "/api/courses/hottest",
                    "/api/posts/search",
                    "/api/posts/search_by_tags",
                    "/api/posts/hottest",
                    "/api/posts/{postId}",
                    "/api/posts/{postId}/comments",
                    "/api/posts/{postId}/comments/own",
                    "/api/tags/search",
                    "/api/hello",
                    "/api/test/**",
                    "/api/courses/payment-return",
                    "/ws/**",
                    "/user/**"
            };


    @Bean
    public UserDetailsService userDetailsService() {
        return new SecUserService(repository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(PUBLIC_URL).permitAll()
                                .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwtConfigurer ->
                                jwtConfigurer.decoder(customJwtDecoder)
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .successHandler(customOAuth2LoginSuccessHandler())
                        .failureHandler(customOAuth2LoginFailureHandler()))
                .formLogin(Customizer.withDefaults())
                .logout(Customizer.withDefaults())
                .rememberMe(rememberMe ->
                        rememberMe
                                .key("uniqueAndSecret")
                                .tokenValiditySeconds(3600)
                                .userDetailsService(userDetailsService())
                )
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin(FE_ORIGIN);
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    @Bean
    public CustomOAuth2LoginSuccessHandler customOAuth2LoginSuccessHandler() {
        return new CustomOAuth2LoginSuccessHandler();
    }

    @Bean
    public CustomOAuth2LoginFailureHandler customOAuth2LoginFailureHandler() {
        return new CustomOAuth2LoginFailureHandler();
    }


}

package com.thangthai.training.backend.config;

import com.thangthai.training.backend.config.token.TokenFilterConfiguerer;
import com.thangthai.training.backend.service.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenService tokenService;

    private final String[] PUBLIC ={
            "/actuator/**",
            "/user/register",
            "/user/login",
            "/user/activate",
            "/user/resend-activation-email",
            "/socket/**"
    };

    public SecurityConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        // TODO:
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*http.cors().disable().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers(PUBLIC).anonymous()
                .anyRequest().authenticated()
                .and().apply(new TokenFilterConfiguerer(tokenService));*/

        /**
         * Fixed set cors: Cross Origin Resource Sharing
         * For Spring Boot version 2.4.0++
         */
        http.cors(config -> {
                    CorsConfiguration cors = new CorsConfiguration();
                    cors.setAllowCredentials(true);
                    cors.setAllowedOriginPatterns(Collections.singletonList("http://*"));
                    cors.addAllowedHeader("*");
                    cors.addAllowedMethod("GET");
                    cors.addAllowedMethod("POST");
                    cors.addAllowedMethod("PUT");
                    cors.addAllowedMethod("DELETE");
                    cors.addAllowedMethod("OPTIONS");

                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", cors);

                    config.configurationSource(source);
                }).csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers(PUBLIC).anonymous()
                .anyRequest().authenticated()
                .and().apply(new TokenFilterConfiguerer(tokenService));
    }

    //Discard
    /*@Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        //config.addAllowedOrigin("http://localhost:4200");
        config.setAllowedOriginPatterns(Arrays.asList("http://localhost*"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        source.registerCorsConfiguration("/**",config);
        return new CorsFilter(source);
    }*/
}

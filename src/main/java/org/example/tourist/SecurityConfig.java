package org.example.tourist;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()
                        .requestMatchers("/cart/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/tour-packages/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/bookings/**").hasRole("ADMIN")
                        .requestMatchers("/bookings/create").hasRole("USER")
                        .requestMatchers("/bookings").hasRole("AGENT")
                        .requestMatchers("/agents/**").hasRole("ADMIN")
                        .requestMatchers("/agents").hasRole("AGENT")
                        .requestMatchers("/login", "/register", "/").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


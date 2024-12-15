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
                        // Разрешение доступа к статическим файлам
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**").permitAll()

                        // Доступ к корзине для пользователей и администраторов
                        .requestMatchers("/cart/**").hasAnyRole("ADMIN", "USER")

                        // Доступ к тур-пакетам (создание и редактирование) для агентов и админов
                        .requestMatchers("/tour-packages/create", "/tour-packages/edit/**").hasAnyRole("ADMIN", "AGENT")

                        // Удаление туров только для администраторов
                        .requestMatchers("/tour-packages/delete/**").hasRole("ADMIN")

                        // Бронирования: создание только для пользователей, изменение для администраторов и агентов
                        .requestMatchers("/bookings/create").hasRole("USER") // Только для пользователей
                        .requestMatchers("/bookings/my-bookings").hasRole("USER")
                        .requestMatchers("/bookings/**").hasAnyRole("ADMIN", "AGENT") // Для агентов и администраторов

                        // Отзывы: пользователи могут добавлять/редактировать, админ удаляет
                        .requestMatchers("/reviews/**").hasAnyRole("USER", "ADMIN") // Пользователь и админ могут работать с отзывами
                        .requestMatchers("/reviews/delete/**").hasRole("ADMIN") // Админ может удалять отзывы

                        // Доступ к агентам и клиентам только для администраторов
                        .requestMatchers("/agents/**", "/users/**").hasRole("ADMIN") // Только админ видит агентов и пользователей

                        // Страница логина и регистрации для всех
                        .requestMatchers("/login", "/register", "/").permitAll()

                        // Для других запросов необходима авторизация
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
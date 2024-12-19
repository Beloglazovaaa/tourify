package org.example.tourist.services;

import org.example.tourist.models.User;
import org.example.tourist.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * Сервис для загрузки информации о пользователе для аутентификации.
 * Реализует интерфейс {@link UserDetailsService} для интеграции с системой безопасности Spring Security.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Конструктор для инициализации {@link UserRepository}.
     *
     * @param userRepository репозиторий для работы с пользователями
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Загружает пользователя по имени пользователя для аутентификации.
     * Преобразует информацию о пользователе в объект {@link UserDetails}, который используется для аутентификации в Spring Security.
     *
     * @param username имя пользователя
     * @return объект {@link UserDetails} для аутентификации
     * @throws UsernameNotFoundException если пользователь с указанным именем не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Ищем пользователя по имени пользователя
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));

        // Преобразуем его в объект UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName())) // Преобразуем роли в авторитеты
                        .collect(Collectors.toList())
        );
    }
}

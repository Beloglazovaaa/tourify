// UserService.java
package org.example.tourist.services;

import org.example.tourist.models.Role;
import org.example.tourist.models.User;
import org.example.tourist.repositories.RoleRepository;
import org.example.tourist.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Регистрирует нового пользователя с заданным именем, паролем и ролью.
     */
    @Transactional
    public void registerUser(String username, String rawPassword, String roleName) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Роль не найдена: " + roleName));

        user.setRoles(Set.of(role));
        userRepository.save(user);
    }

    /**
     * Получить всех пользователей.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Обновить роль пользователя.
     */
    @Transactional
    public void updateUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        Role role = roleRepository.findByName(newRole)
                .orElseThrow(() -> new IllegalArgumentException("Роль не найдена: " + newRole));

        user.setRoles(Set.of(role));
        userRepository.save(user);
    }

    /**
     * Удалить пользователя по ID.
     */
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        userRepository.deleteById(userId);
    }

    /**
     * Найти пользователя по имени.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
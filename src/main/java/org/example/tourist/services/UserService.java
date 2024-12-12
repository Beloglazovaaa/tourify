package org.example.tourist.services;

import org.example.tourist.models.Role;
import org.example.tourist.models.User;
import org.example.tourist.repositories.RoleRepository;
import org.example.tourist.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
     *
     * @param username    имя пользователя
     * @param rawPassword необработанный пароль
     * @param roleName    имя роли
     * @throws RuntimeException если пользователь уже существует или роль не найдена
     */
    public void registerUser(String username, String rawPassword, String roleName) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Роль не найдена: " + roleName));

        user.setRoles(Set.of(role));
        userRepository.save(user);
    }

    /**
     * Добавить нового пользователя.
     *
     * @param user объект пользователя
     * @return сохраненный пользователь
     */
    public User addUser(User user) {
        // Можно добавить дополнительные проверки или валидацию
        return userRepository.save(user);
    }

    /**
     * Обновить пользователя.
     *
     * @param id      ID пользователя
     * @param updatedUser обновленный объект пользователя
     * @return обновленный пользователь
     * @throws RuntimeException если пользователь не найден
     */
    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        user.setUsername(updatedUser.getUsername());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        user.setRoles(updatedUser.getRoles());
        return userRepository.save(user);
    }

    /**
     * Удалить пользователя по ID.
     *
     * @param id ID пользователя
     * @throws RuntimeException если пользователь не найден
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Пользователь не найден");
        }
        userRepository.deleteById(id);
    }
}


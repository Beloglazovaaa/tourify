package org.example.tourist.services;

import org.example.tourist.models.Role;
import org.example.tourist.models.User;
import org.example.tourist.repositories.RoleRepository;
import org.example.tourist.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Сервис для работы с пользователями.
 * Предоставляет методы для регистрации пользователей, обновления их ролей, удаления и получения пользователей.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор для инициализации сервисов {@link UserRepository}, {@link RoleRepository} и {@link PasswordEncoder}.
     *
     * @param userRepository репозиторий пользователей
     * @param roleRepository репозиторий ролей
     * @param passwordEncoder компонент для кодирования паролей
     */
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Регистрирует нового пользователя с заданным именем, паролем и ролью.
     * Проверяет, что пользователь с таким именем еще не существует.
     *
     * @param username имя пользователя
     * @param rawPassword необработанный пароль пользователя
     * @param roleName имя роли для нового пользователя
     * @throws IllegalArgumentException если пользователь с таким именем уже существует или роль не найдена
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

        // Используем изменяемый набор для ролей
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);
    }

    /**
     * Получает список всех пользователей.
     *
     * @return список всех пользователей
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Обновляет роль пользователя.
     * Удаляет старую роль и добавляет новую.
     *
     * @param userId идентификатор пользователя
     * @param newRoleName имя новой роли для пользователя
     * @throws IllegalArgumentException если пользователь или роль не найдены
     */
    @Transactional
    public void updateUserRole(Long userId, String newRoleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        Role role = roleRepository.findByName(newRoleName)
                .orElseThrow(() -> new IllegalArgumentException("Роль не найдена: " + newRoleName));

        // Обновляем существующую изменяемую коллекцию ролей
        user.getRoles().clear();
        user.getRoles().add(role);

        // Альтернативный вариант:
        // Set<Role> roles = new HashSet<>();
        // roles.add(role);
        // user.setRoles(roles);

        userRepository.save(user);
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param userId идентификатор пользователя, которого нужно удалить
     */
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    /**
     * Находит пользователя по имени.
     *
     * @param username имя пользователя
     * @return {@link Optional} с найденным пользователем, если он существует
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
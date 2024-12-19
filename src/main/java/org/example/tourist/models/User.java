package org.example.tourist.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс, представляющий пользователя в системе.
 * Каждый пользователь может иметь несколько ролей.
 */
@Entity
@Table(name = "users")
public class User {

    /** Уникальный идентификатор пользователя */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Имя пользователя */
    private String username;

    /** Пароль пользователя */
    private String password;

    /** Множество ролей, назначенных пользователю */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",  // Таблица связи между пользователями и ролями
            joinColumns = @JoinColumn(name = "user_id"),  // Связь с пользователем
            inverseJoinColumns = @JoinColumn(name = "role_id")  // Связь с ролью
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * Конструктор по умолчанию.
     * Используется для создания пустого объекта пользователя.
     */
    public User() {}

    /**
     * Конструктор для инициализации пользователя с заданным именем пользователя и паролем.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Получает уникальный идентификатор пользователя.
     *
     * @return уникальный идентификатор пользователя
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор пользователя.
     *
     * @param id уникальный идентификатор пользователя
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получает имя пользователя.
     *
     * @return имя пользователя
     */
    public String getUsername() {
        return username;
    }

    /**
     * Устанавливает имя пользователя.
     *
     * @param username имя пользователя
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Получает пароль пользователя.
     *
     * @return пароль пользователя
     */
    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль пользователя.
     *
     * @param password пароль пользователя
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Получает множество ролей, назначенных пользователю.
     *
     * @return множество ролей
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Устанавливает множество ролей для пользователя.
     *
     * @param roles множество ролей, назначенных пользователю
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}

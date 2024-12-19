package org.example.tourist.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс, представляющий роль пользователя в системе.
 * Каждая роль может быть связана с несколькими пользователями.
 */
@Entity
@Table(name = "roles")
public class Role {

    /** Уникальный идентификатор роли */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Название роли (например, "USER", "ADMIN") */
    @Column(nullable = false, unique = true)
    private String name;

    /** Множество пользователей, имеющих эту роль */
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    /**
     * Конструктор по умолчанию.
     * Используется для создания пустого объекта роли.
     */
    public Role() {}

    /**
     * Конструктор для инициализации роли с заданным названием.
     *
     * @param name название роли (например, "USER", "ADMIN")
     */
    public Role(String name) {
        this.name = name;
    }

    /**
     * Получает уникальный идентификатор роли.
     *
     * @return уникальный идентификатор роли
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор роли.
     *
     * @param id уникальный идентификатор роли
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получает название роли.
     *
     * @return название роли
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название роли.
     *
     * @param name название роли
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получает множество пользователей, имеющих эту роль.
     *
     * @return множество пользователей
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Устанавливает множество пользователей, имеющих эту роль.
     *
     * @param users множество пользователей
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }
}


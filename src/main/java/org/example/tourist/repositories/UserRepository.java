package org.example.tourist.repositories;

import org.example.tourist.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link User}.
 * Содержит методы для выполнения операций с пользователями, такими как поиск по имени и подсчет пользователей по ролям.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Находит пользователя по имени пользователя.
     *
     * @param username имя пользователя
     * @return {@link Optional} с пользователем, если он найден, иначе пустой {@link Optional}
     */
    Optional<User> findByUsername(String username);

    /**
     * Выполняет запрос для подсчета количества пользователей для каждой роли.
     * Возвращает список объектов, где первый элемент - имя роли, второй - количество пользователей с этой ролью.
     *
     * @return список объектов, содержащих имя роли и количество пользователей с этой ролью
     */
    @Query("SELECT r.name, COUNT(u) FROM User u JOIN u.roles r GROUP BY r.name")
    List<Object[]> countUsersByRole();
}

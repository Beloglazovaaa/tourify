package org.example.tourist.repositories;

import org.example.tourist.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностью {@link Role}.
 * Содержит методы для выполнения операций с ролями пользователей.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Находит роль по ее названию.
     *
     * @param name название роли (например, "USER", "ADMIN")
     * @return {@link Optional} с ролью, если она найдена, иначе пустой {@link Optional}
     */
    Optional<Role> findByName(String name);
}

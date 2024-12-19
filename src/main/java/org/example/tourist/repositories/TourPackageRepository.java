package org.example.tourist.repositories;

import org.example.tourist.models.TourPackage;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link TourPackage}.
 * Содержит методы для выполнения операций с туристическими пакетами,
 * такими как поиск по доступности, названию и сортировка.
 */
@Repository
public interface TourPackageRepository extends JpaRepository<TourPackage, Long> {

    /**
     * Находит все туристические пакеты, доступные или недоступные.
     *
     * @param availability флаг доступности туристического пакета (true — доступен, false — недоступен)
     * @return список туристических пакетов с заданной доступностью
     */
    List<TourPackage> findByAvailability(boolean availability);

    /**
     * Находит все туристические пакеты, содержащие указанное имя в названии.
     *
     * @param name часть имени туристического пакета для поиска
     * @return список туристических пакетов, содержащих указанное имя
     */
    List<TourPackage> findByNameContaining(String name);

    /**
     * Находит все туристические пакеты, содержащие указанное имя в названии,
     * игнорируя регистр и сортируя результат.
     *
     * @param name часть имени туристического пакета для поиска
     * @param sort объект {@link Sort}, определяющий порядок сортировки
     * @return список туристических пакетов, содержащих указанное имя, отсортированный согласно заданному порядку
     */
    List<TourPackage> findByNameContainingIgnoreCase(String name, Sort sort);

    /**
     * Находит все туристические пакеты с сортировкой.
     *
     * @param sort объект {@link Sort}, определяющий порядок сортировки
     * @return список всех туристических пакетов, отсортированных согласно заданному порядку
     */
    List<TourPackage> findAll(Sort sort);
}

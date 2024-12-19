package org.example.tourist.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import jakarta.transaction.Transactional;
import org.example.tourist.models.TourPackage;
import org.example.tourist.repositories.BookingRepository;
import org.example.tourist.repositories.TourPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с туристическими пакетами.
 * Предоставляет функциональность для получения, добавления, обновления и удаления турпакетов,
 * а также для поиска и фильтрации туров.
 */
@Service
public class TourPackageService {

    private final TourPackageRepository tourPackageRepository;
    private final BookingRepository bookingRepository;

    /**
     * Конструктор для инициализации сервисов и репозиториев.
     *
     * @param tourPackageRepository репозиторий для работы с сущностями TourPackage
     * @param bookingRepository репозиторий для работы с сущностями Booking
     */
    @Autowired
    public TourPackageService(TourPackageRepository tourPackageRepository, BookingRepository bookingRepository) {
        this.tourPackageRepository = tourPackageRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Получить все туристические пакеты.
     *
     * @return список всех туристических пакетов
     */
    public List<TourPackage> getAllTourPackages() {
        return tourPackageRepository.findAll();
    }

    /**
     * Получить туристический пакет по его ID.
     *
     * @param id ID пакета
     * @return объект {@link TourPackage}, соответствующий данному ID
     * @throws RuntimeException если туристический пакет с данным ID не найден
     */
    public TourPackage getTourPackageById(Long id) {
        return tourPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Туристический пакет не найден"));
    }

    /**
     * Добавить новый туристический пакет.
     * Сохраняет новый туристический пакет в базе данных.
     *
     * @param tourPackage объект {@link TourPackage}, представляющий новый туристический пакет
     * @return сохраненный объект {@link TourPackage}
     */
    public TourPackage addTourPackage(TourPackage tourPackage) {
        return tourPackageRepository.save(tourPackage);
    }

    /**
     * Обновить данные туристического пакета.
     * Обновляет существующий туристический пакет в базе данных.
     *
     * @param id ID пакета, который нужно обновить
     * @param updatedTourPackage объект {@link TourPackage} с обновленными данными
     * @return обновленный объект {@link TourPackage}
     * @throws RuntimeException если туристический пакет с данным ID не найден
     */
    public TourPackage updateTourPackage(Long id, TourPackage updatedTourPackage) {
        TourPackage tourPackage = getTourPackageById(id);
        tourPackage.setName(updatedTourPackage.getName());
        tourPackage.setDescription(updatedTourPackage.getDescription());
        tourPackage.setImageUrl(updatedTourPackage.getImageUrl());
        tourPackage.setPrice(updatedTourPackage.getPrice());
        tourPackage.setAvailability(updatedTourPackage.getAvailability());
        tourPackage.setDuration(updatedTourPackage.getDuration());

        return tourPackageRepository.save(tourPackage);
    }

    /**
     * Получить все доступные туристические пакеты.
     * Фильтрует пакеты, где availability установлено в true.
     *
     * @return список доступных туристических пакетов
     */
    public List<TourPackage> getAvailableTourPackages() {
        return tourPackageRepository.findByAvailability(true);
    }

    /**
     * Поиск туристических пакетов по названию с возможностью сортировки.
     *
     * @param name название пакета для поиска
     * @param sort поле для сортировки (например, "price", "name")
     * @param direction направление сортировки ("asc" или "desc")
     * @return список туристических пакетов, которые содержат указанное имя
     */
    public List<TourPackage> searchTourPackages(String name, String sort, String direction) {
        Sort sorting = direction.equalsIgnoreCase("asc") ? Sort.by(sort).ascending() : Sort.by(sort).descending();

        if (name != null && !name.isEmpty()) {
            return tourPackageRepository.findByNameContainingIgnoreCase(name, sorting);
        } else {
            return tourPackageRepository.findAll(sorting);
        }
    }


    /**
     * Проверить, можно ли удалить туристический пакет.
     * Пакет можно удалить, если для него нет активных бронирований.
     *
     * @param id ID туристического пакета
     * @return true, если пакет можно удалить, иначе false
     */
    @Transactional
    public boolean canDeleteTourPackage(Long id) {
        return bookingRepository.countBookingsByTourPackageId(id) == 0;
    }

    /**
     * Удалить туристический пакет по его ID.
     * Туристический пакет может быть удален, если для него нет активных бронирований.
     *
     * @param tourPackageId ID туристического пакета, который нужно удалить
     * @throws IllegalStateException если для пакета есть активные бронирования, и его нельзя удалить
     */
    @Transactional
    public void deleteTourPackage(Long tourPackageId) {
        if (canDeleteTourPackage(tourPackageId)) {
            // Удаляем туристический пакет
            tourPackageRepository.deleteById(tourPackageId);
        } else {
            throw new IllegalStateException("Невозможно удалить турпакет, так как для него есть активные бронирования.");
        }
    }
}

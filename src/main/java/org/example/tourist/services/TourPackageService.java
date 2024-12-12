package org.example.tourist.services;

import org.example.tourist.models.TourPackage;
import org.example.tourist.repositories.TourPackageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TourPackageService {

    private final TourPackageRepository tourPackageRepository;

    public TourPackageService(TourPackageRepository tourPackageRepository) {
        this.tourPackageRepository = tourPackageRepository;
    }

    /**
     * Получить все туристические пакеты.
     *
     * @return список туристических пакетов
     */
    public List<TourPackage> getAllTourPackages() {
        return tourPackageRepository.findAll();
    }

    /**
     * Получить туристический пакет по ID.
     *
     * @param id ID пакета
     * @return туристический пакет
     * @throws RuntimeException если пакет не найден
     */
    public TourPackage getTourPackageById(Long id) {
        return tourPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Туристический пакет не найден"));
    }

    /**
     * Добавить новый туристический пакет.
     *
     * @param tourPackage объект туристического пакета
     * @return сохраненный пакет
     */
    public TourPackage addTourPackage(TourPackage tourPackage) {
        return tourPackageRepository.save(tourPackage);
    }

    /**
     * Обновить данные туристического пакета.
     *
     * @param id              ID пакета
     * @param updatedTourPackage обновленный объект пакета
     * @return обновленный пакет
     * @throws RuntimeException если пакет не найден
     */
    public TourPackage updateTourPackage(Long id, TourPackage updatedTourPackage) {
        TourPackage tourPackage = getTourPackageById(id);
        tourPackage.setName(updatedTourPackage.getName());
        tourPackage.setDescription(updatedTourPackage.getDescription());
        tourPackage.setImageUrl(updatedTourPackage.getImageUrl());
        tourPackage.setPrice(updatedTourPackage.getPrice());
        tourPackage.setAvailability(updatedTourPackage.getAvailability());
        return tourPackageRepository.save(tourPackage);
    }

    /**
     * Получить доступные туристические пакеты.
     *
     * @return список доступных пакетов
     */
    public List<TourPackage> getAvailableTourPackages() {
        return tourPackageRepository.findByAvailability(true);
    }

    /**
     * Поиск туристических пакетов по названию.
     *
     * @param name название для поиска
     * @return список пакетов, содержащих указанное название
     */
    public List<TourPackage> searchTourPackages(String name) {
        return tourPackageRepository.findByNameContaining(name);
    }

    /**
     * Удалить туристический пакет по ID.
     *
     * @param id ID пакета
     */
    public void deleteTourPackage(Long id) {
        if (!tourPackageRepository.existsById(id)) {
            throw new RuntimeException("Туристический пакет не найден");
        }
        tourPackageRepository.deleteById(id);
    }
}

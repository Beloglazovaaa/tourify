package org.example.tourist.models;

import jakarta.persistence.*;
import org.example.tourist.BookingStatus;

import java.util.Date;
import java.util.List;

/**
 * Класс, представляющий бронирование туристического пакета.
 * Содержит информацию о пользователе, турпакетах, дате бронирования и статусе.
 */
@Entity
@Table(name = "bookings")
public class Booking {

    /** Уникальный идентификатор бронирования */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Связь с пользователем, который сделал бронирование */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Список туров, связанных с данным бронированием */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "booking_tour_packages",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "tour_package_id")
    )
    private List<TourPackage> tourPackages;  // Список туров

    /** Дата и время бронирования */
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookingDate;

    /** Общая сумма бронирования */
    private Integer totalAmount;

    /** Статус бронирования */
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    /**
     * Конструктор по умолчанию.
     * Используется для создания пустого объекта бронирования.
     */
    public Booking() {}

    /**
     * Конструктор для инициализации бронирования с указанными параметрами.
     *
     * @param user пользователь, сделавший бронирование
     * @param tourPackages список туров, выбранных пользователем
     * @param bookingDate дата и время бронирования
     * @param totalAmount общая сумма бронирования
     * @param status статус бронирования
     */
    public Booking(User user, List<TourPackage> tourPackages, Date bookingDate, Integer totalAmount, BookingStatus status) {
        this.user = user;
        this.tourPackages = tourPackages;  // Используем список туров
        this.bookingDate = bookingDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор бронирования.
     *
     * @param id уникальный идентификатор бронирования
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получает пользователя, сделавшего бронирование.
     *
     * @return объект пользователя
     */
    public User getUser() {
        return user;
    }

    /**
     * Устанавливает пользователя, сделавшего бронирование.
     *
     * @param user объект пользователя
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Получает список туров, связанных с этим бронированием.
     *
     * @return список туров
     */
    public List<TourPackage> getTourPackages() {
        return tourPackages;
    }

    /**
     * Устанавливает список туров, связанных с этим бронированием.
     *
     * @param tourPackages список туров
     */
    public void setTourPackages(List<TourPackage> tourPackages) {
        this.tourPackages = tourPackages;
    }

    /**
     * Получает дату и время бронирования.
     *
     * @return дата и время бронирования
     */
    public Date getBookingDate() {
        return bookingDate;
    }

    /**
     * Устанавливает дату и время бронирования.
     *
     * @param bookingDate дата и время бронирования
     */
    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    /**
     * Получает общую сумму бронирования.
     *
     * @return общая сумма бронирования
     */
    public Integer getTotalAmount() {
        return totalAmount;
    }

    /**
     * Устанавливает общую сумму бронирования.
     *
     * @param totalAmount общая сумма бронирования
     */
    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Получает статус бронирования.
     *
     * @return статус бронирования
     */
    public BookingStatus getStatus() {
        return status;
    }

    /**
     * Устанавливает статус бронирования.
     *
     * @param status статус бронирования
     */
    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}

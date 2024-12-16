package org.example.tourist.models;

import jakarta.persistence.*;
import org.example.tourist.BookingStatus;

import java.util.Date;
import java.util.List;
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Связь с пользователем
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Связь с несколькими турами
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "booking_tour_packages",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "tour_package_id")
    )
    private List<TourPackage> tourPackages;  // Список туров

    @Temporal(TemporalType.TIMESTAMP)
    private Date bookingDate;

    private Integer totalAmount;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    public Booking() {}

    public Booking(User user, List<TourPackage> tourPackages, Date bookingDate, Integer totalAmount, BookingStatus status) {
        this.user = user;
        this.tourPackages = tourPackages;  // Используем список туров
        this.bookingDate = bookingDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<TourPackage> getTourPackages() { return tourPackages; }
    public void setTourPackages(List<TourPackage> tourPackages) { this.tourPackages = tourPackages; }

    public Date getBookingDate() { return bookingDate; }
    public void setBookingDate(Date bookingDate) { this.bookingDate = bookingDate; }

    public Integer getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Integer totalAmount) { this.totalAmount = totalAmount; }

    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
}

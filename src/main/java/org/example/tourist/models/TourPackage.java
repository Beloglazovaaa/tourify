package org.example.tourist.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tour_packages")
public class TourPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String imageUrl;
    private Integer price;
    private Boolean availability;
    private Integer duration;

    @OneToMany(mappedBy = "tourPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @OneToMany(mappedBy = "tourPackages", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    public TourPackage() {}

    public TourPackage(String name, String description, String imageUrl, Integer price, Boolean availability, Integer duration) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.availability = availability;
        this.duration = duration;
    }


    // Геттеры и Сеттеры
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Остальные геттеры и сеттеры
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {this.description = description;}

    public String getImageUrl() {return imageUrl;}

    public void setImageUrl(String imageUrl) {this.imageUrl = imageUrl;}

    public Integer getPrice() {return price;}

    public void setPrice(Integer price) {this.price = price;}

    public Boolean getAvailability() {return availability;}

    public void setAvailability(Boolean availability) {this.availability = availability;}

    public Integer getDuration() {return duration;}

    public void setDuration(Integer duration) {this.duration = duration;}

    public List<Booking> getBookings() {return bookings;}

    public void setBookings(List<Booking> bookings) {this.bookings = bookings;}

    public List<Review> getReviews() {return reviews;}

    public void setReviews(List<Review> reviews) {this.reviews = reviews;}

}


package com.mzp.carrental.entity.Cars;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mzp.carrental.entity.Users.Agency;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "agency_id", nullable = false)
    @JsonBackReference // Avoids circular reference
    private Agency agency;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    private String imageName;
    private String imageType;
    @Lob
    private byte[] imageData;

    @Column(nullable = false, unique = true)
    private String licensePlate;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false, unique = true)
    private String vin; // Vehicle Identification Number

    @Column(nullable = false)
    private int mileage;

    @Column(nullable = false)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType fuelType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Transmission transmission;

    @Column(nullable = false)
    private int seats;


    @Column(nullable = true)
    private String features;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private double pricePerDay;

    @Column(nullable = false)
    private double driverFeePerDay;

    @Column(nullable = true)
    private boolean available = true;

    @Column(nullable = true)
    private Double avgRating = 0.0; // Deals with Ratings entity

    @Column(nullable = true)
    private Integer ratingCount = 0; // Deals with Ratings entity



    public enum Category {
        SUV, HATCHBACK, SEDAN, COUPE, CONVERTIBLE, TRUCK, VAN, OTHER
    }

    public enum FuelType {
        PETROL, DIESEL, ELECTRIC, HYBRID, OTHER
    }

    public enum Transmission {
        AUTOMATIC, MANUAL
    }

    // First image
    private String firstImageName;
    private String firstImageType;

    @Lob
    private byte[] firstImageData;

    // Second image
    private String secondImageName;
    private String secondImageType;

    @Lob
    private byte[] secondImageData;

    // Third image
    private String thirdImageName;
    private String thirdImageType;

    @Lob
    private byte[] thirdImageData;
    // getters and setters

    // New fields for tracking total rents, total money made, and total days rented
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int totalRents = 0;

    @Column(nullable = false, columnDefinition = "DECIMAL(10, 2) DEFAULT 0.00")
    private double totalMoneyMade = 0;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int totalDaysRented = 0;




    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }


    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", year=" + year +
                ", color='" + color + '\'' +
                ", category=" + category +
                ", fuelType=" + fuelType +
                ", transmission=" + transmission +
                ", seats=" + seats +
                ", pricePerDay=" + pricePerDay +
                ", description='" + description + '\'' +
                '}'; // Excluding 'agency' to prevent recursion
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public double getDriverFeePerDay() {
        return driverFeePerDay;
    }

    public void setDriverFeePerDay(double driverFeePerDay) {
        this.driverFeePerDay = driverFeePerDay;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getFirstImageName() {
        return firstImageName;
    }

    public void setFirstImageName(String firstImageName) {
        this.firstImageName = firstImageName;
    }

    public String getFirstImageType() {
        return firstImageType;
    }

    public void setFirstImageType(String firstImageType) {
        this.firstImageType = firstImageType;
    }

    public byte[] getFirstImageData() {
        return firstImageData;
    }

    public void setFirstImageData(byte[] firstImageData) {
        this.firstImageData = firstImageData;
    }

    public String getSecondImageName() {
        return secondImageName;
    }

    public void setSecondImageName(String secondImageName) {
        this.secondImageName = secondImageName;
    }

    public String getSecondImageType() {
        return secondImageType;
    }

    public void setSecondImageType(String secondImageType) {
        this.secondImageType = secondImageType;
    }

    public byte[] getSecondImageData() {
        return secondImageData;
    }

    public void setSecondImageData(byte[] secondImageData) {
        this.secondImageData = secondImageData;
    }

    public String getThirdImageName() {
        return thirdImageName;
    }

    public void setThirdImageName(String thirdImageName) {
        this.thirdImageName = thirdImageName;
    }

    public String getThirdImageType() {
        return thirdImageType;
    }

    public void setThirdImageType(String thirdImageType) {
        this.thirdImageType = thirdImageType;
    }

    public byte[] getThirdImageData() {
        return thirdImageData;
    }

    public void setThirdImageData(byte[] thirdImageData) {
        this.thirdImageData = thirdImageData;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getTotalRents() {
        return totalRents;
    }

    public void setTotalRents(int totalRents) {
        this.totalRents = totalRents;
    }

    public double getTotalMoneyMade() {
        return totalMoneyMade;
    }

    public void setTotalMoneyMade(double totalMoneyMade) {
        this.totalMoneyMade = totalMoneyMade;
    }

    public int getTotalDaysRented() {
        return totalDaysRented;
    }

    public void setTotalDaysRented(int totalDaysRented) {
        this.totalDaysRented = totalDaysRented;
    }
}

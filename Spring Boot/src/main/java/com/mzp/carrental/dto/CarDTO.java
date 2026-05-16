package com.mzp.carrental.dto;

import com.mzp.carrental.entity.Cars.Car;
import lombok.Data;

@Data
public class CarDTO {
    private Long id;
    private String brand;
    private String model;
    private String licensePlate;
    private int year;
    private String vin;
    private int mileage;
    private String color;
    private Car.Category category;
    private Car.FuelType fuelType;
    private Car.Transmission transmission;
    private int seats;
    private String features;
    private String description;
    private double pricePerDay;
    private double driverFeePerDay;
    private boolean available;
    private Double avgRating;
    private Integer ratingCount;
    private Integer totalRents;
    private double totalMoneyMade;
    private int totalDaysRented;
    private String imageType;
    private String imageName;
    private byte[] imageData;
    private String firstImageType;
    private String firstImageName;
    private byte[] firstImageData;
    private String secondImageType;
    private String secondImageName;
    private byte[] secondImageData;
    private String thirdImageType;
    private String thirdImageName;
    private byte[] thirdImageData;

    // Constructors, getters, and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Car.Category getCategory() {
        return category;
    }

    public void setCategory(Car.Category category) {
        this.category = category;
    }

    public Car.FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(Car.FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public Car.Transmission getTransmission() {
        return transmission;
    }

    public void setTransmission(Car.Transmission transmission) {
        this.transmission = transmission;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
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

    public double getDriverFeePerDay() {
        return driverFeePerDay;
    }

    public void setDriverFeePerDay(double driverFeePerDay) {
        this.driverFeePerDay = driverFeePerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
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

    public Integer getTotalRents() {
        return totalRents;
    }

    public void setTotalRents(Integer totalRents) {
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

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getFirstImageType() {
        return firstImageType;
    }

    public void setFirstImageType(String firstImageType) {
        this.firstImageType = firstImageType;
    }

    public String getFirstImageName() {
        return firstImageName;
    }

    public void setFirstImageName(String firstImageName) {
        this.firstImageName = firstImageName;
    }

    public byte[] getFirstImageData() {
        return firstImageData;
    }

    public void setFirstImageData(byte[] firstImageData) {
        this.firstImageData = firstImageData;
    }

    public String getSecondImageType() {
        return secondImageType;
    }

    public void setSecondImageType(String secondImageType) {
        this.secondImageType = secondImageType;
    }

    public String getSecondImageName() {
        return secondImageName;
    }

    public void setSecondImageName(String secondImageName) {
        this.secondImageName = secondImageName;
    }

    public byte[] getSecondImageData() {
        return secondImageData;
    }

    public void setSecondImageData(byte[] secondImageData) {
        this.secondImageData = secondImageData;
    }

    public String getThirdImageType() {
        return thirdImageType;
    }

    public void setThirdImageType(String thirdImageType) {
        this.thirdImageType = thirdImageType;
    }

    public String getThirdImageName() {
        return thirdImageName;
    }

    public void setThirdImageName(String thirdImageName) {
        this.thirdImageName = thirdImageName;
    }

    public byte[] getThirdImageData() {
        return thirdImageData;
    }

    public void setThirdImageData(byte[] thirdImageData) {
        this.thirdImageData = thirdImageData;
    }
}
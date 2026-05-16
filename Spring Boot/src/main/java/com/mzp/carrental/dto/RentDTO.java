package com.mzp.carrental.dto;

import com.mzp.carrental.entity.Rent.Rent;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RentDTO {
    private Long id;
    private Long carId;
    private Integer customerId;
    private LocalDate startDate;
    private LocalDate endDate;


    private boolean includeDriver;
    private double totalPrice;


    private String carBrand;
    private String carModel;
    private String customerName;
    private String customerVerificationStatus;
    private String pickUpLocation;
    private String dropOffLocation;
    private String customerPhoneNumber;

    private Rent.RentStatus rentStatus;

    public enum RentStatus {
        NOT_STARTED, ONGOING, COMPLETED
    }

    private LocalDateTime createdDate;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Rent.RentStatus getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(Rent.RentStatus rentStatus) {
        this.rentStatus = rentStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerVerificationStatus() {
        return customerVerificationStatus;
    }

    public void setCustomerVerificationStatus(String customerVerificationStatus) {
        this.customerVerificationStatus = customerVerificationStatus;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    public boolean isIncludeDriver() {
        return includeDriver;
    }

    public void setIncludeDriver(boolean includeDriver) {
        this.includeDriver = includeDriver;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public String getDropOffLocation() {
        return dropOffLocation;
    }

    public void setDropOffLocation(String dropOffLocation) {
        this.dropOffLocation = dropOffLocation;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    @Override
    public String toString() {
        return "RentDTO{" +
                "id=" + id +
                ", carId=" + carId +
                ", customerId=" + customerId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", includeDriver=" + includeDriver +
                ", totalPrice=" + totalPrice +
                ", carBrand='" + carBrand + '\'' +
                ", carModel='" + carModel + '\'' +
                ", customerName='" + customerName + '\'' +
                ", pickUpLocation='" + pickUpLocation + '\'' +
                ", dropOffLocation='" + dropOffLocation + '\'' +
                ", rentStatus=" + rentStatus +
                '}';
    }
}

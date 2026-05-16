package com.mzp.carrental.dto;

import com.mzp.carrental.entity.Rent.RentalOrder;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class RentalOrderDTO {
    private Long id;
    private Long carId;
    private String carBrand;
    private String carModel;
    private Integer customerId;
    private String customerName;
    private String customerVerificationStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private String pickUpLocation;
    private String dropOffLocation;
    private String customerPhoneNumber;

    private boolean includeDriver;
    private double totalPrice;

    private LocalDateTime orderDate;

    private RentalOrder.OrderStatus status;

    public enum OrderStatus {
        CANCEL, PENDING, APPROVED, DENIED
    }

    public RentalOrder.OrderStatus getStatus() {
        return status;
    }

    public void setStatus(RentalOrder.OrderStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerVerificationStatus() {
        return customerVerificationStatus;
    }

    public void setCustomerVerificationStatus(String customerVerificationStatus) {
        this.customerVerificationStatus = customerVerificationStatus;
    }

    @Override
    public String toString() {
        return "RentalOrderDTO{" +
                "id=" + id +
                ", carId=" + carId +
                ", carBrand='" + carBrand + '\'' +
                ", carModel='" + carModel + '\'' +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", pickUpLocation='" + pickUpLocation + '\'' +
                ", dropOffLocation='" + dropOffLocation + '\'' +
                ", includeDriver=" + includeDriver +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                '}';
    }
}

package com.mzp.carrental.entity.Rent;


import com.mzp.carrental.entity.Cars.Car;
import com.mzp.carrental.entity.Users.Customer;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Rent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false) // Each Rent corresponds to one Order
    @JoinColumn(name = "order_id", nullable = false)
    private RentalOrder rentalOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentStatus rentStatus;

    public enum RentStatus {
        NOT_STARTED, ONGOING, COMPLETED
    }

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private boolean includeDriver;

    @Column(nullable = false)
    private double driverFee;

    @Column(nullable = false)
    private double totalPrice;

    @Column(nullable = false)
    private String customerPhoneNumber;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    // getters and setters


    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RentalOrder getRentalOrder() {
        return rentalOrder;
    }

    public void setRentalOrder(RentalOrder rentalOrder) {
        this.rentalOrder = rentalOrder;
    }

    public RentStatus getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(RentStatus rentStatus) {
        this.rentStatus = rentStatus;
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

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isIncludeDriver() {
        return includeDriver;
    }

    public void setIncludeDriver(boolean includeDriver) {
        this.includeDriver = includeDriver;
    }

    public double getDriverFee() {
        return driverFee;
    }

    public void setDriverFee(double driverFee) {
        this.driverFee = driverFee;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    @Override
    public String toString() {
        return "Rent{" +
                "id=" + id +
                ", order=" + rentalOrder +
                ", rentStatus=" + rentStatus +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", car=" + car +
                ", customer=" + customer +
                ", includeDriver=" + includeDriver +
                ", driverFee=" + driverFee +
                ", totalPrice=" + totalPrice +
                '}';
    }
}

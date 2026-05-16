package com.mzp.carrental.service.admin;


import com.mzp.carrental.entity.Cars.Car;
import com.mzp.carrental.entity.Users.Agency;
import com.mzp.carrental.repository.Car.CarRepo;
import com.mzp.carrental.repository.Customer.CustomerRepo;
import com.mzp.carrental.repository.agency.AgencyRepo;
import com.mzp.carrental.repository.rent.RentRepo;
import com.mzp.carrental.repository.rent.RentalOrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private  AgencyRepo agencyRepo;
    @Autowired
    private RentalOrderRepo orderRepo;
    @Autowired
    private RentRepo rentRepo;

    @Autowired
    private CarRepo carRepo;

    @Autowired
    private CustomerRepo customerRepo;

    public List<Map<String, Object>> getAgencyStats() {
        return agencyRepo.findAll().stream().map(agency -> {
            Map<String, Object> stats = new HashMap<>();
            stats.put("agencyId", agency.getId());
            stats.put("agencyName", agency.getUsername());
            stats.put("totalOrders", orderRepo.findByAgencyId(agency.getId()).size());
            stats.put("totalRents", rentRepo.findByAgencyId(agency.getId()).size());
            return stats;
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getCustomerStats() {
        return customerRepo.findAll().stream().map(customer -> {
            Map<String, Object> stats = new HashMap<>();
            stats.put("customerId", customer.getId());
            stats.put("customerName", customer.getUsername());
            stats.put("totalOrders", orderRepo.findByCustomer_Id(customer.getId()).size());
            stats.put("totalRents", rentRepo.findByCustomerId(customer.getId()).size());
            return stats;
        }).collect(Collectors.toList());
    }

    public double getTotalRevenue(){
        double totalRevenue = agencyRepo.countTotalRevenue();
        return totalRevenue;
    }


    public List<Car> getTopRentedCars(Integer numOfCars) {
        return carRepo.findTopRentedCars(numOfCars);
    }

}

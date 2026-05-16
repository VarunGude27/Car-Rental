package com.mzp.carrental.service;


import com.mzp.carrental.repository.Car.CarRepo;
import com.mzp.carrental.repository.Customer.CustomerRepo;
import com.mzp.carrental.repository.agency.AgencyRepo;
import com.mzp.carrental.repository.rent.RentRepo;
import com.mzp.carrental.repository.rent.RentalOrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticsService {

    @Autowired
    private CarRepo carRepo;

    @Autowired
    private RentRepo rentRepo;

    @Autowired
    private RentalOrderRepo rentalOrderRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private AgencyRepo agencyRepo;



    public Map<String, Long> getStatistics() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalCars", carRepo.count());
        stats.put("totalRents", rentRepo.count());
        stats.put("totalRentalOrders", rentalOrderRepo.count());
        stats.put("totalCustomers", customerRepo.count());
        stats.put("totalAgencies", agencyRepo.count());
        return stats;
    }




}

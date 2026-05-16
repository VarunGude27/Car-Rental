package com.mzp.carrental.controller.admin;

import com.mzp.carrental.entity.Cars.Car;
import com.mzp.carrental.service.admin.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/stats")
    public List<Map<String, Object>> getAgencyStats() {
        return adminService.getAgencyStats();
    }

    // api for customer list to verify add pagination to 10, add verification status of pending as param

    // api for agency list to verify add pagination to 10, add verification status of pending as param


    @GetMapping("/customer/stats")
    public List<Map<String, Object>> getCustomerStats() {
        return adminService.getCustomerStats();
    }

    @GetMapping("/totalRevenue")
    public double getTotalRevenue() {
        return adminService.getTotalRevenue();
    }

    @GetMapping("/top-rented-cars/{numOfCars}")
    public ResponseEntity<List<Car>> getTopRentedCars(@PathVariable Integer numOfCars) {
        try {
            List<Car> cars = adminService.getTopRentedCars(numOfCars);
            return cars.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                    : ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}


package com.mzp.carrental.controller.rent;


import com.mzp.carrental.dto.RentDTO;
import com.mzp.carrental.dto.RentalOrderDTO;
import com.mzp.carrental.entity.Rent.Rent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mzp.carrental.service.rent.RentService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rents")
public class RentController {

    @Autowired
    private RentService rentService;



    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateRentStatus(@PathVariable Long id, @RequestParam Rent.RentStatus status) {
        try {
            rentService.updateOrderStatus(id, status);
            return ResponseEntity.ok("Order status updated!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rent> getRentById(@PathVariable Long id) {
        Rent rent = rentService.getRentById(id); // Add a method to fetch Rent by ID
        return ResponseEntity.ok(rent);
    }

    @GetMapping("/{carId}/rents")
    public ResponseEntity<List<RentDTO>> getRentByCar(@PathVariable Long carId) {
        List<RentDTO> rents = rentService.getFilteredRentsByCar(carId);
        return ResponseEntity.ok(rents);
    }
    @GetMapping("/customer")
    public ResponseEntity<List<RentDTO>> getRentByCustomer() {
        List<RentDTO> rents = rentService.getFilteredRentsByCustomer();
        return ResponseEntity.ok(rents);
    }

    @GetMapping("/agency")
    public ResponseEntity<List<RentDTO>> getRentByAgency() {
        List<RentDTO> rents = rentService.getFilteredRentsByAgency();
        return ResponseEntity.ok(rents);
    }

    @GetMapping("/{carId}/unavailable-dates")
    public ResponseEntity<List<LocalDate>> getUnavailableDates(@PathVariable Long carId) {
        // Collect unavailable dates (based on the start and end date of each order)
        List<LocalDate> unavailableDates = rentService.getUnavailableDatesByCar(carId);
        return ResponseEntity.ok(unavailableDates);
    }
}




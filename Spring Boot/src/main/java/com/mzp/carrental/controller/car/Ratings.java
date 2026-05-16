package com.mzp.carrental.controller.car;

import com.mzp.carrental.entity.Cars.Car;
import com.mzp.carrental.entity.OurUsers;
import com.mzp.carrental.entity.Users.Customer;
import com.mzp.carrental.repository.Customer.CustomerRepo;
import com.mzp.carrental.repository.UsersRepo;
import com.mzp.carrental.repository.rent.RentRepo;
import com.mzp.carrental.service.OurUserDetailsService;
import com.mzp.carrental.service.car.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/ratings/cars")
public class Ratings {
    @Autowired
    private CarService carService;

    @Autowired
    private OurUserDetailsService userDetailsService;

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private RentRepo rentRepo;

    @PostMapping("/{carId}/rate")
    public ResponseEntity<?> rateCar(@PathVariable Long carId, @RequestParam Double rating) {
        try {
            String email = userDetailsService.getCurrentUserEmail();
            Optional<OurUsers> user = usersRepo.findByEmail(email);

            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
            }

            // Get the customer entity
            Optional<Customer> customer = customerRepo.findByOurUsers(user.get());
            if (customer.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only customers can rate cars.");
            }

            // Ensure the customer has rented the car
            boolean hasRented = rentRepo.existsByCustomerIdAndCarId(customer.get().getId(), carId);
            if (!hasRented) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only rate cars that you have rented.");
            }

            Car updatedCar = carService.rateCar(email, carId, rating);
            return ResponseEntity.ok(updatedCar);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error rating car.");
        }
    }
}

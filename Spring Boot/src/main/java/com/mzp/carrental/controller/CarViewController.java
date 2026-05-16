package com.mzp.carrental.controller;

import com.mzp.carrental.dto.AgencyDTO;
import com.mzp.carrental.entity.Cars.Car;
import com.mzp.carrental.entity.Users.Agency;
import com.mzp.carrental.repository.Car.CarRepo;
import com.mzp.carrental.service.car.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/view/cars")
public class CarViewController {

    @Autowired
    private CarService carService;

    @Autowired
    private CarRepo carRepo;

    // Route to get all cars
    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        List<Car> cars = carService.getAllCars();
        return cars.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(cars);
    }

    // Route to get a car by ID
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        return car != null
                ? ResponseEntity.ok(car)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/test/{id}")
    public ResponseEntity<Car> getCarForTest(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        System.out.println("Inside /test/id/");
        if (car != null) {
            System.out.println("not found test cars");
            System.out.println(car.toString());
            return ResponseEntity.ok(car);
        } else {
            // Create a test car object with mock data
            Car testCar = new Car();
            testCar.setId(1L); // Example ID
            testCar.setBrand("Test Brand");
            testCar.setModel("Test Model");
            testCar.setLicensePlate("TEST123");
            testCar.setYear(2022);
            testCar.setVin("1HGCM82633A123456"); // Example VIN
            testCar.setMileage(1000);
            testCar.setColor("Red");
            testCar.setCategory(Car.Category.SUV);
            testCar.setFuelType(Car.FuelType.PETROL);
            testCar.setTransmission(Car.Transmission.AUTOMATIC);
            testCar.setSeats(5);
            testCar.setFeatures("Air Conditioning Navigation System");
            testCar.setDescription("This is a test car.");
            testCar.setPricePerDay(50.0);


            System.out.println("Responsed Test cars");
            return ResponseEntity.ok(testCar);
        }
    }


    // Route to filter cars
    @GetMapping("/filter")
    public ResponseEntity<List<Car>> filterCars(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Integer seats,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String fuelType) {
        List<Car> cars = carService.filterCars(brand, model, seats, category, fuelType);
        return cars.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getCarImage(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        if (car != null && car.getImageData() != null) {
            System.out.println("Image of  car id="+ car.getId() + " is responsed");
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(car.getImageType())) // Dynamically set content type
                    .body(car.getImageData());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{id}/firstImage")
    public ResponseEntity<byte[]> getFirstCarImage(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        if (car != null && car.getFirstImageData() != null) {
            System.out.println("Returning first image for car ID=" + car.getId());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(car.getFirstImageType())) // Set correct content type
                    .body(car.getFirstImageData());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // No image found
    }


    @GetMapping("/{id}/secondImage")
    public ResponseEntity<byte[]> getSecondCarImage(@PathVariable Long id) {
        System.out.println("Get Second Image is called");
        Car car = carService.getCarById(id);
        if (car != null && car.getSecondImageData() != null) {
            System.out.println("Returning second image for car ID=" + car.getId());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(car.getSecondImageType()))
                    .body(car.getSecondImageData());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    @GetMapping("/{id}/thirdImage")
    public ResponseEntity<byte[]> getThirdCarImage(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        if (car != null && car.getThirdImageData() != null) {
            System.out.println("Returning third image for car ID=" + car.getId());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(car.getThirdImageType()))
                    .body(car.getThirdImageData());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{id}/agency")
    public ResponseEntity<AgencyDTO> getAgencyFByCar(@PathVariable Long id) {
        AgencyDTO agency = carService.getAgencyByCar(id);
        System.out.println(agency.toString());
        if (agency != null ) {
            System.out.println("Agency of  car id="+ id + " is responsed");
            return ResponseEntity.ok()
                    .body(agency);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @GetMapping("/available")
    public ResponseEntity<List<Car>> getAvailableCars() {
        List<Car> cars = carRepo.findByAvailableTrue();
        return cars.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(cars);
    }


}

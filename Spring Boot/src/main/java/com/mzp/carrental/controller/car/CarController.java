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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agency/cars")
public class CarController {

    @Autowired
    private CarService carService;


    // Get all cars for the current agency
    @GetMapping
    public ResponseEntity<List<Car>> getAllCarsForAgency() {
        List<Car> cars = carService.getAllCarsForCurrentAgency();
        return cars.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(cars);
    }

    // Get a specific car by ID for the current agency
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Car car = carService.getCarByIdForCurrentAgency(id);
//        System.out.println("/agency/cars/:id is called" + car.toString());
        return car != null
                ? ResponseEntity.ok(car)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Car> createCar(
            @RequestParam("brand") String brand,
            @RequestParam("model") String model,
            @RequestParam("year") int year,
            @RequestParam("licensePlate") String licensePlate,
            @RequestParam("vin") String vin,
            @RequestParam("mileage") int mileage,
            @RequestParam("color") String color,
            @RequestParam("category") Car.Category category,
            @RequestParam("fuelType") Car.FuelType fuelType,
            @RequestParam("transmission") Car.Transmission transmission,
            @RequestParam("seats") int seats,
            @RequestParam("features") String features,
            @RequestParam("description") String description,
            @RequestParam("pricePerDay") double pricePerDay,
            @RequestParam("driverFeePerDay") double driverFeePerDay,
            @RequestParam("imageFile") MultipartFile image,
            @RequestParam(value = "firstImage", required = false) MultipartFile firstImage,
            @RequestParam(value = "secondImage", required = false) MultipartFile secondImage,
            @RequestParam(value = "thirdImage", required = false) MultipartFile thirdImage
    ) throws IOException {

        Car car = new Car();
        car.setBrand(brand);
        car.setModel(model);
        car.setYear(year);
        car.setLicensePlate(licensePlate);
        car.setVin(vin);
        car.setMileage(mileage);
        car.setColor(color);
        car.setCategory(category);
        car.setFuelType(fuelType);
        car.setTransmission(transmission);
        car.setSeats(seats);
        car.setDescription(description);
        car.setPricePerDay(pricePerDay);
        car.setDriverFeePerDay(driverFeePerDay);
        car.setFeatures(features);
        System.out.println("Setting car data is done");


        // Handle image file
        if (image != null) {
            String imageName = image.getOriginalFilename();
            String imageType = image.getContentType();
            byte[] imageBytes = image.getBytes();

            // Set the image details to the Product object
            car.setImageName(imageName);
            car.setImageType(imageType);
            car.setImageData(imageBytes);
        }
        // Handle additional optional images
        if (firstImage != null && !firstImage.isEmpty()) {
            car.setFirstImageName(firstImage.getOriginalFilename());
            car.setFirstImageType(firstImage.getContentType());
            car.setFirstImageData(firstImage.getBytes());
        }
        if (secondImage != null && !secondImage.isEmpty()) {
            car.setSecondImageName(secondImage.getOriginalFilename());
            car.setSecondImageType(secondImage.getContentType());
            car.setSecondImageData(secondImage.getBytes());
        }
        if (thirdImage != null && !thirdImage.isEmpty()) {
            car.setThirdImageName(thirdImage.getOriginalFilename());
            car.setThirdImageType(thirdImage.getContentType());
            car.setThirdImageData(thirdImage.getBytes());
        }

        // Optionally log the received product data
        System.out.println("Car received: " + car.toString());


        // Save the product using your service method
        Car savedCar = carService.createCarForCurrentAgency(car, image, firstImage, secondImage, thirdImage);  // Make sure service handles the image correctly

        // Return the created product in the response
        return new ResponseEntity<>(savedCar, HttpStatus.CREATED);
    }



    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Car> updateCar(
            @PathVariable Long id,
            @RequestParam("brand") String brand,
            @RequestParam("model") String model,
            @RequestParam("year") int year,
            @RequestParam("licensePlate") String licensePlate,
            @RequestParam("vin") String vin,
            @RequestParam("mileage") int mileage,
            @RequestParam("color") String color,
            @RequestParam("category") Car.Category category,
            @RequestParam("fuelType") Car.FuelType fuelType,
            @RequestParam("transmission") Car.Transmission transmission,
            @RequestParam("seats") int seats,
            @RequestParam("features") String features,
            @RequestParam("description") String description,
            @RequestParam("pricePerDay") double pricePerDay,
            @RequestParam("driverFeePerDay") double driverFeePerDay,
            @RequestParam("available") boolean available,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "firstImage", required = false) MultipartFile firstImage,
            @RequestParam(value = "secondImage", required = false) MultipartFile secondImage,
            @RequestParam(value = "thirdImage", required = false) MultipartFile thirdImage)

            throws IOException {

        Car updatedCar = carService.updateCarWithImage(id, brand, model, year, licensePlate, vin, mileage, color, category, fuelType, transmission, seats, features, description, pricePerDay, driverFeePerDay, available, imageFile, firstImage, secondImage, thirdImage);
        return updatedCar != null ? ResponseEntity.ok(updatedCar) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    // Delete a car for the current agency
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        boolean isDeleted = carService.deleteCarForCurrentAgency(id);
        return isDeleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }



    @PutMapping("/{id}/availability")
    public ResponseEntity<Car> updateCarAvailability(@PathVariable Long id, @RequestParam boolean available) {
        Car updatedCar = carService.updateCarAvailability(id, available);
        return ResponseEntity.ok(updatedCar);
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<List<Car>> getAllCarsByAgencyId(@PathVariable Integer id) {
        try {
            List<Car> cars = carService.getAllCarsByAgencyId(id);
            return cars.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                    : ResponseEntity.ok(cars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}

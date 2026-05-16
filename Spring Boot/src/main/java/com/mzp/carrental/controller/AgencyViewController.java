package com.mzp.carrental.controller;

import com.mzp.carrental.dto.AgencyDTO;
import com.mzp.carrental.entity.Cars.Car;
import com.mzp.carrental.entity.Users.Agency;
import com.mzp.carrental.service.agency.AgencyService;
import com.mzp.carrental.service.car.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/view/agencies")
public class AgencyViewController {

    @Autowired
    private AgencyService agencyService;

    @Autowired
    private CarService carService;

    @GetMapping("/{id}")
    public ResponseEntity<AgencyDTO> getAgencyById(@PathVariable Integer id) {
        System.out.println("Inside get agency");
        Agency agency = agencyService.getAgencyById(id);
        System.out.println("And here is " + agency.toString());

        AgencyDTO agencyDTO;
        agencyDTO = new AgencyDTO(agency.getId(),agency.getOurUsers().getEmail(),agency.getUsername(),agency.getPhoneNumber(),agency.getAddress(),agency.getCity(),agency.getCars().size(), agency.getVerificationStatus().toString());
        return agencyDTO != null
                ? ResponseEntity.ok(agencyDTO)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }



    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getAgencyImage(@PathVariable Integer id) {
        // Fetch the agency image from the service
        byte[] imageData = agencyService.getAgencyImageById(id);
        if (imageData != null) {
            // Retrieve the image type (e.g., "image/jpeg") from the service
            String imageType = agencyService.getAgencyImageTypeById(id);

            // Return the image with the appropriate content type
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(imageType)) // Dynamically set content type
                    .body(imageData);
        }

        // If no image is found, return a 404 NOT_FOUND response
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{id}/cars")
    public ResponseEntity<List<Car>> getAllCarsForAgency(@PathVariable Long id) {
        List<Car> cars = carService.getAllCarsForAgency(id);
        return cars.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(cars);
    }
}
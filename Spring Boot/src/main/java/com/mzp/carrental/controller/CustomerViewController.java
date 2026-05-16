package com.mzp.carrental.controller;

import com.mzp.carrental.service.Customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/view/customers")
public class CustomerViewController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getAgencyImage(@PathVariable Integer id) {
        // Fetch the customer image from the service
        byte[] imageData = customerService.getCustomerImageById(id);

        if (imageData != null) {
            // Retrieve the image type (e.g., "image/jpeg") from the service
            String imageType = customerService.getCustomerImageTypeById(id);

            // Return the image with the appropriate content type
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(imageType)) // Dynamically set content type
                    .body(imageData);
        }

        // If no image is found, return a 404 NOT_FOUND response
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}


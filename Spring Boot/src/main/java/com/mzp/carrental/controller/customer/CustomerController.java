package com.mzp.carrental.controller.customer;

import com.mzp.carrental.entity.Users.Customer;
import com.mzp.carrental.service.Customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Get all customers
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return customers.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(customers);
    }

    // Get a specific customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id) {
        Customer customer = customerService.getCustomerById(id);
        return customer != null
                ? ResponseEntity.ok(customer)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Create a new customer
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer createdCustomer = customerService.createCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

//    // Update an existing customer
//    @PutMapping("/{id}")
//    public ResponseEntity<Customer> updateCustomer(@PathVariable Integer id, @RequestBody Customer customer) {
//        Customer updatedCustomer = customerService.updateCustomer(id, customer);
//        return updatedCustomer != null
//                ? ResponseEntity.ok(updatedCustomer)
//                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable Integer id,
            @RequestParam("username") String username,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("city") String city,
            @RequestParam("drivingLiscene") String drivingLiscene,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        Customer customerDetails = new Customer();
        customerDetails.setUsername(username);
        customerDetails.setPhoneNumber(phoneNumber);
        customerDetails.setCity(city);
        customerDetails.setDrivingLiscene(drivingLiscene);

        Customer updatedCustomer = customerService.updateCustomer(id, customerDetails, image);
        return updatedCustomer != null
                ? ResponseEntity.ok(updatedCustomer)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Delete a customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer id) {
        boolean isDeleted = customerService.deleteCustomer(id);
        return isDeleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // upload to admin /{id}/upload-to-verify
    // function here {} - id, nrc- string, NRC photo Front, Nrc Photo Back, verifiaction status to pending
}

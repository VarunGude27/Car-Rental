package com.mzp.carrental.service.Customer;

import com.mzp.carrental.entity.Users.Agency;
import com.mzp.carrental.entity.Users.Customer;
import com.mzp.carrental.repository.Customer.CustomerRepo;
import com.mzp.carrental.service.OurUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private OurUserDetailsService userDetailsService;

    // Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    // Get a customer by ID
    public Customer getCustomerById(Integer id) {
        return customerRepo.findById(id).orElse(null);
    }

    // Create a new customer
    public Customer createCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

//    // Update an existing customer
//    public Customer updateCustomer(Integer id, Customer customerDetails) {
//        return customerRepo.findById(id)
//                .map(existingCustomer -> {
//                    existingCustomer.setUsername(customerDetails.getUsername());
//                    existingCustomer.setPhoneNumber(customerDetails.getPhoneNumber());
//                    existingCustomer.setCity(customerDetails.getCity());
//                    existingCustomer.setDrivingLiscene(customerDetails.getDrivingLiscene());
//                    return customerRepo.save(existingCustomer);
//                }).orElse(null);
//    }
        public Customer updateCustomer(Integer id, Customer customerDetails, MultipartFile image) throws IOException {
            return customerRepo.findById(id).map(existingCustomer -> {
                existingCustomer.setUsername(customerDetails.getUsername());
                existingCustomer.setPhoneNumber(customerDetails.getPhoneNumber());
                existingCustomer.setCity(customerDetails.getCity());
                existingCustomer.setDrivingLiscene(customerDetails.getDrivingLiscene());

                if (image != null) {
                    existingCustomer.setImageName(image.getOriginalFilename());
                    existingCustomer.setImageType(image.getContentType());
                    try {
                        existingCustomer.setImageData(image.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                return customerRepo.save(existingCustomer);
            }).orElse(null);
        }

    // Delete a customer
    public boolean deleteCustomer(Integer id) {
        if (customerRepo.existsById(id)) {
            customerRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public Customer getLoggedInCustomer() {
        String email = userDetailsService.getCurrentUserEmail();
        return customerRepo.findByOurUsers_Email(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    // -----------customer view
    public byte[] getCustomerImageById(Integer id) {
        Optional<Customer> customerOptional = customerRepo.findById(id);
        return customerOptional.map(Customer::getImageData).orElse(null);
    }
    public String getCustomerImageTypeById(Integer id) {
        Optional<Customer> customerOptional = customerRepo.findById(id);
        return customerOptional.map(Customer::getImageType).orElse(null);
    }
}

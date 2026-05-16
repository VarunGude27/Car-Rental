package com.mzp.carrental.service.Customer;

import com.mzp.carrental.dto.CustomerVerificationDTO;
import com.mzp.carrental.entity.Users.Customer;
import com.mzp.carrental.entity.Verification.CustomerVerification;
import com.mzp.carrental.repository.Customer.CustomerRepo;
import com.mzp.carrental.repository.Customer.CustomerVerificationRepo;
import com.mzp.carrental.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class CustomerVerificationService {

    @Autowired
    private CustomerVerificationRepo customerVerificationRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private UsersRepo usersRepo;

    // ✅ Upload or Update Customer Verification Data
    public CustomerVerification uploadCustomerVerification(
            Integer customerId, String nrc, MultipartFile nrcPhotoFront, MultipartFile nrcPhotoBack) throws IOException {

        Customer customer = customerRepo.findByOurUsersId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Optional<CustomerVerification> verificationOpt = customerVerificationRepo.findByCustomerId(customerId);
        CustomerVerification verification;

        if (verificationOpt.isPresent()) {
            // Update existing verification
            verification = verificationOpt.get();
            System.out.println("Updating existing verification for customerId=" + customerId);

            // ❌ Prevent modifying VERIFIED data
            if (verification.getVerificationStatus() == CustomerVerification.VerificationStatus.VERIFIED) {
                throw new RuntimeException("Cannot modify verified verification data.");
            }
        } else {
            // Create new verification
            verification = new CustomerVerification();
            verification.setCustomer(customer); // Link to Customer entity (sets customer_id as PK)
            System.out.println("Creating new verification for customerId=" + customerId);
        }

        // Set or update fields
        verification.setNrc(nrc);

        if (nrcPhotoFront != null && !nrcPhotoFront.isEmpty()) {
            verification.setNrcPhotoFront(nrcPhotoFront.getOriginalFilename());
            verification.setNrcPhotoFrontData(nrcPhotoFront.getBytes());
        }

        if (nrcPhotoBack != null && !nrcPhotoBack.isEmpty()) {
            verification.setNrcPhotoBack(nrcPhotoBack.getOriginalFilename());
            verification.setNrcPhotoBackData(nrcPhotoBack.getBytes());
        }

        verification.setVerificationStatus(CustomerVerification.VerificationStatus.PENDING);
        customer.setVerificationStatus(Customer.VerificationStatus.PENDING); // Assuming Customer has this field

        customerRepo.save(customer); // Save customer to update its verificationStatus
        return customerVerificationRepo.save(verification); // Save verification (customer_id matches customer's id)
    }

    // ✅ Fetch paginated list of PENDING verifications for the admin
    public Page<CustomerVerificationDTO> getPendingVerifications(Pageable pageable) {
        Page<Customer> customers = customerRepo.findByVerificationStatus(Customer.VerificationStatus.PENDING, pageable);
        return customers.map(customer -> {
            CustomerVerification verification = customerVerificationRepo.findByCustomerId(customer.getId())
                    .orElseThrow(() -> new RuntimeException("Customer verification not found"));
            return new CustomerVerificationDTO(customer, verification);
        });
    }

    // ✅ Fetch a single customer’s verification details
    public CustomerVerificationDTO getCustomerVerification(Integer customerId) {
        Customer customer = customerRepo.findByOurUsersId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        CustomerVerification verification = customerVerificationRepo.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer verification not found"));

        return new CustomerVerificationDTO(customer, verification);
    }

    // ✅ Update the verification status (Admin Action)
    public void updateVerificationStatus(Integer customerId, CustomerVerification.VerificationStatus newStatus) {
        CustomerVerification verification = customerVerificationRepo.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer verification not found"));

        // ❌ Prevent changing status if already VERIFIED
        if (verification.getVerificationStatus() == CustomerVerification.VerificationStatus.VERIFIED) {
            throw new RuntimeException("Cannot change status. This verification is already VERIFIED.");
        }

        verification.setVerificationStatus(newStatus);

        // if status is verified, also change the Verification status inside Customer Entity
        if (newStatus == CustomerVerification.VerificationStatus.VERIFIED) {
            Customer customer = customerRepo.findByOurUsersId(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            //System.out.println("Customer Status inside custmer entity is " + customer.getVerificationStatus().toString());
            customer.setVerificationStatus(Customer.VerificationStatus.VERIFIED); // Assuming Customer has this field
            customerRepo.save(customer);
        }

        customerVerificationRepo.save(verification);
    }

    // ✅ Fetch verifications by status
    public Page<CustomerVerificationDTO> getVerificationsByStatus(CustomerVerification.VerificationStatus status, Pageable pageable) {
        Page<CustomerVerification> verifications = customerVerificationRepo.findByVerificationStatus(status, pageable);
        return verifications.map(verification -> {
            Customer customer = customerRepo.findById(verification.getCustomer().getId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            return new CustomerVerificationDTO(customer, verification);
        });
    }

    // ✅ Fetch verifications with no status (Blank)
    public Page<CustomerVerificationDTO> getVerificationsWithNoStatus(Pageable pageable) {
        Page<Customer> customers = customerRepo.findByVerificationStatus(null, pageable);
        return customers.map(customer -> {
            CustomerVerification verification = customerVerificationRepo.findByCustomerId(customer.getId())
                    .orElseThrow(() -> new RuntimeException("Customer verification not found"));
            return new CustomerVerificationDTO(customer, verification);
        });
    }
}
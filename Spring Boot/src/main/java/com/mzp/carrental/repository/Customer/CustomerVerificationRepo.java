package com.mzp.carrental.repository.Customer;

import com.mzp.carrental.entity.Verification.CustomerVerification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerVerificationRepo extends JpaRepository<CustomerVerification, Integer> {
    Optional<CustomerVerification> findByCustomerId(Integer customerId);


    // ✅ New method to fetch verifications by status with pagination
    Page<CustomerVerification> findByVerificationStatus(CustomerVerification.VerificationStatus status, Pageable pageable);

}
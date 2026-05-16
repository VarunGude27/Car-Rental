package com.mzp.carrental.repository.Customer;

import com.mzp.carrental.entity.OurUsers;
import com.mzp.carrental.entity.Users.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    // Example custom query: Find a customer by username
    Optional<Customer> findByUsername(String username);

    Optional<Customer> findByOurUsers_Email(String email);

    Optional<Customer> findByOurUsers(OurUsers ourUsers);

    // Below quary for deleting a user
    Optional<Customer> findByOurUsersId(Integer userId);

    /// ✅ Fetch customers by verification status
    Page<Customer> findByVerificationStatus(Customer.VerificationStatus status, Pageable pageable);

    // ✅ Fetch customers where verification status is NULL (BLANK status)
    Page<Customer> findByVerificationStatusIsNull(Pageable pageable);
}

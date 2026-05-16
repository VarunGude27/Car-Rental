package com.mzp.carrental.repository.agency;


import com.mzp.carrental.dto.AgencyDTO;
import com.mzp.carrental.entity.Users.Agency;
import com.mzp.carrental.entity.Verification.AgencyVerification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AgencyRepo extends JpaRepository<Agency, Integer> {

    // Find an agency by the email of the associated user
    Optional<Agency> findByOurUsers_Email(String email);

    // Below quary for deleting a user
    Optional<Agency> findByOurUsersId(Integer userId);

    // ✅ Fetch agencies by verification status
    Page<Agency> findByVerificationStatus(AgencyVerification.VerificationStatus status, Pageable pageable);

    @Query("SELECT a FROM Agency a ORDER BY a.totalRevenue DESC")
    List<AgencyDTO> findTop10MostRevenueAgencies();

    @Query("SELECT SUM(a.totalRevenue) FROM Agency a")
    Double countTotalRevenue();
}

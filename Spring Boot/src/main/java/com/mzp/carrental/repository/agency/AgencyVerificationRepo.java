package com.mzp.carrental.repository.agency;

import com.mzp.carrental.entity.Verification.AgencyVerification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgencyVerificationRepo extends JpaRepository<AgencyVerification, Integer> {
    Optional<AgencyVerification> findByAgencyId(Integer agencyId);

    // ✅ Fetch agencies by verification status
    Page<AgencyVerification> findByVerificationStatus(AgencyVerification.VerificationStatus status, Pageable pageable);

    // ✅ Fetch agencies with no verification status (Blank)
    Page<AgencyVerification> findByVerificationStatusIsNull(Pageable pageable);
}
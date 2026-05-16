package com.mzp.carrental.service.agency;

import com.mzp.carrental.dto.AgencyVerificationDTO;
import com.mzp.carrental.entity.Users.Agency;
import com.mzp.carrental.entity.Verification.AgencyVerification;
import com.mzp.carrental.repository.UsersRepo;
import com.mzp.carrental.repository.agency.AgencyRepo;
import com.mzp.carrental.repository.agency.AgencyVerificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class AgencyVerificationService {
    @Autowired
    private AgencyVerificationRepo agencyVerificationRepo;

    @Autowired
    private AgencyRepo agencyRepo;

    @Autowired
    private UsersRepo usersRepo;

    public AgencyVerification uploadAgencyVerification(
            Integer agencyId, String nrc, MultipartFile nrcPhotoFront, MultipartFile nrcPhotoBack,
            MultipartFile agencyLicenseFront, MultipartFile agencyLicenseBack) throws IOException {

        Agency agency = agencyRepo.findByOurUsersId(agencyId)
                .orElseThrow(() -> new RuntimeException("Agency not found"));

        Optional<AgencyVerification> verificationOpt = agencyVerificationRepo.findByAgencyId(agencyId);
        AgencyVerification verification;

        if (verificationOpt.isPresent()) {
            verification = verificationOpt.get();
            System.out.println("Updating existing verification for agencyId=" + agencyId);
            if (verification.getVerificationStatus() == AgencyVerification.VerificationStatus.VERIFIED) {
                throw new RuntimeException("Cannot modify verified verification data.");
            }
        } else {
            verification = new AgencyVerification();
            verification.setAgency(agency); // Link to Agency entity
            System.out.println("Creating new verification for agencyId=" + agencyId);
        }

        verification.setNrc(nrc);
        if (nrcPhotoFront != null && !nrcPhotoFront.isEmpty()) {
            verification.setNrcPhotoFront(nrcPhotoFront.getOriginalFilename());
            verification.setNrcPhotoFrontData(nrcPhotoFront.getBytes());
        }
        if (nrcPhotoBack != null && !nrcPhotoBack.isEmpty()) {
            verification.setNrcPhotoBack(nrcPhotoBack.getOriginalFilename());
            verification.setNrcPhotoBackData(nrcPhotoBack.getBytes());
        }
        if (agencyLicenseFront != null && !agencyLicenseFront.isEmpty()) {
            verification.setAgencyLicenseFront(agencyLicenseFront.getOriginalFilename());
            verification.setAgencyLicenseFrontData(agencyLicenseFront.getBytes());
        }
        if (agencyLicenseBack != null && !agencyLicenseBack.isEmpty()) {
            verification.setAgencyLicenseBack(agencyLicenseBack.getOriginalFilename());
            verification.setAgencyLicenseBackData(agencyLicenseBack.getBytes());
        }

        verification.setVerificationStatus(AgencyVerification.VerificationStatus.PENDING);
        agency.setVerificationStatus(Agency.VerificationStatus.PENDING); // Update agency status
        agencyRepo.save(agency); // Save the updated agency

        return agencyVerificationRepo.save(verification);
    }

    public AgencyVerificationDTO getAgencyVerification(Integer agencyId) {
        Agency agency = agencyRepo.findByOurUsersId(agencyId)
                .orElseThrow(() -> new RuntimeException("Agency not found"));
        AgencyVerification verification = agencyVerificationRepo.findByAgencyId(agencyId)
                .orElseThrow(() -> new RuntimeException("Agency verification not found"));
        return new AgencyVerificationDTO(agency, verification);
    }

    // Admin features
    public Page<AgencyVerificationDTO> getVerificationsByStatus(AgencyVerification.VerificationStatus status, Pageable pageable) {
        Page<AgencyVerification> verifications = agencyVerificationRepo.findByVerificationStatus(status, pageable);
        return verifications.map(verification -> {
            Agency agency = agencyRepo.findByOurUsersId(verification.getAgency().getId())
                    .orElseThrow(() -> new RuntimeException("Agency not found"));
            return new AgencyVerificationDTO(agency, verification);
        });
    }

    public void updateVerificationStatus(Integer agencyId, AgencyVerification.VerificationStatus newStatus) {
        AgencyVerification verification = agencyVerificationRepo.findByAgencyId(agencyId)
                .orElseThrow(() -> new RuntimeException("Agency verification not found"));

        if (verification.getVerificationStatus() == AgencyVerification.VerificationStatus.VERIFIED) {
            throw new RuntimeException("Cannot change status. This verification is already VERIFIED.");
        }

        verification.setVerificationStatus(newStatus);
        agencyVerificationRepo.save(verification);

        // If status is VERIFIED, also update the Verification status in the Agency entity
        if (newStatus == AgencyVerification.VerificationStatus.VERIFIED) {
            Agency agency = agencyRepo.findByOurUsersId(agencyId)
                    .orElseThrow(() -> new RuntimeException("Agency not found"));
            agency.setVerificationStatus(Agency.VerificationStatus.VERIFIED); // Assuming Agency has this field
            agencyRepo.save(agency);
        }
    }

    public Page<AgencyVerificationDTO> getPendingVerifications(Pageable pageable) {
        Page<AgencyVerification> verifications = agencyVerificationRepo.findByVerificationStatus(AgencyVerification.VerificationStatus.PENDING, pageable);
        return verifications.map(verification -> {
            Agency agency = agencyRepo.findByOurUsersId(verification.getAgency().getId())
                    .orElseThrow(() -> new RuntimeException("Agency not found"));
            return new AgencyVerificationDTO(agency, verification);
        });
    }

    public Page<AgencyVerificationDTO> getVerificationsWithNoStatus(Pageable pageable) {
        Page<AgencyVerification> verifications = agencyVerificationRepo.findByVerificationStatusIsNull(pageable);
        return verifications.map(verification -> {
            Agency agency = agencyRepo.findByOurUsersId(verification.getAgency().getId())
                    .orElseThrow(() -> new RuntimeException("Agency not found"));
            return new AgencyVerificationDTO(agency, verification);
        });
    }

    public Page<AgencyVerificationDTO> getAllVerifications(Pageable pageable) {
        Page<AgencyVerification> verifications = agencyVerificationRepo.findAll(pageable);
        return verifications.map(verification -> {
            Agency agency = agencyRepo.findByOurUsersId(verification.getAgency().getId())
                    .orElseThrow(() -> new RuntimeException("Agency not found"));
            return new AgencyVerificationDTO(agency, verification);
        });
    }
}
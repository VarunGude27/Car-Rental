package com.mzp.carrental.controller.admin;

import com.mzp.carrental.dto.AgencyVerificationDTO;
import com.mzp.carrental.dto.CustomerVerificationDTO;
import com.mzp.carrental.entity.Verification.AgencyVerification;
import com.mzp.carrental.entity.Verification.CustomerVerification;
import com.mzp.carrental.service.Customer.CustomerVerificationService;
import com.mzp.carrental.service.agency.AgencyVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/verifications")
public class AdminVerificationController {

    @Autowired
    private CustomerVerificationService customerVerificationService;

    @Autowired
    private AgencyVerificationService agencyVerificationService;

    // Customer endpoints (unchanged, assuming they work)
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerVerificationDTO>> getPendingVerifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerVerificationDTO> verificationsPage = customerVerificationService.getPendingVerifications(pageable);
        List<CustomerVerificationDTO> verifications = verificationsPage.getContent();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(verificationsPage.getTotalElements()));

        return new ResponseEntity<>(verifications, headers, HttpStatus.OK);
    }

    @GetMapping("/customers/{customerId}")
    public ResponseEntity<?> getCustomerVerification(@PathVariable Integer customerId) {
        try {
            CustomerVerificationDTO verification = customerVerificationService.getCustomerVerification(customerId);
            return ResponseEntity.ok(verification);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer verification not found.");
        }
    }

    @PutMapping("/customers/{customerId}/status")
    public ResponseEntity<String> updateVerificationStatus(
            @PathVariable Integer customerId,
            @RequestParam String status) {
        try {
            CustomerVerification.VerificationStatus newStatus = CustomerVerification.VerificationStatus.valueOf(status.toUpperCase());
            customerVerificationService.updateVerificationStatus(customerId, newStatus);
            return ResponseEntity.ok("Verification status updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification status.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update verification status.");
        }
    }

    @GetMapping("/customers/sort")
    public ResponseEntity<List<CustomerVerificationDTO>> getSortedVerifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerVerificationDTO> verificationsPage;

        if (status == null || status.equalsIgnoreCase("ALL")) {
            verificationsPage = customerVerificationService.getPendingVerifications(pageable);
        } else if (status.equalsIgnoreCase("BLANK")) {
            verificationsPage = customerVerificationService.getVerificationsWithNoStatus(pageable);
        } else {
            try {
                CustomerVerification.VerificationStatus verificationStatus = CustomerVerification.VerificationStatus.valueOf(status.toUpperCase());
                verificationsPage = customerVerificationService.getVerificationsByStatus(verificationStatus, pageable);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(verificationsPage.getTotalElements()));

        return new ResponseEntity<>(verificationsPage.getContent(), headers, HttpStatus.OK);
    }

    // Agency endpoints (fixed)
    @GetMapping("/agencies")
    public ResponseEntity<VerificationResponse<List<AgencyVerificationDTO>>> getPaginatedAgencies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<AgencyVerificationDTO> verificationsPage = agencyVerificationService.getPendingVerifications(pageable);
            List<AgencyVerificationDTO> verifications = verificationsPage.getContent();

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(verificationsPage.getTotalElements()));
            headers.add("X-Total-Pages", String.valueOf(verificationsPage.getTotalPages()));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new VerificationResponse<>(true, "Pending agency verifications retrieved successfully", verifications));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new VerificationResponse<>(false, "Failed to retrieve agency verifications: " + e.getMessage(), null));
        }
    }

    @GetMapping("/agencies/{agencyId}")
    public ResponseEntity<VerificationResponse<AgencyVerificationDTO>> getAgencyVerification(@PathVariable Integer agencyId) {
        try {
            AgencyVerificationDTO verification = agencyVerificationService.getAgencyVerification(agencyId);
            return ResponseEntity.ok(new VerificationResponse<>(true, "Agency verification retrieved successfully", verification));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new VerificationResponse<>(false, "Agency verification not found", null));
        }
    }

    @PutMapping("/agencies/{agencyId}/status")
    public ResponseEntity<VerificationResponse<String>> updateAgencyVerificationStatus(
            @PathVariable Integer agencyId, @RequestParam String status) {
        try {
            AgencyVerification.VerificationStatus newStatus = AgencyVerification.VerificationStatus.valueOf(status.toUpperCase());
            agencyVerificationService.updateVerificationStatus(agencyId, newStatus);
            return ResponseEntity.ok(new VerificationResponse<>(true, "Agency verification status updated successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new VerificationResponse<>(false, "Invalid verification status: " + status, null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new VerificationResponse<>(false, "Failed to update verification status: " + e.getMessage(), null));
        }
    }

    @GetMapping("/agencies/sort")
    public ResponseEntity<VerificationResponse<Page<AgencyVerificationDTO>>> getSortedAgencies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<AgencyVerificationDTO> verificationsPage;

            if (status == null || status.equalsIgnoreCase("ALL")) {
                // Fetch all verifications regardless of status
                verificationsPage = agencyVerificationService.getAllVerifications(pageable);
            } else if (status.equalsIgnoreCase("BLANK")) {
                verificationsPage = agencyVerificationService.getVerificationsWithNoStatus(pageable);
            } else {
                AgencyVerification.VerificationStatus verificationStatus =
                        AgencyVerification.VerificationStatus.valueOf(status.toUpperCase());
                verificationsPage = agencyVerificationService.getVerificationsByStatus(verificationStatus, pageable);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(verificationsPage.getTotalElements()));
            headers.add("X-Total-Pages", String.valueOf(verificationsPage.getTotalPages()));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new VerificationResponse<>(true, "Sorted agency verifications retrieved successfully", verificationsPage));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new VerificationResponse<>(false, "Invalid status value: " + status, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new VerificationResponse<>(false, "Failed to retrieve sorted agency verifications: " + e.getMessage(), null));
        }
    }
}

// Generic response record for consistency
record VerificationResponse<T>(boolean success, String message, T data) {
    public VerificationResponse(boolean success, String message) {
        this(success, message, null);
    }
}
package com.mzp.carrental.controller.agency;

import com.mzp.carrental.dto.AgencyVerificationDTO;
import com.mzp.carrental.entity.Verification.AgencyVerification;
import com.mzp.carrental.service.agency.AgencyVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/agency/verification")
public class AgencyVerificationController {
    @Autowired
    private AgencyVerificationService agencyVerificationService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> uploadAgencyVerification(
            @RequestParam("agencyId") Integer agencyId,
            @RequestParam("nrc") String nrc,
            @RequestParam("nrcPhotoFront") MultipartFile nrcPhotoFront,
            @RequestParam("nrcPhotoBack") MultipartFile nrcPhotoBack,
            @RequestParam("agencyLicenseFront") MultipartFile agencyLicenseFront,
            @RequestParam("agencyLicenseBack") MultipartFile agencyLicenseBack) throws IOException {

        System.out.println("Received request: " +
                "agencyId=" + agencyId +
                ", nrc=" + nrc +
                ", nrcPhotoFront=" + (nrcPhotoFront != null ? nrcPhotoFront.getOriginalFilename() : "null") +
                ", nrcPhotoBack=" + (nrcPhotoBack != null ? nrcPhotoBack.getOriginalFilename() : "null") +
                ", agencyLicenseFront=" + (agencyLicenseFront != null ? agencyLicenseFront.getOriginalFilename() : "null") +
                ", agencyLicenseBack=" + (agencyLicenseBack != null ? agencyLicenseBack.getOriginalFilename() : "null"));

        try {
            if (agencyId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new UploadResponse(false, "agencyId is required and must be a valid integer", null));
            }
            if (nrc == null || nrc.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new UploadResponse(false, "nrc is required and cannot be empty", null));
            }
            if (nrcPhotoFront == null || nrcPhotoBack == null ||
                    agencyLicenseFront == null || agencyLicenseBack == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new UploadResponse(false, "All photo files (nrcPhotoFront, nrcPhotoBack, agencyLicenseFront, agencyLicenseBack) are required", null));
            }
            if (nrcPhotoFront.isEmpty() || nrcPhotoBack.isEmpty() ||
                    agencyLicenseFront.isEmpty() || agencyLicenseBack.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new UploadResponse(false, "Photo files cannot be empty", null));
            }

            AgencyVerification verification = agencyVerificationService.uploadAgencyVerification(
                    agencyId, nrc, nrcPhotoFront, nrcPhotoBack, agencyLicenseFront, agencyLicenseBack);

            return ResponseEntity.ok(new UploadResponse(true, "Agency verification uploaded successfully", verification));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UploadResponse(false, "Failed to process uploaded files: " + e.getMessage(), null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UploadResponse(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{agencyId}")
    public ResponseEntity<AgencyVerificationDTO> getAgencyVerification(@PathVariable Integer agencyId) {
        System.out.println("Getting Verification for ageny id" + agencyId);
        try {
            AgencyVerificationDTO verification = agencyVerificationService.getAgencyVerification(agencyId);

            return ResponseEntity.ok(verification);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AgencyVerificationDTO(null, null)); // Return empty DTO or handle differently
        }
    }
}



record UploadResponse(boolean success, String message, AgencyVerification data) {
    public UploadResponse(boolean success, String message) {
        this(success, message, null);
    }
}

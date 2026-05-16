package com.mzp.carrental.controller.customer;

import com.mzp.carrental.dto.CustomerVerificationDTO;
import com.mzp.carrental.entity.Verification.CustomerVerification;
import com.mzp.carrental.service.Customer.CustomerVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/customer/verification")
public class CustomerVerificationController {
    @Autowired
    private CustomerVerificationService customerVerificationService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> uploadCustomerVerification(
            @RequestParam("customerId") Integer customerId,
            @RequestParam("nrc") String nrc,
            @RequestParam("nrcPhotoFront") MultipartFile nrcPhotoFront,
            @RequestParam("nrcPhotoBack") MultipartFile nrcPhotoBack) throws IOException {

        System.out.println("Received request: " +
                "customerId=" + customerId +
                ", nrc=" + nrc +
                ", nrcPhotoFront=" + (nrcPhotoFront != null ? nrcPhotoFront.getOriginalFilename() : "null") +
                ", nrcPhotoBack=" + (nrcPhotoBack != null ? nrcPhotoBack.getOriginalFilename() : "null"));

        try {
            if (customerId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new UploadResponse(false, "customerId is required and must be a valid integer", null));
            }
            if (nrc == null || nrc.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new UploadResponse(false, "nrc is required and cannot be empty", null));
            }
            if (nrcPhotoFront == null || nrcPhotoBack == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new UploadResponse(false, "Both nrcPhotoFront and nrcPhotoBack are required", null));
            }
            if (nrcPhotoFront.isEmpty() || nrcPhotoBack.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new UploadResponse(false, "NRC photo files cannot be empty", null));
            }

            CustomerVerification verification = customerVerificationService.uploadCustomerVerification(
                    customerId, nrc, nrcPhotoFront, nrcPhotoBack);

            return ResponseEntity.ok(new UploadResponse(true, "Verification data uploaded successfully", verification));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UploadResponse(false, "Failed to process uploaded files: " + e.getMessage(), null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new UploadResponse(false, e.getMessage(), null));
        }
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerVerificationDTO> getCustomerVerification(@PathVariable Integer customerId) {
        try {
            CustomerVerificationDTO verification = customerVerificationService.getCustomerVerification(customerId);
            return ResponseEntity.ok(verification);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CustomerVerificationDTO(null, null)); // Empty DTO
        }
    }
}

record UploadResponse(boolean success, String message, CustomerVerification data) {
    public UploadResponse(boolean success, String message) {
        this(success, message, null);
    }
}
package com.mzp.carrental.controller;

import com.mzp.carrental.entity.OurUsers;
import com.mzp.carrental.repository.UsersRepo;
import com.mzp.carrental.service.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final Map<String, String> otpStorage = new HashMap<>();
    private final EmailService emailService;
    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Autowired
    public AuthController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = emailService.generateOtp();
        otpStorage.put(email, otp);
        try {
            emailService.sendOtp(email, otp);
            return ResponseEntity.ok("OTP sent successfully!");
        } catch (MessagingException e) {
            return ResponseEntity.badRequest().body("Failed to send OTP");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        if (otpStorage.containsKey(email) && otpStorage.get(email).equals(otp)) {
            return ResponseEntity.ok("OTP verified successfully!");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        Optional<OurUsers> user = usersRepo.findByEmail(email);
        if (user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(newPassword));
            usersRepo.save(user.get());
            return ResponseEntity.ok("Password reset successful.");
        }
        // Update password in database (mock example)
        System.out.println("Password reset for " + email + ": " + newPassword);

        return ResponseEntity.ok("Password updated successfully!");
    }
}

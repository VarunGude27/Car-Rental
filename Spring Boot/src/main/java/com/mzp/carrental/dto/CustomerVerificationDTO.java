package com.mzp.carrental.dto;


import com.mzp.carrental.entity.Users.Customer;
import com.mzp.carrental.entity.Verification.CustomerVerification;
import lombok.Data;

@Data
public class CustomerVerificationDTO {
    private Integer customerId;
    private String username;
    private String phoneNumber;
    private String city;
    private String nrc;
    private byte[] profileImage;
    private byte[] nrcPhotoFront;
    private byte[] nrcPhotoBack;
    private CustomerVerification.VerificationStatus verificationStatus;
    private String verificationStatusDescription;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNrc() {
        return nrc;
    }

    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public byte[] getNrcPhotoFront() {
        return nrcPhotoFront;
    }

    public void setNrcPhotoFront(byte[] nrcPhotoFront) {
        this.nrcPhotoFront = nrcPhotoFront;
    }

    public byte[] getNrcPhotoBack() {
        return nrcPhotoBack;
    }

    public void setNrcPhotoBack(byte[] nrcPhotoBack) {
        this.nrcPhotoBack = nrcPhotoBack;
    }

    public CustomerVerification.VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(CustomerVerification.VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getVerificationStatusDescription() {
        return verificationStatusDescription;
    }

    public void setVerificationStatusDescription(String verificationStatusDescription) {
        this.verificationStatusDescription = verificationStatusDescription;
    }

    public CustomerVerificationDTO(Customer customer, CustomerVerification verification) {
        this.customerId = customer.getId();
        this.username = customer.getUsername();
        this.phoneNumber = customer.getPhoneNumber();
        this.city = customer.getCity();
        this.nrc = verification.getNrc();
        this.profileImage = customer.getImageData();
        this.nrcPhotoFront = verification.getNrcPhotoFrontData();
        this.nrcPhotoBack = verification.getNrcPhotoBackData();
        this.verificationStatus = verification.getVerificationStatus();
        this.verificationStatusDescription = verification.getVerificationStatus().toString();


    }
}
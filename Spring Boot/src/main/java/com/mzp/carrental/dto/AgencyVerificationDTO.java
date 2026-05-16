package com.mzp.carrental.dto;


import com.mzp.carrental.entity.Users.Agency;
import com.mzp.carrental.entity.Verification.AgencyVerification;
import lombok.Data;

@Data
public class AgencyVerificationDTO {
    private Integer agencyId;
    private String username;
    private String phoneNumber;
    private String address;
    private String city;
    private String nrc;
    private byte[] profileImage;
    private byte[] nrcPhotoFront;
    private byte[] nrcPhotoBack;
    private byte[] agencyLicenseFront;
    private byte[] agencyLicenseBack;
    private AgencyVerification.VerificationStatus verificationStatus;
    private String verificationStatusDescription;

    public Integer getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(Integer agencyId) {
        this.agencyId = agencyId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public byte[] getAgencyLicenseFront() {
        return agencyLicenseFront;
    }

    public void setAgencyLicenseFront(byte[] agencyLicenseFront) {
        this.agencyLicenseFront = agencyLicenseFront;
    }

    public byte[] getAgencyLicenseBack() {
        return agencyLicenseBack;
    }

    public void setAgencyLicenseBack(byte[] agencyLicenseBack) {
        this.agencyLicenseBack = agencyLicenseBack;
    }

    public AgencyVerification.VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(AgencyVerification.VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getVerificationStatusDescription() {
        return verificationStatusDescription;
    }

    public void setVerificationStatusDescription(String verificationStatusDescription) {
        this.verificationStatusDescription = verificationStatusDescription;
    }

    public AgencyVerificationDTO(Agency agency, AgencyVerification verification) {
        this.agencyId = agency.getId();
        this.username = agency.getUsername();
        this.phoneNumber = agency.getPhoneNumber();
        this.address = agency.getAddress();
        this.city = agency.getCity();
        this.nrc = verification.getNrc();
        this.profileImage = agency.getImageData();
        this.nrcPhotoFront = verification.getNrcPhotoFrontData();
        this.nrcPhotoBack = verification.getNrcPhotoBackData();
        this.agencyLicenseFront = verification.getAgencyLicenseFrontData();
        this.agencyLicenseBack = verification.getAgencyLicenseBackData();
        this.verificationStatus = verification.getVerificationStatus();
        this.verificationStatusDescription = verification.getVerificationStatus().toString();
    }
}
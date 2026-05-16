package com.mzp.carrental.entity.Verification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mzp.carrental.entity.Users.Agency;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AgencyVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "agency_id", nullable = false)
    @JsonIgnore
    private Agency agency;
    private String nrc;
    @Column(nullable = true)
    private String nrcPhotoFront;
    @Column(nullable = true)
    private String nrcPhotoBack;
    @Column(nullable = true)
    private String agencyLicenseFront;
    @Column(nullable = true)
    private String agencyLicenseBack;
    @Lob
    private byte[] nrcPhotoFrontData;
    @Lob
    private byte[] nrcPhotoBackData;
    @Lob
    private byte[] agencyLicenseFrontData;
    @Lob
    private byte[] agencyLicenseBackData;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    public enum VerificationStatus {
        PENDING, VERIFIED, REUPLOAD, DENIED
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Agency getAgency() {
        return agency;
    }

    public void setAgency(Agency agency) {
        this.agency = agency;
    }

    public String getNrc() {
        return nrc;
    }

    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    public String getNrcPhotoFront() {
        return nrcPhotoFront;
    }

    public void setNrcPhotoFront(String nrcPhotoFront) {
        this.nrcPhotoFront = nrcPhotoFront;
    }

    public String getNrcPhotoBack() {
        return nrcPhotoBack;
    }

    public void setNrcPhotoBack(String nrcPhotoBack) {
        this.nrcPhotoBack = nrcPhotoBack;
    }

    public String getAgencyLicenseFront() {
        return agencyLicenseFront;
    }

    public void setAgencyLicenseFront(String agencyLicenseFront) {
        this.agencyLicenseFront = agencyLicenseFront;
    }

    public String getAgencyLicenseBack() {
        return agencyLicenseBack;
    }

    public void setAgencyLicenseBack(String agencyLicenseBack) {
        this.agencyLicenseBack = agencyLicenseBack;
    }

    public byte[] getNrcPhotoFrontData() {
        return nrcPhotoFrontData;
    }

    public void setNrcPhotoFrontData(byte[] nrcPhotoFrontData) {
        this.nrcPhotoFrontData = nrcPhotoFrontData;
    }

    public byte[] getNrcPhotoBackData() {
        return nrcPhotoBackData;
    }

    public void setNrcPhotoBackData(byte[] nrcPhotoBackData) {
        this.nrcPhotoBackData = nrcPhotoBackData;
    }

    public byte[] getAgencyLicenseFrontData() {
        return agencyLicenseFrontData;
    }

    public void setAgencyLicenseFrontData(byte[] agencyLicenseFrontData) {
        this.agencyLicenseFrontData = agencyLicenseFrontData;
    }

    public byte[] getAgencyLicenseBackData() {
        return agencyLicenseBackData;
    }

    public void setAgencyLicenseBackData(byte[] agencyLicenseBackData) {
        this.agencyLicenseBackData = agencyLicenseBackData;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
}
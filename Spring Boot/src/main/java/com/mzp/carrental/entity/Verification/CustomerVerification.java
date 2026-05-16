package com.mzp.carrental.entity.Verification;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mzp.carrental.entity.Users.Customer;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CustomerVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;
    private String nrc;
    @Column(nullable = true)
    private String nrcPhotoFront;
    @Column(nullable = true)
    private String nrcPhotoBack;
    @Lob
    private byte[] nrcPhotoFrontData;
    @Lob
    private byte[] nrcPhotoBackData;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
}
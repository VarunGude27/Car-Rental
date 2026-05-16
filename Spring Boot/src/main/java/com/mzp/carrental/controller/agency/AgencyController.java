package com.mzp.carrental.controller.agency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mzp.carrental.dto.ReqRes;
import com.mzp.carrental.entity.Users.Agency;
import com.mzp.carrental.service.agency.AgencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/agency")
public class AgencyController {

    @Autowired
    private AgencyService agencyService;



    @GetMapping
    public ResponseEntity<List<Agency>> getAllAgencies() {
        List<Agency> agencies = agencyService.getAllAgencies();
        return agencies.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(agencies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agency> getAgencyById(@PathVariable Integer id) {
        System.out.println("Inside get agency");
        Agency agency = agencyService.getAgencyById(id);
        System.out.println("And here is " + agency.toString());
        return agency != null
                ? ResponseEntity.ok(agency)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<Agency> createAgency(@RequestBody Agency agency) {
        Agency savedAgency = agencyService.createAgency(agency);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAgency);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Agency> updateAgency(@PathVariable Integer id, @RequestBody Agency agency) {
//        Agency updatedAgency = agencyService.updateAgency(id, agency);
//        return updatedAgency != null
//                ? ResponseEntity.ok(updatedAgency)
//                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Agency> updateAgency(
            @PathVariable Integer id,
            @RequestParam("username") String username,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("address") String address,
            @RequestParam("city") String city,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        Agency agencyDetails = new Agency();
        agencyDetails.setUsername(username);
        agencyDetails.setPhoneNumber(phoneNumber);
        agencyDetails.setAddress(address);
        agencyDetails.setCity(city);

        Agency updatedAgency = agencyService.updateAgency(id, agencyDetails, image);
        return updatedAgency != null
                ? ResponseEntity.ok(updatedAgency)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgency(@PathVariable Integer id) {
        boolean isDeleted = agencyService.deleteAgency(id);
        return isDeleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    // upload to admin /{id}/upload-to-verify
    // function here {} - id, nrc- string, NRC photo Front, Nrc Photo Back, Agency Liscene Photo Front, Agency Liscene Photo Back,  verifiaction status to pending

}

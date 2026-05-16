package com.mzp.carrental.service.agency;


import com.mzp.carrental.dto.AgencyDTO;
import com.mzp.carrental.entity.Users.Agency;
import com.mzp.carrental.repository.agency.AgencyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AgencyService {

    @Autowired
    private AgencyRepo agencyRepo;

    public List<Agency> getAllAgencies() {
        return agencyRepo.findAll();
    }

    public Agency getAgencyById(Integer id) {
        return agencyRepo.findById(id).orElse(null);
    }

    public Agency createAgency(Agency agency) {
        return agencyRepo.save(agency);
    }

//    public Agency updateAgency(Integer id, Agency agencyDetails) {
//        return agencyRepo.findById(id).map(existingAgency -> {
//            existingAgency.setUsername(agencyDetails.getUsername());
//            existingAgency.setPhoneNumber(agencyDetails.getPhoneNumber());
//            existingAgency.setAddress(agencyDetails.getAddress());
//            existingAgency.setCity(agencyDetails.getCity());
//            return agencyRepo.save(existingAgency);
//        }).orElse(null);
//    }
        public Agency updateAgency(Integer id, Agency agencyDetails, MultipartFile image) throws IOException {
            return agencyRepo.findById(id).map(existingAgency -> {
                existingAgency.setUsername(agencyDetails.getUsername());
                existingAgency.setPhoneNumber(agencyDetails.getPhoneNumber());
                existingAgency.setAddress(agencyDetails.getAddress());
                existingAgency.setCity(agencyDetails.getCity());

                if (image != null) {
                    existingAgency.setImageName(image.getOriginalFilename());
                    existingAgency.setImageType(image.getContentType());
                    try {
                        System.out.println("Inside updategencyService");
                        existingAgency.setImageData(image.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                return agencyRepo.save(existingAgency);
            }).orElse(null);
        }

    public boolean deleteAgency(Integer id) {
        if (agencyRepo.existsById(id)) {
            agencyRepo.deleteById(id);
            return true;
        }
        return false;
    }

    // -----------agency view
    public byte[] getAgencyImageById(Integer id) {
        Optional<Agency> agencyOptional = agencyRepo.findById(id);
        return agencyOptional.map(Agency::getImageData).orElse(null);
    }
    public String getAgencyImageTypeById(Integer id) {
        Optional<Agency> agencyOptional = agencyRepo.findById(id);
        return agencyOptional.map(Agency::getImageType).orElse(null);
    }


}

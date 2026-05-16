
package com.mzp.carrental.service.rent;
import com.mzp.carrental.dto.RentDTO;
import com.mzp.carrental.dto.RentalOrderDTO;
import com.mzp.carrental.entity.Cars.Car;
import com.mzp.carrental.entity.OurUsers;
import com.mzp.carrental.entity.Rent.Rent;

import com.mzp.carrental.entity.Rent.RentalOrder;
import com.mzp.carrental.entity.Users.Agency;
import com.mzp.carrental.entity.Users.Customer;
import com.mzp.carrental.repository.Car.CarRepo;
import com.mzp.carrental.repository.Customer.CustomerRepo;
import com.mzp.carrental.repository.UsersRepo;
import com.mzp.carrental.repository.agency.AgencyRepo;
import com.mzp.carrental.repository.rent.RentRepo;
import com.mzp.carrental.service.Customer.CustomerService;
import com.mzp.carrental.service.OurUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class RentService {

    @Autowired
    private RentRepo rentRepository;
    @Autowired
    private OurUserDetailsService userDetailsService;
    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private CarRepo carRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private AgencyRepo agencyRepo;





    public Rent getRentById(Long id) {
        Optional<Rent> rent = rentRepository.findById(id);
        if (rent.isEmpty()) {
            throw new RuntimeException("Rent with ID " + id + " not found");
        }
        return rent.get();
    }

    public Rent saveRent(Rent rent) {
        return rentRepository.save(rent);
    }


    public List<RentDTO> getFilteredRentsByCar(Long carId) {
        List<Rent> rents = rentRepository.findByCarId(carId);



        // Debug: Print filtered orders
        rents.forEach(rent -> System.out.println("rents: " + rent));
        // Map to DTOs
        return rents.stream().map(this::mapToDTO).toList();

    }

    private RentDTO mapToDTO(Rent rent) {
        RentDTO dto = new RentDTO();
        dto.setId(rent.getId());
        dto.setRentStatus(rent.getRentStatus());
        dto.setCarId(rent.getCar().getId());
        dto.setCustomerId(rent.getCustomer().getId());
        dto.setStartDate(rent.getStartDate());
        dto.setEndDate(rent.getEndDate());
        dto.setCustomerName(rent.getCustomer().getUsername());
        dto.setCustomerVerificationStatus(rent.getCustomer().getVerificationStatus().toString());
        dto.setPickUpLocation(rent.getRentalOrder().getPickUpLocation());
        dto.setDropOffLocation(rent.getRentalOrder().getDropOffLocation());
        dto.setIncludeDriver(rent.getRentalOrder().isIncludeDriver());
        dto.setTotalPrice(rent.getRentalOrder().getTotalPrice());
        dto.setCustomerPhoneNumber(rent.getCustomerPhoneNumber());
        dto.setCarBrand(rent.getCar().getBrand());
        dto.setCarModel(rent.getCar().getModel());
        dto.setIncludeDriver(rent.isIncludeDriver());
        dto.setTotalPrice(rent.getTotalPrice());
        dto.setCreatedDate(rent.getCreatedDate());
        System.out.println("Responded Rent DTO is ");
        System.out.println(dto.toString());
        return dto;
    }

    public void updateOrderStatus(Long id, Rent.RentStatus status) {
        Rent rent = rentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        if (!rent.getRentStatus().equals(status)) {
            rent.setRentStatus(status);
            if (status.equals(Rent.RentStatus.COMPLETED)) {
                updateStatisticsOnCompletion(rent);
            }
        }

        rentRepository.save(rent);
    }
    private void updateStatisticsOnCompletion(Rent rent) {
        // Update Car statistics
        Car car = rent.getCar();
        car.setTotalRents(car.getTotalRents() + 1);
        car.setTotalMoneyMade(car.getTotalMoneyMade() + rent.getTotalPrice());
        int totalDaysRented = (int) (ChronoUnit.DAYS.between(rent.getStartDate(), rent.getEndDate()) + 1);
        car.setTotalDaysRented(car.getTotalDaysRented() + totalDaysRented);
        carRepo.save(car);

        // Update Customer statistics
        Customer customer = rent.getCustomer();
        customer.setTotalRents(customer.getTotalRents() + 1);
        customer.setTotalSpending(customer.getTotalSpending() + rent.getTotalPrice());
        customerRepo.save(customer);

        // Update Agency statistics
        Agency agency = car.getAgency();
        agency.setTotalRents(agency.getTotalRents() + 1);
        agency.setTotalRevenue(agency.getTotalRevenue() + rent.getTotalPrice());
        agencyRepo.save(agency);
    }

    public List<RentDTO> getFilteredRentsByCustomer() {
        String email = userDetailsService.getCurrentUserEmail();
        System.out.println("Email in getCurrentCustomer is " + email);
        Optional<OurUsers> user = usersRepo.findByEmail(email);
        Integer customerId = user.get().getId();


        List<Rent> rents = rentRepository.findByCustomerId(customerId);



        // Debug: Print filtered orders
        rents.forEach(rent -> System.out.println("rents: " + rent));
        // Map to DTOs
        return rents.stream().map(this::mapToDTO).toList();
    }
    public List<RentDTO> getFilteredRentsByAgency() {
        String email = userDetailsService.getCurrentUserEmail();
        System.out.println("Email in getFilteredRentsByAgency is " + email);

        OurUsers user = usersRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Integer agencyId = user.getId();

        List<Rent> rents = rentRepository.findByAgencyId(agencyId);

        rents.forEach(rent -> System.out.println("Rent: " + rent));

        return rents.stream().map(this::mapToDTO).toList();
    }

    public List<LocalDate> getUnavailableDatesByCar(Long carId) {
        // Fetch and filter orders
        List<Rent> filteredRents = getFilteredRentsByCar(carId)
                .stream()
                .map(this::mapToEntity) // Map DTO back to entity if needed
                .toList();

        // Print fetched orders for debugging
        System.out.println("Fetched Orders for Car ID " + carId + ":");
        for (Rent rent : filteredRents) {
            System.out.println("Rent ID: " + rent.getId() + ", Start Date: " + rent.getStartDate() + ", End Date: " + rent.getEndDate());
        }

        // Calculate unavailable dates
        List<LocalDate> unavailableDates = filteredRents.stream()
                .flatMap(rent -> rent.getStartDate().datesUntil(rent.getEndDate().plusDays(1))) // Include end date
                .distinct()
                .collect(Collectors.toList());

        // Print unavailable dates for debugging
        System.out.println("Unavailable Dates for Car ID " + carId + ":");
        unavailableDates.forEach(System.out::println);

        return unavailableDates;
    }

    private Rent mapToEntity(RentDTO dto) {
        Rent entity = new Rent();
        entity.setId(dto.getId());
        entity.setRentStatus(dto.getRentStatus());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setIncludeDriver(dto.isIncludeDriver());
        entity.setTotalPrice(dto.getTotalPrice());
        entity.setCustomerPhoneNumber(dto.getCustomerPhoneNumber());

        return entity;
    }
}


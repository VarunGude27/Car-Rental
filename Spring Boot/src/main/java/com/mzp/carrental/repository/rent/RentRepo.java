package com.mzp.carrental.repository.rent;

import com.mzp.carrental.entity.Cars.Car;
import com.mzp.carrental.entity.Rent.Rent;
import com.mzp.carrental.entity.Rent.RentalOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentRepo extends JpaRepository<Rent,Long> {
    Rent findByRentalOrder(RentalOrder rentalOrder);

    List<Rent> findByCarId(Long carId);

    List<Rent> findByCustomerId(Integer customerId);

    @Query("""
    SELECT r FROM Rent r
    WHERE r.car.agency.id = :agencyId
    ORDER BY r.id DESC
""")
    List<Rent> findByAgencyId(@Param("agencyId") Integer agencyId);


    boolean existsByCarIdAndStartDateBeforeAndEndDateAfter(Long id, LocalDate endDate, LocalDate startDate);

    //boolean existsByOrderId(Long orderId);

    boolean existsByRentalOrder(Optional<RentalOrder> byId);

    @Query("""
    SELECT COUNT(r) > 0 FROM Rent r 
    WHERE r.car.id = :carId 
    AND r.rentStatus IN ('NOT_STARTED', 'ONGOING')
    AND (
        (r.startDate <= :endDate AND r.endDate >= :startDate)
    )
    ORDER BY r.id DESC
""")
    boolean existsOverlappingActiveRent(
            @Param("carId") Long carId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    boolean existsByCustomerIdAndCarId(Integer id, Long carId);


    @Query("""
    SELECT r.car FROM Rent r
    GROUP BY r.car
    ORDER BY COUNT(r) DESC
    """)
    List<Car> findTopRentedCars(Integer numOfCars);

    //boolean existsByCarIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndOrderIdNot(Long id, LocalDate endDate, LocalDate startDate, Long id1);



    // below two code Quaries for Deleting User
//    @Modifying
//    @Query("DELETE FROM RentalOrder ro WHERE ro.car.id = :carId")
//    void deleteByCar(@Param("carId") Long carId);
//
//
//    @Modifying
//    @Query("DELETE FROM RentalOrder ro WHERE ro.customer.id = :customerId")
//    void deleteByCustomer(@Param("customerId") Integer customerId);

//    @Query("SELECT SUM(r.totalPrice) FROM Rent r")
//    double sumTotalRevenue();



}

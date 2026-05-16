package com.mzp.carrental.repository.rent;

import com.mzp.carrental.entity.Rent.RentalOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RentalOrderRepo extends JpaRepository<RentalOrder,Long> {
    List<RentalOrder> findByCustomer_Id(Integer customerId);

//    @Query("SELECT ro FROM RentalOrder ro WHERE ro.car.agency.id = :agencyId")
//    List<RentalOrder> findByAgencyId(@Param("agencyId") Integer agencyId);

    @Query("SELECT ro FROM RentalOrder ro WHERE ro.car.id = :carId ORDER BY ro.id DESC")
    List<RentalOrder> findByCarId(@Param("carId") Long carId);

    @Query("SELECT ro FROM RentalOrder ro WHERE ro.customer.id = :customerId ORDER BY ro.id DESC")
    List<RentalOrder> findByCustomerId(Integer customerId);

    @Query("SELECT ro FROM RentalOrder ro JOIN FETCH ro.car JOIN FETCH ro.customer WHERE ro.car.agency.id = :agencyId ORDER BY ro.id DESC")
    List<RentalOrder> findByAgencyId(@Param("agencyId") Integer agencyId);

    @Query("""
    SELECT COUNT(ro) > 0 FROM RentalOrder ro 
    WHERE ro.car.id = :carId 
    AND ro.status = 'APPROVED'
    AND (
        (ro.startDate <= :endDate AND ro.endDate >= :startDate) 
    )
    ORDER BY ro.id DESC
""")
    boolean existsOverlappingApprovedOrder(
            @Param("carId") Long carId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Below two Quaries for Deleting a user
//    @Modifying
//    @Query("DELETE FROM Rent r WHERE r.customer.id = :customerId")
//    void deleteByCustomer(@Param("customerId") Integer customerId);
//
//    @Modifying
//    @Query("DELETE FROM Rent r WHERE r.car.id = :carId")
//    void deleteByCar(@Param("carId") Long carId);


}

package com.mzp.carrental.repository.Car;

import com.mzp.carrental.entity.Cars.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RatingsRepo extends JpaRepository<Ratings, Integer> {
    Optional<Ratings> findByCustomerIdAndCarId(Integer customerId, Long carId);

    @Query("SELECT AVG(r.ratingValue) FROM Ratings r WHERE r.car.id = :carId")
    Double calculateAverageRatingByCarId(@Param("carId") Long carId);

    // Below Two Quaries for deleting a user
//    @Modifying
//    @Query("DELETE FROM Rating r WHERE r.customer.id = :customerId")
//    void deleteByCustomer(@Param("customerId") Integer customerId);
//
//    @Modifying
//    @Query("DELETE FROM Rating r WHERE r.car.id = :carId")
//    void deleteByCar(@Param("carId") Long carId);
}

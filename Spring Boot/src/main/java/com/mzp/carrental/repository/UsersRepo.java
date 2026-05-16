package com.mzp.carrental.repository;


import com.mzp.carrental.entity.OurUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepo extends JpaRepository<OurUsers, Integer> {

    Optional<OurUsers> findByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM OurUsers u WHERE u.role = :role")
    boolean existsByRole(@Param("role") String role);

    boolean existsByEmail(String email);
}

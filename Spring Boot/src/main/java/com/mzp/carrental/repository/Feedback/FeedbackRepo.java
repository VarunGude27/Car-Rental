package com.mzp.carrental.repository.Feedback;


import com.mzp.carrental.entity.Feedback.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedbackRepo extends JpaRepository<Feedback, Long> {

    Page<Feedback> findAllByOrderByIdDesc(Pageable pageable);
}

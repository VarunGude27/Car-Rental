package com.mzp.carrental.service.feedback;


import com.mzp.carrental.entity.Feedback.Feedback;
import com.mzp.carrental.repository.Feedback.FeedbackRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepo feedbackRepo;

    public Feedback submitFeedback(Feedback feedback) {
        feedback.setCreatedAt(LocalDateTime.now());
        return feedbackRepo.save(feedback);
    }

    public List<Feedback> getAllFeedback() {
        return feedbackRepo.findAll();
    }

    public List<Feedback> getFeedbackByPage(int page, int size) {
        Page<Feedback> feedbackPage = feedbackRepo.findAllByOrderByIdDesc(PageRequest.of(page, size));
        return feedbackPage.getContent();
    }
}

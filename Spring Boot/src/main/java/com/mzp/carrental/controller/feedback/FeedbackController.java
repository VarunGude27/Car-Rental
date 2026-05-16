package com.mzp.carrental.controller.feedback;

import com.mzp.carrental.dto.FeedbackDTO;
import com.mzp.carrental.entity.Feedback.Feedback;
import com.mzp.carrental.service.feedback.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Feedback> submitFeedback(@RequestBody Feedback feedback) {
        Feedback savedFeedback = feedbackService.submitFeedback(feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFeedback);
    }

    @GetMapping
    public ResponseEntity<List<FeedbackDTO>> getAllFeedback() {
        List<FeedbackDTO> feedbackList = feedbackService.getAllFeedback()
                .stream()
                .map(feedback -> {
                    FeedbackDTO dto = new FeedbackDTO();
                    dto.setName(feedback.getName());
                    dto.setEmail(feedback.getEmail());
                    dto.setMessage(feedback.getMessage());
                    dto.setCreatedAt(feedback.getCreatedAt());
                    return dto;
                })
                .toList();

        return feedbackList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(feedbackList);
    }


    @GetMapping("/page/{pageNumber}")
    public ResponseEntity<List<Feedback>> getFeedbackByPage(@PathVariable int pageNumber) {
        int pageSize = 10; // Each page contains 10 feedbacks
        List<Feedback> feedbackList = feedbackService.getFeedbackByPage(pageNumber - 1, pageSize); // Page index starts at 0
        return feedbackList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(feedbackList);
    }
}

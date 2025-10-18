package com.example.productapi.service;

import com.example.productapi.dto.request.FeedbackRequest;
import com.example.productapi.dto.response.FeedbackResponse;
import com.example.productapi.dto.response.MessageResponse;
import com.example.productapi.entity.Feedback;
import com.example.productapi.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackService {
    
    private final FeedbackRepository feedbackRepository;
    
    public FeedbackResponse createFeedback(FeedbackRequest request) {
        Feedback feedback = new Feedback();
        feedback.setName(request.getName());
        feedback.setEmail(request.getEmail());
        feedback.setMessage(request.getMessage());
        
        Feedback savedFeedback = feedbackRepository.save(feedback);
        return mapToResponse(savedFeedback);
    }
    
    public FeedbackResponse getFeedbackById(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));
        return mapToResponse(feedback);
    }
    
    public List<FeedbackResponse> getAllFeedbacks() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    

    public FeedbackResponse updateFeedback(Long id, FeedbackRequest request) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));
        
        feedback.setName(request.getName());
        feedback.setEmail(request.getEmail());
        feedback.setMessage(request.getMessage());
        
        Feedback updatedFeedback = feedbackRepository.save(feedback);
        return mapToResponse(updatedFeedback);
    }
    
    
    public MessageResponse deleteFeedback(Long id) {
        if (!feedbackRepository.existsById(id)) {
            throw new RuntimeException("Feedback not found with id: " + id);
        }
        
        feedbackRepository.deleteById(id);
        return MessageResponse.builder()
                .message("Feedback deleted successfully")
                .build();
    }
    
    private FeedbackResponse mapToResponse(Feedback feedback) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .name(feedback.getName())
                .email(feedback.getEmail())
                .message(feedback.getMessage())
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}
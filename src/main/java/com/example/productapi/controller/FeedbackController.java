package com.example.productapi.controller;

import com.example.productapi.dto.request.FeedbackRequest;
import com.example.productapi.dto.response.FeedbackResponse;
import com.example.productapi.dto.response.MessageResponse;
import com.example.productapi.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
@Tag(name = "Feedback Management", description = "APIs for managing feedback")
public class FeedbackController {
    
    private final FeedbackService feedbackService;
    
    @PostMapping
    @Operation(summary = "Create new feedback", description = "Create a new feedback entry")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Feedback created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<FeedbackResponse> createFeedback(
            @Valid @RequestBody FeedbackRequest request) {
        FeedbackResponse response = feedbackService.createFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get feedback by ID", description = "Retrieve a specific feedback by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feedback found"),
        @ApiResponse(responseCode = "404", description = "Feedback not found")
    })
    public ResponseEntity<FeedbackResponse> getFeedbackById(
            @Parameter(description = "Feedback ID") @PathVariable Long id) {
        FeedbackResponse response = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all feedbacks", description = "Retrieve all feedbacks ordered by creation date")
    @ApiResponse(responseCode = "200", description = "List of feedbacks retrieved successfully")
    public ResponseEntity<List<FeedbackResponse>> getAllFeedbacks() {
        List<FeedbackResponse> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update feedback", description = "Update an existing feedback")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feedback updated successfully"),
        @ApiResponse(responseCode = "404", description = "Feedback not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<FeedbackResponse> updateFeedback(
            @Parameter(description = "Feedback ID") @PathVariable Long id,
            @Valid @RequestBody FeedbackRequest request) {
        FeedbackResponse response = feedbackService.updateFeedback(id, request);
        return ResponseEntity.ok(response);
    }
    
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete feedback", description = "Delete a feedback by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feedback deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Feedback not found")
    })
    public ResponseEntity<MessageResponse> deleteFeedback(
            @Parameter(description = "Feedback ID") @PathVariable Long id) {
        MessageResponse response = feedbackService.deleteFeedback(id);
        return ResponseEntity.ok(response);
    }
}
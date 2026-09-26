package com.devshowcase.api.dto;

import com.devshowcase.api.entity.Feedback;

import java.time.LocalDateTime;

/**
 * DTO de saída para Feedback.
 */
public record FeedbackResponse(
    Long id,
    String authorName,
    String comment,
    Integer rating,
    LocalDateTime createdAt,
    Long projectId
) {
    public static FeedbackResponse from(Feedback feedback) {
        return new FeedbackResponse(
            feedback.getId(),
            feedback.getAuthorName(),
            feedback.getComment(),
            feedback.getRating(),
            feedback.getCreatedAt(),
            feedback.getProject() != null ? feedback.getProject().getId() : null
        );
    }
}

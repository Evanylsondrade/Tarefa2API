package com.devshowcase.api.dto;

import com.devshowcase.api.entity.Project;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de saída para Project. Inclui curtidas (upvotes), média de avaliações (averageRating), tecnologias e feedbacks.
 */
public record ProjectResponse(
    Long id,
    String title,
    String description,
    String repositoryUrl,
    String demoUrl,
    String thumbnailUrl,
    Integer upvotes,
    Double averageRating,
    LocalDateTime createdAt,
    Long profileId,
    String profileName,
    List<TechnologyResponse> technologies,
    List<FeedbackResponse> feedbacks
) {

    public static ProjectResponse from(Project project) {
        List<TechnologyResponse> techs = project.getTechnologies() != null
            ? project.getTechnologies().stream().map(TechnologyResponse::from).toList()
            : List.of();

        List<FeedbackResponse> fbs = project.getFeedbacks() != null
            ? project.getFeedbacks().stream().map(FeedbackResponse::from).toList()
            : List.of();

        return new ProjectResponse(
            project.getId(),
            project.getTitle(),
            project.getDescription(),
            project.getRepositoryUrl(),
            project.getDemoUrl(),
            project.getThumbnailUrl(),
            project.getUpvotes() != null ? project.getUpvotes() : 0,
            project.getAverageRating() != null ? project.getAverageRating() : 0.0,
            project.getCreatedAt(),
            project.getProfile() != null ? project.getProfile().getId() : null,
            project.getProfile() != null ? project.getProfile().getName() : null,
            techs,
            fbs
        );
    }
}

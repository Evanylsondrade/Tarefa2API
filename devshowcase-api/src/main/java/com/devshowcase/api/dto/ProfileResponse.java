package com.devshowcase.api.dto;

import com.devshowcase.api.entity.Profile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de saída para Profile. Expõe dados públicos e lista resumida de projetos.
 */
public record ProfileResponse(
    Long id,
    String name,
    String email,
    String bio,
    String githubUrl,
    String linkedinUrl,
    String avatarUrl,
    LocalDateTime createdAt,
    List<ProjectSummary> projects
) {

    /** Projeção resumida de Project para evitar payload pesado */
    public record ProjectSummary(
        Long id,
        String title,
        String description,
        String repositoryUrl,
        String demoUrl
    ) {}

    /** Factory method que converte entidade para DTO de saída */
    public static ProfileResponse from(Profile profile) {
        List<ProjectSummary> projectSummaries = profile.getProjects().stream()
            .map(p -> new ProjectSummary(
                p.getId(),
                p.getTitle(),
                p.getDescription(),
                p.getRepositoryUrl(),
                p.getDemoUrl()
            ))
            .toList();

        return new ProfileResponse(
            profile.getId(),
            profile.getName(),
            profile.getEmail(),
            profile.getBio(),
            profile.getGithubUrl(),
            profile.getLinkedinUrl(),
            profile.getAvatarUrl(),
            profile.getCreatedAt(),
            projectSummaries
        );
    }
}

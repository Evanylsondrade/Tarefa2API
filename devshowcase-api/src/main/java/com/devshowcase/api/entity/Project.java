package com.devshowcase.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entidade que representa um projeto do desenvolvedor.
 * Relacionamentos:
 *   - Project N:1 Profile
 *   - Project N:N Technology
 *   - Project 1:N Feedback
 */
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "repository_url", length = 255)
    private String repositoryUrl;

    @Column(name = "demo_url", length = 255)
    private String demoUrl;

    @Column(name = "thumbnail_url", length = 255)
    private String thumbnailUrl;

    /** Número de curtidas/upvotes (estrelas) */
    @Column(name = "upvotes", nullable = false)
    private Integer upvotes = 0;

    /** Média das notas dos feedbacks (1.0 a 5.0) */
    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Profile dono deste projeto (N:1) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    /** Tecnologias usadas no projeto (N:N) */
    @ManyToMany
    @JoinTable(
        name = "project_technologies",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "technology_id")
    )
    private Set<Technology> technologies = new HashSet<>();

    /** Feedbacks recebidos pelo projeto (1:N) */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks = new ArrayList<>();

    public Project() {
    }

    public Project(Long id, String title, String description, String repositoryUrl, String demoUrl, String thumbnailUrl, Integer upvotes, Double averageRating, LocalDateTime createdAt, LocalDateTime updatedAt, Profile profile, Set<Technology> technologies, List<Feedback> feedbacks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.repositoryUrl = repositoryUrl;
        this.demoUrl = demoUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.upvotes = upvotes != null ? upvotes : 0;
        this.averageRating = averageRating != null ? averageRating : 0.0;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.profile = profile;
        if (technologies != null) {
            this.technologies = technologies;
        }
        if (feedbacks != null) {
            this.feedbacks = feedbacks;
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getDemoUrl() {
        return demoUrl;
    }

    public void setDemoUrl(String demoUrl) {
        this.demoUrl = demoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Integer getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(Integer upvotes) {
        this.upvotes = upvotes;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Set<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<Technology> technologies) {
        this.technologies = technologies;
    }

    public List<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    /** Recalcula a nota média do projeto com base na lista de feedbacks */
    public void recalculateAverageRating() {
        if (feedbacks == null || feedbacks.isEmpty()) {
            this.averageRating = 0.0;
        } else {
            double sum = feedbacks.stream()
                    .filter(f -> f.getRating() != null)
                    .mapToInt(Feedback::getRating)
                    .sum();
            double avg = sum / feedbacks.size();
            this.averageRating = Math.round(avg * 10.0) / 10.0;
        }
    }

    public static ProjectBuilder builder() {
        return new ProjectBuilder();
    }

    public static class ProjectBuilder {
        private Long id;
        private String title;
        private String description;
        private String repositoryUrl;
        private String demoUrl;
        private String thumbnailUrl;
        private Integer upvotes = 0;
        private Double averageRating = 0.0;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Profile profile;
        private Set<Technology> technologies = new HashSet<>();
        private List<Feedback> feedbacks = new ArrayList<>();

        public ProjectBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProjectBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ProjectBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProjectBuilder repositoryUrl(String repositoryUrl) {
            this.repositoryUrl = repositoryUrl;
            return this;
        }

        public ProjectBuilder demoUrl(String demoUrl) {
            this.demoUrl = demoUrl;
            return this;
        }

        public ProjectBuilder thumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
            return this;
        }

        public ProjectBuilder upvotes(Integer upvotes) {
            this.upvotes = upvotes;
            return this;
        }

        public ProjectBuilder averageRating(Double averageRating) {
            this.averageRating = averageRating;
            return this;
        }

        public ProjectBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ProjectBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ProjectBuilder profile(Profile profile) {
            this.profile = profile;
            return this;
        }

        public ProjectBuilder technologies(Set<Technology> technologies) {
            this.technologies = technologies;
            return this;
        }

        public ProjectBuilder feedbacks(List<Feedback> feedbacks) {
            this.feedbacks = feedbacks;
            return this;
        }

        public Project build() {
            return new Project(id, title, description, repositoryUrl, demoUrl, thumbnailUrl, upvotes, averageRating, createdAt, updatedAt, profile, technologies, feedbacks);
        }
    }
}

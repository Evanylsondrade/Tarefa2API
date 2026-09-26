package com.devshowcase.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entidade que representa um feedback/opinião sobre um projeto.
 * Relacionamento: Feedback N:1 Project
 */
@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome de quem enviou o feedback */
    @Column(name = "author_name", nullable = false, length = 100)
    private String authorName;

    @Column(nullable = false, length = 2000)
    private String comment;

    /** Nota de 1 a 5 */
    @Column(nullable = false)
    private Integer rating;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Projeto ao qual este feedback pertence (N:1) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public Feedback() {
    }

    public Feedback(Long id, String authorName, String comment, Integer rating, LocalDateTime createdAt, Project project) {
        this.id = id;
        this.authorName = authorName;
        this.comment = comment;
        this.rating = rating;
        this.createdAt = createdAt;
        this.project = project;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public static FeedbackBuilder builder() {
        return new FeedbackBuilder();
    }

    public static class FeedbackBuilder {
        private Long id;
        private String authorName;
        private String comment;
        private Integer rating;
        private LocalDateTime createdAt;
        private Project project;

        public FeedbackBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public FeedbackBuilder authorName(String authorName) {
            this.authorName = authorName;
            return this;
        }

        public FeedbackBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public FeedbackBuilder rating(Integer rating) {
            this.rating = rating;
            return this;
        }

        public FeedbackBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public FeedbackBuilder project(Project project) {
            this.project = project;
            return this;
        }

        public Feedback build() {
            return new Feedback(id, authorName, comment, rating, createdAt, project);
        }
    }
}

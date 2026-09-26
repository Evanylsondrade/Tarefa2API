package com.devshowcase.api.service;

import com.devshowcase.api.dto.FeedbackRequest;
import com.devshowcase.api.dto.FeedbackResponse;
import com.devshowcase.api.dto.ProjectRequest;
import com.devshowcase.api.dto.ProjectResponse;
import com.devshowcase.api.entity.Feedback;
import com.devshowcase.api.entity.Profile;
import com.devshowcase.api.entity.Project;
import com.devshowcase.api.entity.Technology;
import com.devshowcase.api.exception.ResourceNotFoundException;
import com.devshowcase.api.repository.FeedbackRepository;
import com.devshowcase.api.repository.ProfileRepository;
import com.devshowcase.api.repository.ProjectRepository;
import com.devshowcase.api.repository.TechnologyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;
    private final TechnologyRepository technologyRepository;
    private final FeedbackRepository feedbackRepository;

    public ProjectService(ProjectRepository projectRepository,
                          ProfileRepository profileRepository,
                          TechnologyRepository technologyRepository,
                          FeedbackRepository feedbackRepository) {
        this.projectRepository = projectRepository;
        this.profileRepository = profileRepository;
        this.technologyRepository = technologyRepository;
        this.feedbackRepository = feedbackRepository;
    }

    /**
     * Cadastra um novo projeto associado a um perfil existente.
     * Associa tecnologias pelos IDs fornecidos.
     */
    @Transactional
    public ProjectResponse create(ProjectRequest request) {
        // Verifica se o perfil existe
        Profile profile = profileRepository.findById(request.profileId())
            .orElseThrow(() -> new ResourceNotFoundException("Profile", request.profileId()));

        // Monta o conjunto de tecnologias a associar
        Set<Technology> technologies = new HashSet<>();
        if (request.technologyIds() != null && !request.technologyIds().isEmpty()) {
            for (Long techId : request.technologyIds()) {
                Technology tech = technologyRepository.findById(techId)
                    .orElseThrow(() -> new ResourceNotFoundException("Technology", techId));
                technologies.add(tech);
            }
        }

        Project project = Project.builder()
            .title(request.title())
            .description(request.description())
            .repositoryUrl(request.repositoryUrl())
            .demoUrl(request.demoUrl())
            .thumbnailUrl(request.thumbnailUrl())
            .profile(profile)
            .technologies(technologies)
            .build();

        Project saved = projectRepository.save(project);
        return ProjectResponse.from(saved);
    }

    /**
     * Lista todos os projetos com suas tecnologias associadas.
     * Usa fetch join para evitar o problema N+1.
     */
    @Transactional(readOnly = true)
    public List<ProjectResponse> findAll() {
        return projectRepository.findAllWithTechnologies().stream()
            .map(ProjectResponse::from)
            .toList();
    }

    /**
     * Lista projetos com filtragem opcional por tecnologia e suporte a paginação.
     */
    @Transactional(readOnly = true)
    public Page<ProjectResponse> findAllPaginated(Long technologyId, String technology, Pageable pageable) {
        return projectRepository.findByTechnologyFilters(technologyId, technology, pageable)
            .map(ProjectResponse::from);
    }

    /**
     * Cadastra um feedback (nota de 1 a 5 e comentário) para o projeto.
     * Recalcula e atualiza a nota média do projeto.
     */
    @Transactional
    public FeedbackResponse addFeedback(Long projectId, FeedbackRequest request) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));

        Feedback feedback = Feedback.builder()
            .authorName(request.authorName())
            .comment(request.comment())
            .rating(request.rating())
            .project(project)
            .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);

        project.getFeedbacks().add(savedFeedback);
        project.recalculateAverageRating();
        projectRepository.save(project);

        return FeedbackResponse.from(savedFeedback);
    }

    /**
     * Incrementa as curtidas/upvotes (estrelas) de um projeto.
     */
    @Transactional
    public ProjectResponse upvote(Long projectId) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));

        int currentUpvotes = project.getUpvotes() != null ? project.getUpvotes() : 0;
        project.setUpvotes(currentUpvotes + 1);

        Project updated = projectRepository.save(project);
        return ProjectResponse.from(updated);
    }
}

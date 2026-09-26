package com.devshowcase.api.repository;

import com.devshowcase.api.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByProfileId(Long profileId);

    /** Carrega projetos com tecnologias em uma única query para evitar N+1 */
    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.technologies LEFT JOIN FETCH p.profile")
    List<Project> findAllWithTechnologies();

    /**
     * Busca projetos com filtragem opcional por ID de tecnologia ou nome da tecnologia com paginação.
     */
    @Query(value = "SELECT DISTINCT p FROM Project p " +
                   "LEFT JOIN FETCH p.profile " +
                   "LEFT JOIN p.technologies t " +
                   "WHERE (:technologyId IS NULL OR t.id = :technologyId) " +
                   "AND (:technologyName IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :technologyName, '%')))",
           countQuery = "SELECT COUNT(DISTINCT p) FROM Project p " +
                        "LEFT JOIN p.technologies t " +
                        "WHERE (:technologyId IS NULL OR t.id = :technologyId) " +
                        "AND (:technologyName IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :technologyName, '%')))")
    Page<Project> findByTechnologyFilters(@Param("technologyId") Long technologyId,
                                          @Param("technologyName") String technologyName,
                                          Pageable pageable);
}

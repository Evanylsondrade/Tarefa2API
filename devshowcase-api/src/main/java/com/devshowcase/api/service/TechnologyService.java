package com.devshowcase.api.service;

import com.devshowcase.api.dto.TechnologyRequest;
import com.devshowcase.api.dto.TechnologyResponse;
import com.devshowcase.api.entity.Technology;
import com.devshowcase.api.exception.ConflictException;
import com.devshowcase.api.repository.TechnologyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TechnologyService {

    private final TechnologyRepository technologyRepository;

    public TechnologyService(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    /**
     * Cadastra uma nova tecnologia.
     * Valida duplicidade de nome (case-insensitive).
     */
    @Transactional
    public TechnologyResponse create(TechnologyRequest request) {
        if (technologyRepository.existsByNameIgnoreCase(request.name())) {
            throw new ConflictException("Tecnologia '" + request.name() + "' já está cadastrada");
        }

        Technology technology = Technology.builder()
            .name(request.name())
            .category(request.category())
            .iconUrl(request.iconUrl())
            .build();

        Technology saved = technologyRepository.save(technology);
        return TechnologyResponse.from(saved);
    }

    /**
     * Lista todas as tecnologias cadastradas.
     */
    @Transactional(readOnly = true)
    public List<TechnologyResponse> findAll() {
        return technologyRepository.findAll().stream()
            .map(TechnologyResponse::from)
            .toList();
    }
}

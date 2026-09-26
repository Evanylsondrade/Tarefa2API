package com.devshowcase.api.service;

import com.devshowcase.api.dto.ProfileRequest;
import com.devshowcase.api.dto.ProfileResponse;
import com.devshowcase.api.entity.Profile;
import com.devshowcase.api.exception.ConflictException;
import com.devshowcase.api.exception.ResourceNotFoundException;
import com.devshowcase.api.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    /**
     * Cria um novo perfil de desenvolvedor.
     * Valida duplicidade de e-mail antes de persistir.
     */
    @Transactional
    public ProfileResponse create(ProfileRequest request) {
        if (profileRepository.existsByEmail(request.email())) {
            throw new ConflictException("Já existe um perfil cadastrado com o e-mail: " + request.email());
        }

        Profile profile = Profile.builder()
            .name(request.name())
            .email(request.email())
            .bio(request.bio())
            .githubUrl(request.githubUrl())
            .linkedinUrl(request.linkedinUrl())
            .avatarUrl(request.avatarUrl())
            .build();

        Profile saved = profileRepository.save(profile);
        return ProfileResponse.from(saved);
    }

    /**
     * Busca um perfil pelo ID.
     * Lança ResourceNotFoundException se não encontrar.
     */
    @Transactional(readOnly = true)
    public ProfileResponse findById(Long id) {
        Profile profile = profileRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profile", id));
        return ProfileResponse.from(profile);
    }

    /**
     * Exclui um perfil pelo ID.
     * Lança ResourceNotFoundException se o perfil não existir.
     */
    @Transactional
    public void deleteById(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new ResourceNotFoundException("Profile", id);
        }
        profileRepository.deleteById(id);
    }
}

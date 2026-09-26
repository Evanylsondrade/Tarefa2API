package com.devshowcase.api.dto;

import com.devshowcase.api.entity.Technology;

/**
 * DTO de saída para Technology.
 */
public record TechnologyResponse(
    Long id,
    String name,
    String category,
    String iconUrl
) {

    public static TechnologyResponse from(Technology technology) {
        return new TechnologyResponse(
            technology.getId(),
            technology.getName(),
            technology.getCategory(),
            technology.getIconUrl()
        );
    }
}

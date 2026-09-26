package com.devshowcase.api.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidade que representa uma tecnologia (linguagem, framework, ferramenta).
 * Relacionamento: Technology N:N Project (lado inverso)
 */
@Entity
@Table(name = "technologies")
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    /** Categoria da tecnologia: LANGUAGE, FRAMEWORK, TOOL, DATABASE, etc. */
    @Column(length = 50)
    private String category;

    @Column(name = "icon_url", length = 255)
    private String iconUrl;

    /** Lado inverso do relacionamento N:N com Project */
    @ManyToMany(mappedBy = "technologies")
    private Set<Project> projects = new HashSet<>();

    public Technology() {
    }

    public Technology(Long id, String name, String category, String iconUrl, Set<Project> projects) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.iconUrl = iconUrl;
        if (projects != null) {
            this.projects = projects;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public static TechnologyBuilder builder() {
        return new TechnologyBuilder();
    }

    public static class TechnologyBuilder {
        private Long id;
        private String name;
        private String category;
        private String iconUrl;
        private Set<Project> projects = new HashSet<>();

        public TechnologyBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TechnologyBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TechnologyBuilder category(String category) {
            this.category = category;
            return this;
        }

        public TechnologyBuilder iconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
            return this;
        }

        public TechnologyBuilder projects(Set<Project> projects) {
            this.projects = projects;
            return this;
        }

        public Technology build() {
            return new Technology(id, name, category, iconUrl, projects);
        }
    }
}

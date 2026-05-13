package com.redteam.vulndb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Project Entity - Penetrasyon testi projelerini temsil eder.
 * Her proje birden fazla Vulnerability içerebilir (1:N ilişki).
 * Örnek: "Banka Web Uygulaması Pentest", "İç Ağ Taraması" vb.
 */
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Proje adı. Örnek: "Banka Web App Pentest Q1-2026" */
    @NotBlank(message = "Proje adı boş bırakılamaz")
    @Column(nullable = false)
    private String name;

    /** Proje açıklaması / kapsamı */
    @Column(length = 1000)
    private String description;

    /** Proje durumu: ACTIVE, COMPLETED, ARCHIVED */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status = ProjectStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Bir projenin birden fazla vulnerability'si olabilir (1:N).
     * mappedBy: İlişkinin sahibi Vulnerability entity'sindeki "project" alanıdır.
     * cascade ALL: Proje silindiğinde tüm vulnerability'leri de silinir.
     * orphanRemoval: Projeden çıkarılan vulnerability otomatik silinir.
     */
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vulnerability> vulnerabilities = new ArrayList<>();

    public Project() {}

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = ProjectStatus.ACTIVE;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ProjectStatus getStatus() { return status; }
    public void setStatus(ProjectStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<Vulnerability> getVulnerabilities() { return vulnerabilities; }
    public void setVulnerabilities(List<Vulnerability> vulnerabilities) { this.vulnerabilities = vulnerabilities; }
}

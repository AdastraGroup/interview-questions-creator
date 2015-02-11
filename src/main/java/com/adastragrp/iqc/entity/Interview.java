package com.adastragrp.iqc.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    @Size(max = 300)
    private String name;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotBlank
    @Size(max = 300)
    private String evaluationDescription;

    @OneToMany(mappedBy = "interview")
    private Set<Question> questions = new HashSet<>();

    @NotNull
    private boolean descriptionMarkup;

    @NotNull
    private boolean evaluationDescriptionMarkup;

    private LocalDateTime created;



    @PrePersist
    private void prePersist() {
        created = LocalDateTime.now();
    }


    //<editor-fold desc="Getters&Setters">
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEvaluationDescription() {
        return evaluationDescription;
    }

    public void setEvaluationDescription(String evaluationDescription) {
        this.evaluationDescription = evaluationDescription;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public boolean isDescriptionMarkup() {
        return descriptionMarkup;
    }

    public void setDescriptionMarkup(boolean isDescriptionMarkup) {
        this.descriptionMarkup = isDescriptionMarkup;
    }

    public boolean isEvaluationDescriptionMarkup() {
        return evaluationDescriptionMarkup;
    }

    public void setEvaluationDescriptionMarkup(boolean isEvaluationDescriptionMarkup) {
        this.evaluationDescriptionMarkup = isEvaluationDescriptionMarkup;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
    //</editor-fold>
}

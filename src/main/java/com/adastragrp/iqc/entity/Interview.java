package com.adastragrp.iqc.entity;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.rest.core.config.Projection;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Interview {

    public static enum State {DRAFT, FINISHED, BEING_APPROVED, APPROVED, PUBLISHED, ASSIGNED, COMPLETED}

    //<editor-fold desc="Projections">
    @Projection(name = "InlineQuestions", types = {Interview.class})
    public static interface InlineQuestions {

        long getId();

        String getName();

        String getDescription();

        String getEvaluationDescription();

        Set<Question> getQuestions();

        boolean isDescriptionMarkup();

        boolean isEvaluationDescriptionMarkup();

        State getState();

        LocalDateTime getCreated();
    }
    //</editor-fold>

    //<editor-fold desc="Attributes">
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

    @OneToMany(mappedBy = "interview", fetch = FetchType.EAGER)
    private Set<Question> questions = new HashSet<>();

    @NotNull
    private boolean descriptionMarkup;

    @NotNull
    private boolean evaluationDescriptionMarkup;

    @Enumerated(EnumType.STRING)
    private State state;

    private LocalDateTime created;
    //</editor-fold>

    @PrePersist
    private void prePersist() {
        this.created = LocalDateTime.now();
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

    public void setDescriptionMarkup(boolean descriptionMarkup) {
        this.descriptionMarkup = descriptionMarkup;
    }

    public boolean isEvaluationDescriptionMarkup() {
        return evaluationDescriptionMarkup;
    }

    public void setEvaluationDescriptionMarkup(boolean evaluationDescriptionMarkup) {
        this.evaluationDescriptionMarkup = evaluationDescriptionMarkup;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    /*
    public void setCreated(LocalDateTime created) {             // should not be able to setup - it is set once in prepersist block
        this.created = created;
    }
    */

    //</editor-fold>
}

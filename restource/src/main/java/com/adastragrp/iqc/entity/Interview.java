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
    @OrderBy("position ASC")
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


    //<editor-fold desc="Equals&HashCode">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interview)) return false;

        Interview interview = (Interview) o;

        if (id != interview.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
    //</editor-fold>


    //<editor-fold desc="Getters&Setters">
    public long getId() {
        return id;
    }

    public Interview setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Interview setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Interview setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getEvaluationDescription() {
        return evaluationDescription;
    }

    public Interview setEvaluationDescription(String evaluationDescription) {
        this.evaluationDescription = evaluationDescription;
        return this;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public Interview setQuestions(Set<Question> questions) {
        this.questions = questions;
        return this;
    }

    public boolean isDescriptionMarkup() {
        return descriptionMarkup;
    }

    public Interview setDescriptionMarkup(boolean descriptionMarkup) {
        this.descriptionMarkup = descriptionMarkup;
        return this;
    }

    public boolean isEvaluationDescriptionMarkup() {
        return evaluationDescriptionMarkup;
    }

    public Interview setEvaluationDescriptionMarkup(boolean evaluationDescriptionMarkup) {
        this.evaluationDescriptionMarkup = evaluationDescriptionMarkup;
        return this;
    }

    public State getState() {
        return state;
    }

    public Interview setState(State state) {
        this.state = state;
        return this;
    }

    public LocalDateTime getCreated() {
        return created;
    }
                                                                /*
    public void setCreated(LocalDateTime created) {             // should not be able to setup - it is set once in prepersist method
        this.created = created;
    }                                                           */
    //</editor-fold>
}

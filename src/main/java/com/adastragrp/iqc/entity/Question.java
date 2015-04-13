package com.adastragrp.iqc.entity;

import org.springframework.data.rest.core.config.Projection;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Question {

    public static enum QuestionType {CHECKBOX, RADIO, TEXT_AREA}

    //<editor-fold desc="Projections">
    @Projection(name = "InlineAnswers", types = {Question.class})
    public static interface InlineAnswers {

        long getId();

        String getText();

        QuestionType getQuestionType();

        Set<Answer> getAnswers();
    }
    //</editor-fold>

    //<editor-fold desc="Attributes">
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Interview interview;

    @NotNull
    private String text;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, orphanRemoval=true)
    private Set<Answer> answers = new HashSet<>();
    //</editor-fold>

    //<editor-fold desc="Equals&HashCode">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;

        Question question = (Question) o;

        if (id != question.id) return false;

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

    public void setId(long id) {
        this.id = id;
    }

    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }


    //</editor-fold>

}

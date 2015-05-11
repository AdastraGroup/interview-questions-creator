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

        String getPrivateText();

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

    private String privateText;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType = QuestionType.CHECKBOX;

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

    public Question setId(long id) {
        this.id = id;
        return this;
    }

    public Interview getInterview() {
        return interview;
    }

    public Question setInterview(Interview interview) {
        this.interview = interview;
        return this;
    }

    public String getText() {
        return text;
    }

    public Question setText(String text) {
        this.text = text;
        return this;
    }

    public String getPrivateText() {
        return privateText;
    }

    public Question setPrivateText(String privateText) {
        this.privateText = privateText;
        return this;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public Question setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
        return this;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public Question setAnswers(Set<Answer> answers) {
        this.answers = answers;
        return this;
    }


    //</editor-fold>

}

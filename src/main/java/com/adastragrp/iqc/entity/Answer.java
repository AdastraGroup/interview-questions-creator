package com.adastragrp.iqc.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Answer {

    //<editor-fold desc="Attributes">
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    @Size(max = 200)
    private String text;

    private boolean textMarkup = false;

    @NotNull
    private Boolean right;

    @ManyToOne
    Question question;
    //</editor-fold>

    //<editor-fold desc="Getters&Setters">

    public long getId() {
        return id;
    }

    public Answer setId(long id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return text;
    }

    public Answer setText(String text) {
        this.text = text;
        return this;
    }

    public boolean isTextMarkup() {
        return textMarkup;
    }

    public Answer setTextMarkup(boolean textMarkup) {
        this.textMarkup = textMarkup;
        return this;
    }

    public Boolean getRight() {
        return right;
    }

    public Answer setRight(Boolean right) {
        this.right = right;
        return this;
    }

    public Question getQuestion() {
        return question;
    }

    public Answer setQuestion(Question question) {
        this.question = question;
        return this;
    }


    //</editor-fold>

    //<editor-fold desc="Equals&HashCode">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer)) return false;

        Answer answer = (Answer) o;

        if (id != answer.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
    //</editor-fold>
}

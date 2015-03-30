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

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isTextMarkup() {
        return textMarkup;
    }

    public void setTextMarkup(boolean textMarkup) {
        this.textMarkup = textMarkup;
    }

    public Boolean getRight() {
        return right;
    }

    public void setRight(Boolean right) {
        this.right = right;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }


    //</editor-fold>

}

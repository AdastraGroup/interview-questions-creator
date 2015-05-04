package com.adastragrp.iqc.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Answer {

    //<editor-fold desc="Attributes">
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Size(max = 200)
    private String text;

    private boolean textMarkup = false;

    private boolean right = false;

    private boolean chosen = false;

    @JsonIgnore
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

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
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

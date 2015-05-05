package com.adastragrp.iqc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class QuestionNotFoundException extends RuntimeException{

    public QuestionNotFoundException(Long questionId) {
        super("could not find question with id: '" + questionId + "'.");
    }

}

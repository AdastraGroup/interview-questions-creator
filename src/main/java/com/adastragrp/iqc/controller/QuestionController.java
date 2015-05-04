package com.adastragrp.iqc.controller;

import com.adastragrp.iqc.entity.Answer;
import com.adastragrp.iqc.entity.Question;
import com.adastragrp.iqc.repository.AnswerRepository;
import com.adastragrp.iqc.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.adastragrp.iqc.entity.Question.QuestionType.TEXT_AREA;

@RestController
@RequestMapping("/api/questions/{id}")
public class QuestionController {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;


    @RequestMapping(value = "/textAnswer", method = RequestMethod.POST)
    @Transactional
    Answer makeTextAnswerForQuestion(@PathVariable("id") Long questionId) {
        Question question = questionRepository.findOne(questionId);

        if (question == null) {
            //map exception to http error
        }

        answerRepository.deleteAllByQuestionId(questionId);
        Answer a = new Answer();
        a.setQuestion(question);
        a.setText("");                                                  // placeholder on frontend will be displayed
        answerRepository.save(a);

        question.setQuestionType(TEXT_AREA);
        questionRepository.save(question);

        return a;
    }


    //<editor-fold desc="Getters&Setters">
    public AnswerRepository getAnswerRepository() {
        return answerRepository;
    }

    public void setAnswerRepository(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }
    //</editor-fold>
}

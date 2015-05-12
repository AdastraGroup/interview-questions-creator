package com.adastragrp.iqc.controller;

import com.adastragrp.iqc.entity.Answer;
import com.adastragrp.iqc.entity.Question;
import com.adastragrp.iqc.exception.NotFoundException;
import com.adastragrp.iqc.repository.AnswerRepository;
import com.adastragrp.iqc.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        throwNotFoundExceptionOnNull(question, Question.class, questionId);

        answerRepository.deleteAllByQuestionId(questionId);
        Answer a = new Answer();
        a.setQuestion(question);
        a.setText("");                                                  // placeholder on frontend will be displayed
        answerRepository.save(a);

        question.setQuestionType(TEXT_AREA);
        questionRepository.save(question);

        return a;
    }


    @RequestMapping(value = "/answers/{answerId}/right", method = RequestMethod.POST)
    @Transactional
    void setAnswerRight(@PathVariable("id") Long questionId, @PathVariable("answerId") Long answerId) {
        Question question = questionRepository.findOne(questionId);
        Answer answer = answerRepository.findOne(answerId);

        throwNotFoundExceptionOnNull(answer, Answer.class, answerId);
        throwNotFoundExceptionOnNull(question, Question.class, questionId);

        question.getAnswers().stream().forEach(
                a -> {a.setRight(a.getId() == answerId);}
        );

        answerRepository.save(question.getAnswers());
    }



    private void throwNotFoundExceptionOnNull(Object o, Class clazz, Long id) {

        if(o == null){
            log.error("Could not find "+ clazz.getCanonicalName() +" with id: '" + id + "'.");
            throw new NotFoundException(id, clazz);
        }
    }

    protected final static transient Logger log = LoggerFactory.getLogger(QuestionController.class);
}

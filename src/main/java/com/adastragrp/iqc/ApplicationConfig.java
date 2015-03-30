package com.adastragrp.iqc;

import com.adastragrp.iqc.entity.Answer;
import com.adastragrp.iqc.entity.Interview;
import com.adastragrp.iqc.entity.Question;
import com.adastragrp.iqc.repository.AnswerRepository;
import com.adastragrp.iqc.repository.InterviewRepository;
import com.adastragrp.iqc.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@EnableAutoConfiguration
public class ApplicationConfig {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    InterviewRepository interviewRepository;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfig.class, args);
    }

    @PostConstruct
    public void populateDB() {

        Interview interview1 = new Interview();
        interview1.setName("Interview for Java Spring programmer");
        interview1.setDescription("This interview is supposed to test knowledge of spring especially spring-boot and spring-rest.");
        interview1.setState(Interview.State.DRAFT);
        interview1.setEvaluationDescription("You have 60 minutes for complete the test. In multi-choice question the wrong answers are evaluated by negative points ");
        interviewRepository.save(interview1);

        Interview interview2 = new Interview();
        interview2.setName("Interview for Adastra CEO");
        interview2.setDescription("This interview is supposed to test knowledge of spring especially spring-boot and spring-rest.");
        interview2.setState(Interview.State.DRAFT);
        interview2.setEvaluationDescription("You have 60 minutes for complete the test. In multi-choice question the wrong answers are evaluated by negative points ");
        interviewRepository.save(interview2);


        Question q1 = new Question();
        q1.setText("What's new in Java 8");
        q1.setQuestionType(Question.QuestionType.MULTI_CHOICE);
        q1.setInterview(interview1);
        questionRepository.save(q1);


        Answer a1 = new Answer();
        a1.setText("lambdas");
        a1.setRight(true);
        a1.setQuestion(q1);

        Answer a2 = new Answer();
        a2.setText("exception multi-catch");
        a2.setRight(true);
        a2.setQuestion(q1);

        answerRepository.save(Arrays.asList(a1, a2));
    }

}
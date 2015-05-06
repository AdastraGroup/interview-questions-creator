package com.adastragrp.iqc;

import com.adastragrp.iqc.entity.Answer;
import com.adastragrp.iqc.entity.Question;
import com.adastragrp.iqc.repository.AnswerRepository;
import com.adastragrp.iqc.repository.QuestionRepository;
import com.jayway.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static com.jayway.restassured.RestAssured.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationConfig.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class OrphanRemovalIT {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Value("${local.server.port}")
    private int serverPort;

    private Question q1;
    private Answer a1;
    private Answer a2;

    @Before
    public void setUp() throws Exception {
        q1 = new Question();
        q1.setText("What's new in Java 8");
        q1.setQuestionType(Question.QuestionType.CHECKBOX);
        questionRepository.save(q1);

        a1 = new Answer();
        a1.setText("lambdas");
        a1.setRight(true);
        a1.setQuestion(q1);

        a2 = new Answer();
        a2.setText("exception multi-catch");
        a2.setRight(true);
        a2.setQuestion(q1);
        answerRepository.save(Arrays.asList(a1, a2));

        RestAssured.port = serverPort;
    }

    @Test
    public void orphanRemoval() {
        when()
                .get("/api/answers/{id}", a1.getId())
                .then()
                .statusCode(HttpStatus.SC_OK);

        when()
                .get("/api/answers/{id}", a2.getId())
                .then()
                .statusCode(HttpStatus.SC_OK);

        when()
                .delete("/api/questions/{id}", q1.getId())
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        when()
                .get("/api/answers/{id}", a1.getId())
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);

        when()
                .get("/api/answers/{id}", a2.getId())
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);

    }

    @Test
    @Transactional
    public void orphanRemovalByRepository() {
        questionRepository.delete(q1);
    }
}
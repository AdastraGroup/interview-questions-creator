package com.adastragrp.iqc;

import com.adastragrp.iqc.entity.Answer;
import com.adastragrp.iqc.entity.Interview;
import com.adastragrp.iqc.entity.Question;
import com.adastragrp.iqc.init.TestDataPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import javax.annotation.PostConstruct;
import java.net.URI;

@EnableAutoConfiguration
@Import(RepositoryRestMvcConfiguration.class)
@Configuration
@ComponentScan
public class ApplicationConfig extends RepositoryRestMvcConfiguration {

    @Autowired
    private TestDataPopulator testDataPopulator;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationConfig.class, args);
    }

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Answer.class, Interview.class, Question.class);
        config.setReturnBodyOnCreate(true);
        config.setReturnBodyOnUpdate(true);
        config.setBaseUri(URI.create("/api"));
    }

    @PostConstruct
    public void afterConfig() {
        testDataPopulator.init();
    }
}
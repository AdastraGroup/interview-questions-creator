package com.adastragrp.iqc;

import com.adastragrp.iqc.entity.Answer;
import com.adastragrp.iqc.entity.Interview;
import com.adastragrp.iqc.entity.Question;
import com.adastragrp.iqc.init.TestDataPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@EnableAutoConfiguration
@Import(RepositoryRestMvcConfiguration.class)
@Configuration
@RestController
@ComponentScan
@EnableRedisHttpSession
public class RestApiApplication extends RepositoryRestMvcConfiguration {

    @Autowired
    private TestDataPopulator testDataPopulator;

    public static void main(String[] args) {
        SpringApplication.run(RestApiApplication.class, args);
    }

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Answer.class, Interview.class, Question.class);
        config.setReturnBodyOnCreate(true);
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "{\"message\":\"you called protected rest endpoint!!!\"}";
    }


    @PostConstruct
    public void afterConfig() {
        testDataPopulator.init();
    }

    @Configuration
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.httpBasic().disable();
            http.authorizeRequests().anyRequest().authenticated();
        }

    }
}
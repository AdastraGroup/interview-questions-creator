package com.adastragrp.iqc.repository;

import com.adastragrp.iqc.entity.Question;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = Question.InlineAnswers.class)
public interface QuestionRepository extends PagingAndSortingRepository<Question, Long> {
}

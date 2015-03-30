package com.adastragrp.iqc.repository;


import com.adastragrp.iqc.entity.Answer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AnswerRepository extends PagingAndSortingRepository<Answer, Long> {

}

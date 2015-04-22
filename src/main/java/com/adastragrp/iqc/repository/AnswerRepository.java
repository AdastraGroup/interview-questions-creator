package com.adastragrp.iqc.repository;


import com.adastragrp.iqc.entity.Answer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@RepositoryRestResource
public interface AnswerRepository extends PagingAndSortingRepository<Answer, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Answer a WHERE a.id <> :answerId AND a.question.id = :questionId)")
    public void deleteAllExceptOne(@Param("answerId") Long answerId, @Param("questionId") Long questionId);

}

package com.adastragrp.iqc.repository;

import com.adastragrp.iqc.entity.Interview;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Set;

@RepositoryRestResource
public interface InterviewRepository extends PagingAndSortingRepository<Interview, Long> {

    @Override
    @RestResource(exported = false)
    void delete(Long id);

    @Override
    @RestResource(exported = false)
    void delete(Interview entity);

    @Query("SELECT i FROM Interview i WHERE i.state = :state)")
    public Set<Interview> find(@Param("state") Interview.State state);

    public Set<Interview> findByState(@Param("state") Interview.State state);
}

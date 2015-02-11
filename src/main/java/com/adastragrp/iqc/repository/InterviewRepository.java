package com.adastragrp.iqc.repository;

import com.adastragrp.iqc.entity.Interview;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface InterviewRepository extends PagingAndSortingRepository<Interview, Long> {
}

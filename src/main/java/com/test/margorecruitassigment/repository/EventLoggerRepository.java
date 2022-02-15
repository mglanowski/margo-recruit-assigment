package com.test.margorecruitassigment.repository;

import com.test.margorecruitassigment.model.AppEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLoggerRepository extends CrudRepository<AppEvent, String> {
}

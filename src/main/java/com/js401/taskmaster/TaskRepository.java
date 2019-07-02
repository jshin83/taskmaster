package com.js401.taskmaster;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface TaskRepository extends CrudRepository<Task, String> {
    Task findByid(String id);

    Task findByTitleAndDescription(String title, String description);
}
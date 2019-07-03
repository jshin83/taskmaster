package com.js401.taskmaster;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface TaskRepository extends CrudRepository<Task, String> {
    Task findByid(String id);
    List<Task> findByAssignee(String name);
    Task findByTitleAndDescription(String title, String description);
}
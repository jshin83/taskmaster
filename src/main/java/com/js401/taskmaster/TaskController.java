package com.js401.taskmaster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TaskController {
    @Autowired
    TaskRepository taskrepo;

    @GetMapping({"/"})
    public String helloWorld() {
        return "hello world";
    }

    @GetMapping({"/tasks"})
    public ResponseEntity<Iterable<Task>> findAllTasks() {
        return ResponseEntity.ok(this.taskrepo.findAll());
    }

    @PostMapping({"/tasks/{title}/{description}"})
    public ResponseEntity<Task> saveNewTask(@PathVariable String title, @PathVariable String description) {
        Task taskToSave = new Task(title, description);
        Task savedTask = (Task)this.taskrepo.save(taskToSave);
        return ResponseEntity.ok(savedTask);
    }

    @PostMapping({"/tasks/{id}/state"})
    public ResponseEntity<Task> findById(@PathVariable String id) {
        Task taskToUpdate = this.taskrepo.findByid(id);
        if (taskToUpdate != null) {
            taskToUpdate.setStatus(taskToUpdate.changeStatus(taskToUpdate.getStatus()));
            this.taskrepo.save(taskToUpdate);
        }

        return ResponseEntity.ok(taskToUpdate);
    }
}
package com.js401.taskmaster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class TaskController {
    @Autowired
    TaskRepository taskrepo;


    /**
     * Home route
     * @return String "hello world"
     */
    @GetMapping({"/"})
    public String helloWorld() {
        return "hello world";
    }

    /**
     * Returns all Tasks in DB
     * @return List, all stored Tasks
     */
    @GetMapping({"/tasks"})
    public ResponseEntity<Iterable<Task>> findAllTasks() {
        return ResponseEntity.ok(this.taskrepo.findAll());
    }

    /**
     * Saves a new task
     * @param title String, task title
     * @param description, String description of task
     * @param assignee String, name of assigned person - could be null or empty
     * @return Task object json, newly saved task
     */
    @PostMapping({"/tasks/{title}/{description}/{assignee}"})
    public ResponseEntity<Task> saveNewTask(@PathVariable String title, @PathVariable String description,
                                            @PathVariable String assignee) {
        Task taskToSave = new Task(title, description, assignee);
        Task savedTask = this.taskrepo.save(taskToSave);
        return ResponseEntity.ok(savedTask);
    }

    /**
     * Add new task via request body
     * @param newTask Task, object to add
     * @return newly added Task
     */
    @PostMapping({"/tasks"})
    public ResponseEntity<Task> saveTask(@RequestBody Task newTask) {
        Task savedTask = this.taskrepo.save(newTask);
        return ResponseEntity.ok(savedTask);
    }

    /**
     * Returns all tasks assigned to a user
     * @param name String, user assigned to tasks
     * @return List, all tasks assigned to the user
     */
    @GetMapping("/users/{name}/tasks")
    public ResponseEntity<Iterable<Task>> findByAssignedTasks(@PathVariable String name) {
        return ResponseEntity.ok(taskrepo.findByAssignee(name));
    }

    /**
     * Advances status of a task ONLY IF THERE IS AN ASSIGNEE
     * @param id String, Task id
     * @return updated Task object
     */
    @PutMapping({"/tasks/{id}/state"})
    public ResponseEntity<Task> findById(@PathVariable String id) {
        Task taskToUpdate = this.taskrepo.findByid(id);
        if (taskToUpdate != null) {
            taskToUpdate.setStatus(taskToUpdate.changeStatus(taskToUpdate.getStatus()));
            this.taskrepo.save(taskToUpdate);
        }

        return ResponseEntity.ok(taskToUpdate);
    }

    /**
     * Updates an assignee if the assignee is not being reassigned to the same task
     * @param id String, Task id
     * @param assignee String, name to assign to task
     * @return Task object, updated Task
     */
    @PutMapping({"/tasks/{id}/assign/{assignee}"})
    public ResponseEntity<Task> assignTask(@PathVariable String id, @PathVariable String assignee) {
        Task taskToUpdate = this.taskrepo.findById(id).get();
        if (!(assignee.equals("")) && !(assignee.equals(taskToUpdate.getAssignee()))) {
            taskToUpdate.setAssignee(assignee);
            taskToUpdate.setStatus("assigned");
            this.taskrepo.save(taskToUpdate);
        }
        return ResponseEntity.ok(taskToUpdate);
    }

    /**
     * Deletes a Task
     * @param id String, Task id
     * @return String, task was deleted
     */
    @DeleteMapping({"tasks/delete/{id}"})
    public ResponseEntity<String> deleteTask(@PathVariable String id) {
        this.taskrepo.delete(this.taskrepo.findByid(id));
        return ResponseEntity.ok("Task was deleted");
    }
}
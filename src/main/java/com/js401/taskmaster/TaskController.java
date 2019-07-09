package com.js401.taskmaster;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
public class TaskController {
    @Autowired
    TaskRepository taskrepo;

    private S3Client s3Client;

    @Autowired
    TaskController(S3Client s3Client) {
        this.s3Client = s3Client;
    }

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
     * Retrieves one Task by its id
     * @param id String, Task id
     * @return Task
     */
    @GetMapping({"/tasks/{id}"})
    public ResponseEntity<Task> getOneTask(@PathVariable String id) {
        Task task = taskrepo.findByid(id);
        return ResponseEntity.ok(task);
    }

    /**
     * Updates an existing task image
     * @param id String, Task id
     * @param file File, image
     * @return Task
     */
    @PostMapping({"/tasks/{id}/images"})
    public ResponseEntity<Task> addImage(@PathVariable String id, @RequestPart(value = "file") MultipartFile file) {
        String url = this.s3Client.uploadFile(file);
        Task task = taskrepo.findByid(id);
        task.setUrl(url);
        taskrepo.save(task);
        return ResponseEntity.ok(task);
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
        //check if assignee exists and change status if status is empty
        if(newTask.getAssignee() == null || newTask.getAssignee().equals("")) {
            newTask.setStatus("available");
        } else if (newTask.getStatus() == null || newTask.getStatus().equals("")){
            newTask.setStatus("assigned");
        }
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
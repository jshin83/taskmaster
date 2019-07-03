# TaskMaster

## What is this app
Manages tasks with 4 states for status and an assignee

## Routes

    + @GetMapping({"/tasks"}) -> returns all tasks in db
    + @PostMapping({"/tasks/{title}/{description}"}) -> adds a new task
    + @PutMapping({"/tasks/{id}/state"}) -> updates state
        + this route will not change the assignment if there is no assignee (null or empty String)
    + @PutMapping({"/tasks/{id}/assign/{assignee}"}) -> updates Task to assignee
        + this route will not change the assignee if the same one was already assigned
        + this route will change the status to "assigned" if the Task is updated
    + @DeleteMapping({"tasks/delete/{id}"}) -> deletes a Task by id


## Issues
    + dependencies
    + needed to use Postman, as using local means my routes could only be Get
    + GIT ISSUES - user error, but frustrating
    + needed to have region specified
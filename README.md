# TaskMaster

## What is this app
Manages tasks with 4 states for status

## Routes

    + @GetMapping({"/tasks"}) -> returns all tasks in db
    + @PostMapping({"/tasks/{title}/{description}"}) -> adds a new task
    + @PostMapping({"/tasks/{id}/state"}) -> updates state

## Issues
    + dependencies
    + needed to use Postman, as using local means my routes could only be Get
    + GIT ISSUES - user error, but frustrating
    + needed to have region specified
package com.js401.taskmaster;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(
        tableName = "Task"
)
public class Task {
    private String id;
    private String title;
    private String description;
    private String status = "available";

    public Task() {
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    public String getId() {
        return this.id;
    }

    @DynamoDBAttribute
    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute
    public String getTitle() {
        return this.title;
    }

    @DynamoDBAttribute
    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBAttribute
    public String getDescription() {
        return this.description;
    }

    @DynamoDBAttribute
    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute
    public String getStatus() {
        return this.status;
    }

    @DynamoDBAttribute
    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return "Task{id='" + this.id + '\'' + ", title='" + this.title + '\'' + ", description='" + this.description + '\'' + ", status='" + this.status + '\'' + '}';
    }

    String changeStatus(String status) {
        if (status.equals("available")) {
            status = "assigned";
        } else if (status.equals("assigned")) {
            status = "accepted";
        } else if (status.equals("accepted")) {
            status = "finished";
        }

        return status;
    }
}

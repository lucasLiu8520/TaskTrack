package com.example.tasktrack.model;

// Using a separate request class here
// as when creating a task, the Android app should only send title and description
// while Task represents a full backend-returned task, including 
// id, status, and project_id

public class TaskCreateRequest {
    private String title;
    private String description;

    public TaskCreateRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
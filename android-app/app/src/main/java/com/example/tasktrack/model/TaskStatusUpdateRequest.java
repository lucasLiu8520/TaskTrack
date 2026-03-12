package com.example.tasktrack.model;

// This is just a request body model

public class TaskStatusUpdateRequest {
    private String status;

    public TaskStatusUpdateRequest(String status) {
        this.status = status;
    }
}
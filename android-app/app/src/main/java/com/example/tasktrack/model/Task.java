package com.example.tasktrack.model;

public class Task {
    private int id;
    private String title;
    private String description;
    private String status;
    private int project_id;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public int getProject_id() {
        return project_id;
    }
}
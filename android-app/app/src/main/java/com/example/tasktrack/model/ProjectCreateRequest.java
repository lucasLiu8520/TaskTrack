package com.example.tasktrack.model;

public class ProjectCreateRequest {
    private String name;
    private String description;

    public ProjectCreateRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
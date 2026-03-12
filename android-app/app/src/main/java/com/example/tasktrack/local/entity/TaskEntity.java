package com.example.tasktrack.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class TaskEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String description;
    private String status;
    private int projectId;

    public TaskEntity(String title, String description, String status, int projectId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.projectId = projectId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getProjectId() {
        return projectId;
    }
}
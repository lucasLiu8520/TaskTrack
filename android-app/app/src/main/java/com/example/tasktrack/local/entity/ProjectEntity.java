package com.example.tasktrack.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "projects")
// Tells Room this class maps to a table called projects
public class ProjectEntity {

    @PrimaryKey(autoGenerate = true)
    // Lets Room generate project IDs automatically, similar to what the backend previously does
    private int id;

    private String name;
    private String description;

    public ProjectEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }        public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
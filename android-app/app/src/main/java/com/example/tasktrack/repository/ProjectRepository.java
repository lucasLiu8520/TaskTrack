package com.example.tasktrack.repository;

import com.example.tasktrack.model.Project;
import java.util.List;

public interface ProjectRepository {
    void getProjects(DataCallback<List<Project>> callback);
    void createProject(String name, String description, DataCallback<Project> callback);
}
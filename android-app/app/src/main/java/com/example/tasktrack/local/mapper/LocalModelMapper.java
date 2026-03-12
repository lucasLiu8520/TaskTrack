package com.example.tasktrack.local.mapper;

import com.example.tasktrack.local.entity.ProjectEntity;
import com.example.tasktrack.local.entity.TaskEntity;
import com.example.tasktrack.model.Project;
import com.example.tasktrack.model.Task;

import java.lang.reflect.Field;

public class LocalModelMapper {

    public static Project toProject(ProjectEntity entity) {
        Project project = new Project();
        setField(project, "id", entity.getId());
        setField(project, "name", entity.getName());
        setField(project, "description", entity.getDescription());
        return project;
    }

    public static Task toTask(TaskEntity entity) {
        Task task = new Task();
        setField(task, "id", entity.getId());
        setField(task, "title", entity.getTitle());
        setField(task, "description", entity.getDescription());
        setField(task, "status", entity.getStatus());
        setField(task, "project_id", entity.getProjectId());
        return task;
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to map field: " + fieldName, e);
        }
    }
    // current Project and Task classes only have: private fields, getters, no setters
    // no public constructors with all fields.
    // To avoid rewriting those model classes right now, use reflection to populate them
    // Later, to improve, can try
    // empty constructor
    // setters
    // or full constructor
}
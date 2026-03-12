package com.example.tasktrack.repository;

import com.example.tasktrack.model.Task;
import java.util.List;

public interface TaskRepository {
    void getTasksForProject(int projectId, DataCallback<List<Task>> callback);
    void createTask(int projectId, String title, String description, DataCallback<Task> callback);
    void updateTaskStatus(int taskId, String status, DataCallback<Task> callback);
}
package com.example.tasktrack.repository;

import com.example.tasktrack.config.AppConfig;
import com.example.tasktrack.config.AppMode;

public class RepositoryProvider {

    public static ProjectRepository getProjectRepository() {
        if (AppConfig.CURRENT_MODE == AppMode.REMOTE) {
            return new RemoteProjectRepository();
        }

        // Local implementation will be added later
        return new RemoteProjectRepository();
    }

    public static TaskRepository getTaskRepository() {
        if (AppConfig.CURRENT_MODE == AppMode.REMOTE) {
            return new RemoteTaskRepository();
        }

        // Local implementation will be added later
        return new RemoteTaskRepository();
    }
}
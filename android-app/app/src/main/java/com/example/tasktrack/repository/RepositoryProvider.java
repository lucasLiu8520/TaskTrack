package com.example.tasktrack.repository;

import android.content.Context;

import com.example.tasktrack.config.AppConfig;
import com.example.tasktrack.config.AppMode;

public class RepositoryProvider {
    // RepositoryProvider accept Context and pass it through

    public static ProjectRepository getProjectRepository(Context context) {
        if (AppConfig.CURRENT_MODE == AppMode.REMOTE) {
            return new RemoteProjectRepository();
        }

        return new LocalProjectRepository(context);
    }

    public static TaskRepository getTaskRepository(Context context) {
        if (AppConfig.CURRENT_MODE == AppMode.REMOTE) {
            return new RemoteTaskRepository();
        }

        return new LocalTaskRepository(context);
    }
    
}
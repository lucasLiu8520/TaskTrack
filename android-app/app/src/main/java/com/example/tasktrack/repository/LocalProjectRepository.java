package com.example.tasktrack.repository;

import android.content.Context;

import com.example.tasktrack.local.dao.ProjectDao;
import com.example.tasktrack.local.db.DatabaseProvider;
import com.example.tasktrack.local.entity.ProjectEntity;
import com.example.tasktrack.local.mapper.LocalModelMapper;
import com.example.tasktrack.model.Project;

import java.util.ArrayList;
import java.util.List;

public class LocalProjectRepository implements ProjectRepository {

    private final Context context;
    // Room database access needs Android Context
    // the reason local repositories need the app context, while remote repositories do not

    public LocalProjectRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void getProjects(DataCallback<List<Project>> callback) {
        new Thread(() -> {
            try {
                ProjectDao projectDao = DatabaseProvider.getDatabase(context).projectDao();
                List<ProjectEntity> entities = projectDao.getAllProjects();

                List<Project> projects = new ArrayList<>();
                for (ProjectEntity entity : entities) {
                    projects.add(LocalModelMapper.toProject(entity));
                }

                callback.onSuccess(projects);
            } catch (Exception e) {
                callback.onError("Local database error: " + e.getMessage());
            }
        }).start();
    }

    @Override
    public void createProject(String name, String description, DataCallback<Project> callback) {
        new Thread(() -> {
            try {
                ProjectDao projectDao = DatabaseProvider.getDatabase(context).projectDao();

                ProjectEntity entity = new ProjectEntity(name, description);
                long insertedId = projectDao.insertProject(entity);
                entity.setId((int) insertedId);

                Project project = LocalModelMapper.toProject(entity);
                callback.onSuccess(project);
            } catch (Exception e) {
                callback.onError("Local database error: " + e.getMessage());
            }
        }).start();
    }
}
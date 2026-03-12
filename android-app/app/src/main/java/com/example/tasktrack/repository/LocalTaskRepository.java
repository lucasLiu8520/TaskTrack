package com.example.tasktrack.repository;

import android.content.Context;

import com.example.tasktrack.local.dao.TaskDao;
import com.example.tasktrack.local.db.DatabaseProvider;
import com.example.tasktrack.local.entity.TaskEntity;
import com.example.tasktrack.local.mapper.LocalModelMapper;
import com.example.tasktrack.model.Task;

import java.util.ArrayList;
import java.util.List;

public class LocalTaskRepository implements TaskRepository {

    private final Context context;

    public LocalTaskRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void getTasksForProject(int projectId, DataCallback<List<Task>> callback) {
        new Thread(() -> {
            try {
                TaskDao taskDao = DatabaseProvider.getDatabase(context).taskDao();
                List<TaskEntity> entities = taskDao.getTasksForProject(projectId);

                List<Task> tasks = new ArrayList<>();
                for (TaskEntity entity : entities) {
                    tasks.add(LocalModelMapper.toTask(entity));
                }

                callback.onSuccess(tasks);
            } catch (Exception e) {
                callback.onError("Local database error: " + e.getMessage());
            }
        }).start();
    }

    @Override
    public void createTask(int projectId, String title, String description, DataCallback<Task> callback) {
        new Thread(() -> {
            try {
                TaskDao taskDao = DatabaseProvider.getDatabase(context).taskDao();

                TaskEntity entity = new TaskEntity(title, description, "TODO", projectId);
                long insertedId = taskDao.insertTask(entity);
                entity.setId((int) insertedId);

                Task task = LocalModelMapper.toTask(entity);
                callback.onSuccess(task);
            } catch (Exception e) {
                callback.onError("Local database error: " + e.getMessage());
            }
        }).start();
    }

    @Override
    public void updateTaskStatus(int taskId, String status, DataCallback<Task> callback) {
        new Thread(() -> {
            try {
                TaskDao taskDao = DatabaseProvider.getDatabase(context).taskDao();
                taskDao.updateTaskStatus(taskId, status);

                // Re-read all tasks is unnecessary here; return a lightweight success result
                Task updatedTask = new Task();
                callback.onSuccess(updatedTask);
                // This is only to satisfy the current callback contract, because the Activity does not actually
                // use the returned task object after success
                // it just
                // shows a toast
                // reloads tasks
                // Later can be improved by
                // adding getTaskById() to TaskDao
                // rereading the updated task properly
            } catch (Exception e) {
                callback.onError("Local database error: " + e.getMessage());
            }
        }).start();
    }
}
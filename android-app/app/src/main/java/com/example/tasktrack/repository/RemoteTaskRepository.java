package com.example.tasktrack.repository;

import com.example.tasktrack.model.Task;
import com.example.tasktrack.model.TaskCreateRequest;
import com.example.tasktrack.model.TaskStatusUpdateRequest;
import com.example.tasktrack.network.ApiClient;
import com.example.tasktrack.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteTaskRepository implements TaskRepository {

    private final ApiService apiService = ApiClient.getApiService();

    @Override
    public void getTasksForProject(int projectId, DataCallback<List<Task>> callback) {
        apiService.getTasksForProject(projectId).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to load tasks. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void createTask(int projectId, String title, String description, DataCallback<Task> callback) {
        TaskCreateRequest request = new TaskCreateRequest(title, description);

        apiService.createTask(projectId, request).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to create task. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void updateTaskStatus(int taskId, String status, DataCallback<Task> callback) {
        TaskStatusUpdateRequest request = new TaskStatusUpdateRequest(status);

        apiService.updateTaskStatus(taskId, request).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to update task status. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}
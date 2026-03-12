package com.example.tasktrack.repository;

import com.example.tasktrack.model.Project;
import com.example.tasktrack.model.ProjectCreateRequest;
import com.example.tasktrack.network.ApiClient;
import com.example.tasktrack.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteProjectRepository implements ProjectRepository {

    private final ApiService apiService = ApiClient.getApiService();

    @Override
    public void getProjects(DataCallback<List<Project>> callback) {
        apiService.getProjects().enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to load projects. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void createProject(String name, String description, DataCallback<Project> callback) {
        ProjectCreateRequest request = new ProjectCreateRequest(name, description);

        apiService.createProject(request).enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to create project. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}
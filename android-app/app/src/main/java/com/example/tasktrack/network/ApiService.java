package com.example.tasktrack.network;

import com.example.tasktrack.model.Project;
import com.example.tasktrack.model.ProjectCreateRequest;
import com.example.tasktrack.model.Task;
import com.example.tasktrack.model.TaskCreateRequest;
import com.example.tasktrack.model.TaskStatusUpdateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("projects")
    Call<List<Project>> getProjects();

    @POST("projects")
    Call<Project> createProject(@Body ProjectCreateRequest projectRequest);

    @GET("projects/{projectId}/tasks")
    Call<List<Task>> getTasksForProject(@Path("projectId") int projectId);

    @POST("projects/{projectId}/tasks")
    Call<Task> createTask(
            @Path("projectId") int projectId,
            @Body TaskCreateRequest taskRequest
    );

    @PATCH("tasks/{taskId}/status")
    Call<Task> updateTaskStatus(
            @Path("taskId") int taskId,
            @Body TaskStatusUpdateRequest statusRequest
    );
}
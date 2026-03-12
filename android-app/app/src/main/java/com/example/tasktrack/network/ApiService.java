package com.example.tasktrack.network;

import com.example.tasktrack.model.Project;
import com.example.tasktrack.model.Task;
import com.example.tasktrack.model.TaskCreateRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("projects")
    Call<List<Project>> getProjects();

    @GET("projects/{projectId}/tasks")
    Call<List<Task>> getTasksForProject(@Path("projectId") int projectId);

    @POST("projects/{projectId}/tasks")
    Call<Task> createTask(
            @Path("projectId") int projectId,
            @Body TaskCreateRequest taskRequest
    );
}
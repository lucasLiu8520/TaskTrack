package com.example.tasktrack.network;

import com.example.tasktrack.model.Project;
import com.example.tasktrack.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("projects")
    Call<List<Project>> getProjects();

    @GET("projects/{projectId}/tasks")
    // this tells Retrofit to replace projects/{projectId}/tasks with the actual selected project ID
    Call<List<Task>> getTasksForProject(@Path("projectId") int projectId);
    // for example, if project ID is 1, the request becomes:
    // GET /projects/1/tasks
    // That matches the FastAPI backend
}
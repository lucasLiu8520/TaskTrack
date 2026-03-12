package com.example.tasktrack.network;

import com.example.tasktrack.model.Project;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("projects")
    Call<List<Project>> getProjects();
}
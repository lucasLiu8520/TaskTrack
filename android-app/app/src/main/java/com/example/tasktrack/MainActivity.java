package com.example.tasktrack;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktrack.model.Project;
import com.example.tasktrack.network.ApiClient;
import com.example.tasktrack.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ListView projectsListView;
    private final List<Project> projectObjects = new ArrayList<>();
    private final List<String> projectDisplayNames = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        projectsListView = findViewById(R.id.projectsListView);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                projectDisplayNames
        );

        projectsListView.setAdapter(adapter);

        projectsListView.setOnItemClickListener((parent, view, position, id) -> {
            Project selectedProject = projectObjects.get(position);

            Toast.makeText(
                    MainActivity.this,
                    "Selected project: " + selectedProject.getName(),
                    Toast.LENGTH_SHORT
            ).show();
        });

        loadProjects();
    }

    private void loadProjects() {
        ApiService apiService = ApiClient.getApiService();

        apiService.getProjects().enqueue(new Callback<List<Project>>() {
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    projectObjects.clear();
                    projectDisplayNames.clear();

                    List<Project> projects = response.body();
                    projectObjects.addAll(projects);

                    for (Project project : projects) {
                        projectDisplayNames.add(project.getName());
                    }

                    if (projectDisplayNames.isEmpty()) {
                        projectDisplayNames.add("No projects found.");
                    }

                    adapter.notifyDataSetChanged();
                } else {
                    projectDisplayNames.clear();
                    projectDisplayNames.add("Failed to load projects. Code: " + response.code());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                projectDisplayNames.clear();
                projectDisplayNames.add("Network error: " + t.getMessage());
                adapter.notifyDataSetChanged();
            }
        });
    }
}
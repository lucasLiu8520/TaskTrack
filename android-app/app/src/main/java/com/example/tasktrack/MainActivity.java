package com.example.tasktrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktrack.model.Project;
import com.example.tasktrack.model.ProjectCreateRequest;
import com.example.tasktrack.network.ApiClient;
import com.example.tasktrack.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ListView projectsListView;
    private EditText projectNameEditText;
    private EditText projectDescriptionEditText;
    private Button addProjectButton;

    private final List<Project> projectObjects = new ArrayList<>();
    private final List<String> projectDisplayNames = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        projectsListView = findViewById(R.id.projectsListView);
        projectNameEditText = findViewById(R.id.projectNameEditText);
        projectDescriptionEditText = findViewById(R.id.projectDescriptionEditText);
        addProjectButton = findViewById(R.id.addProjectButton);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                projectDisplayNames
        );

        projectsListView.setAdapter(adapter);

        projectsListView.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= projectObjects.size()) {
                return;
            }

            Project selectedProject = projectObjects.get(position);

            Intent intent = new Intent(MainActivity.this, TaskActivity.class);
            intent.putExtra("project_id", selectedProject.getId());
            intent.putExtra("project_name", selectedProject.getName());
            startActivity(intent);
        });

        addProjectButton.setOnClickListener(v -> createProject());

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
                    projectObjects.clear();
                    projectDisplayNames.clear();
                    projectDisplayNames.add("Failed to load projects. Code: " + response.code());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                projectObjects.clear();
                projectDisplayNames.clear();
                projectDisplayNames.add("Network error: " + t.getMessage());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void createProject() {
        String name = projectNameEditText.getText().toString().trim();
        String description = projectDescriptionEditText.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please enter both name and description.", Toast.LENGTH_SHORT).show();
            return;
        }

        ProjectCreateRequest request = new ProjectCreateRequest(name, description);
        ApiService apiService = ApiClient.getApiService();

        apiService.createProject(request).enqueue(new Callback<Project>() {
            @Override
            public void onResponse(Call<Project> call, Response<Project> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(MainActivity.this, "Project created successfully.", Toast.LENGTH_SHORT).show();

                    projectNameEditText.setText("");
                    projectDescriptionEditText.setText("");

                    loadProjects();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Failed to create project. Code: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Project> call, Throwable t) {
                Toast.makeText(MainActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
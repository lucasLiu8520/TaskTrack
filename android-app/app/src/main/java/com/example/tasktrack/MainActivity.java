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

import com.example.tasktrack.repository.DataCallback;
import com.example.tasktrack.repository.ProjectRepository;
import com.example.tasktrack.repository.RepositoryProvider;

public class MainActivity extends AppCompatActivity {

    private ListView projectsListView;
    private EditText projectNameEditText;
    private EditText projectDescriptionEditText;
    private Button addProjectButton;

    private ProjectRepository projectRepository;

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

        projectRepository = RepositoryProvider.getProjectRepository(this);

        addProjectButton.setOnClickListener(v -> createProject());

        loadProjects();
    }

    private void loadProjects() {
        projectRepository.getProjects(new DataCallback<List<Project>>() {
            @Override
            public void onSuccess(List<Project> projects) {
                runOnUiThread(() -> {
                    projectObjects.clear();
                    projectDisplayNames.clear();

                    projectObjects.addAll(projects);

                    for (Project project : projects) {
                        projectDisplayNames.add(project.getName());
                    }

                    if (projectDisplayNames.isEmpty()) {
                        projectDisplayNames.add("No projects found.");
                    }

                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    projectObjects.clear();
                    projectDisplayNames.clear();
                    projectDisplayNames.add(errorMessage);
                    adapter.notifyDataSetChanged();
                });
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

        projectRepository.createProject(name, description, new DataCallback<Project>() {
            @Override
            public void onSuccess(Project result) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Project created successfully.", Toast.LENGTH_SHORT).show();
                    projectNameEditText.setText("");
                    projectDescriptionEditText.setText("");
                    loadProjects();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}

package com.example.tasktrack;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktrack.model.Project;
import com.example.tasktrack.network.ApiClient;
import com.example.tasktrack.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView projectsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        projectsTextView = findViewById(R.id.projectsTextView);

        loadProjects();
    }

    private void loadProjects() {
        ApiService apiService = ApiClient.getApiService();

        apiService.getProjects().enqueue(new Callback<List<Project>>() {
            // Makes the network request asynchronoulsy
            // Android does not allow network requests on the main UI thread.
            // If not, the app would crash or freeze.
            @Override
            public void onResponse(Call<List<Project>> call, Response<List<Project>> response) {
                // Runs when the server returns a response
                if (response.isSuccessful() && response.body() != null) {
                    List<Project> projects = response.body();

                    if (projects.isEmpty()) {
                        projectsTextView.setText("No projects found.");
                        return;
                    }

                    StringBuilder builder = new StringBuilder();
                    for (Project project : projects) {
                        builder.append("ID: ")
                                .append(project.getId())
                                .append("\nName: ")
                                .append(project.getName())
                                .append("\nDescription: ")
                                .append(project.getDescription())
                                .append("\n\n");
                    }

                    projectsTextView.setText(builder.toString());
                } else {
                    projectsTextView.setText("Failed to load projects. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Project>> call, Throwable t) {
                // Runs when the request fails completely, such as:
                // backend not running
                // wrong URL
                // connection blocked
                projectsTextView.setText("Network error: " + t.getMessage());
            }
        });
    }
}
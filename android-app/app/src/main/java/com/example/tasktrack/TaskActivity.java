package com.example.tasktrack;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktrack.model.Task;
import com.example.tasktrack.network.ApiClient;
import com.example.tasktrack.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActivity extends AppCompatActivity {

    private TextView projectTitleTextView;
    private TextView tasksTextView;
    private int projectId;
    private String projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        projectTitleTextView = findViewById(R.id.projectTitleTextView);
        tasksTextView = findViewById(R.id.tasksTextView);

        projectId = getIntent().getIntExtra("project_id", -1);
        projectName = getIntent().getStringExtra("project_name");

        projectTitleTextView.setText(projectName != null ? projectName : "Unknown Project");

        if (projectId == -1) {
            // This acts as a safety check.
            // If the project ID was not passed correctly from MainActivity, the task screen stops
            // early and shows a clear mesage
            tasksTextView.setText("Invalid project ID.");
            return;
        }

        loadTasks();
    }

    private void loadTasks() {
        // This function keeps network logic separate from onCreate()
        ApiService apiService = ApiClient.getApiService();

        apiService.getTasksForProject(projectId).enqueue(new Callback<List<Task>>() {
            // Uses the selected project ID to fetch only tasks for that project
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Task> tasks = response.body();

                    if (tasks.isEmpty()) {
                        tasksTextView.setText("No tasks found for this project.");
                        return;
                    }

                    StringBuilder builder = new StringBuilder();
                    // Quickest way to display multiple tasks without building a full list UI yet.

                    for (Task task : tasks) {
                        builder.append("ID: ")
                                .append(task.getId())
                                .append("\nTitle: ")
                                .append(task.getTitle())
                                .append("\nDescription: ")
                                .append(task.getDescription())
                                .append("\nStatus: ")
                                .append(task.getStatus())
                                .append("\n\n");
                    }

                    tasksTextView.setText(builder.toString());
                } else {
                    tasksTextView.setText("Failed to load tasks. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                tasksTextView.setText("Network error: " + t.getMessage());
            }
        });
    }
}
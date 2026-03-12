package com.example.tasktrack;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktrack.model.Task;
import com.example.tasktrack.model.TaskCreateRequest;
import com.example.tasktrack.network.ApiClient;
import com.example.tasktrack.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActivity extends AppCompatActivity {

    private TextView projectTitleTextView;
    private TextView tasksTextView;
    private EditText taskTitleEditText;
    private EditText taskDescriptionEditText;
    private Button addTaskButton;

    private int projectId;
    private String projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        projectTitleTextView = findViewById(R.id.projectTitleTextView);
        tasksTextView = findViewById(R.id.tasksTextView);
        taskTitleEditText = findViewById(R.id.taskTitleEditText);
        taskDescriptionEditText = findViewById(R.id.taskDescriptionEditText);
        addTaskButton = findViewById(R.id.addTaskButton);

        projectId = getIntent().getIntExtra("project_id", -1);
        projectName = getIntent().getStringExtra("project_name");

        projectTitleTextView.setText(projectName != null ? projectName : "Unknown Project");

        if (projectId == -1) {
            tasksTextView.setText("Invalid project ID.");
            addTaskButton.setEnabled(false);
            return;
        }

        addTaskButton.setOnClickListener(v -> createTask());

        loadTasks();
    }

    private void loadTasks() {
        ApiService apiService = ApiClient.getApiService();

        apiService.getTasksForProject(projectId).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Task> tasks = response.body();

                    if (tasks.isEmpty()) {
                        tasksTextView.setText("No tasks found for this project.");
                        return;
                    }

                    StringBuilder builder = new StringBuilder();

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

    private void createTask() {
        String title = taskTitleEditText.getText().toString().trim();
        String description = taskDescriptionEditText.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please enter both title and description.", Toast.LENGTH_SHORT).show();
            return;
        }

        TaskCreateRequest request = new TaskCreateRequest(title, description);
        ApiService apiService = ApiClient.getApiService();

        apiService.createTask(projectId, request).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(TaskActivity.this, "Task created successfully.", Toast.LENGTH_SHORT).show();

                    taskTitleEditText.setText("");
                    taskDescriptionEditText.setText("");

                    loadTasks();
                } else {
                    Toast.makeText(TaskActivity.this,
                            "Failed to create task. Code: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                Toast.makeText(TaskActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
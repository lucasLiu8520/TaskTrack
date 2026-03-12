package com.example.tasktrack;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktrack.model.Task;
import com.example.tasktrack.model.TaskCreateRequest;
import com.example.tasktrack.model.TaskStatusUpdateRequest;
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
    private EditText taskIdEditText;
    private Button addTaskButton;
    private Button updateStatusButton;
    private Spinner statusSpinner;

    private int projectId;
    private String projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        projectTitleTextView = findViewById(R.id.projectTitleTextView);
        tasksTextView = findViewById(R.id.tasksTextView);
        taskTitleEditText = findViewById(R.id.taskTitleEditText);
        taskDescriptionEditText = findViewById(R.id.taskDescriptionEditText);
        taskIdEditText = findViewById(R.id.taskIdEditText);
        addTaskButton = findViewById(R.id.addTaskButton);
        updateStatusButton = findViewById(R.id.updateStatusButton);
        statusSpinner = findViewById(R.id.statusSpinner);

        String[] statuses = {"TODO", "IN_PROGRESS", "DONE"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                statuses
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(spinnerAdapter);

        projectId = getIntent().getIntExtra("project_id", -1);
        projectName = getIntent().getStringExtra("project_name");

        projectTitleTextView.setText(projectName != null ? projectName : "Unknown Project");

        if (projectId == -1) {
            tasksTextView.setText("Invalid project ID.");
            addTaskButton.setEnabled(false);
            updateStatusButton.setEnabled(false);
            return;
        }

        addTaskButton.setOnClickListener(v -> createTask());
        updateStatusButton.setOnClickListener(v -> updateTaskStatus());

        loadTasks();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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

    private void updateTaskStatus() {
        String taskIdText = taskIdEditText.getText().toString().trim();

        if (taskIdText.isEmpty()) {
            Toast.makeText(this, "Please enter a task ID.", Toast.LENGTH_SHORT).show();
            return;
        }

        int taskId = Integer.parseInt(taskIdText);
        String selectedStatus = statusSpinner.getSelectedItem().toString();
        // Ensures the user can only choose valid statuses:
        // TODO
        // IN_PROGRESS
        // DONE
        // To make sure matches the backend enum and prevents invalid input

        TaskStatusUpdateRequest request = new TaskStatusUpdateRequest(selectedStatus);
        ApiService apiService = ApiClient.getApiService();

        apiService.updateTaskStatus(taskId, request).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(TaskActivity.this, "Task status updated.", Toast.LENGTH_SHORT).show();
                    taskIdEditText.setText("");
                    loadTasks();
                    // refreshes the screen immediately so the changed status becomes visible.
                } else {
                    Toast.makeText(TaskActivity.this,
                            "Failed to update status. Code: " + response.code(),
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
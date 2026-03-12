package com.example.tasktrack;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tasktrack.model.Task;
import com.example.tasktrack.model.TaskCreateRequest;
import com.example.tasktrack.model.TaskStatusUpdateRequest;
import com.example.tasktrack.network.ApiClient;
import com.example.tasktrack.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskActivity extends AppCompatActivity {

    private TextView projectTitleTextView;
    private EditText taskTitleEditText;
    private EditText taskDescriptionEditText;
    private EditText taskIdEditText;
    private Button addTaskButton;
    private Button updateStatusButton;
    private Spinner statusSpinner;
    private ListView tasksListView;

    private int projectId;
    private String projectName;

    private final List<Task> taskObjects = new ArrayList<>();
    // stores the real Task objects from the backend
    private final List<String> taskDisplayTexts = new ArrayList<>();
    // Stores the strings shown in the list
    // This mirrors the pattern already used in the project
    private ArrayAdapter<String> taskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        projectTitleTextView = findViewById(R.id.projectTitleTextView);
        taskTitleEditText = findViewById(R.id.taskTitleEditText);
        taskDescriptionEditText = findViewById(R.id.taskDescriptionEditText);
        taskIdEditText = findViewById(R.id.taskIdEditText);
        addTaskButton = findViewById(R.id.addTaskButton);
        updateStatusButton = findViewById(R.id.updateStatusButton);
        statusSpinner = findViewById(R.id.statusSpinner);
        tasksListView = findViewById(R.id.tasksListView);

        String[] statuses = {"TODO", "IN_PROGRESS", "DONE"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                statuses
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(spinnerAdapter);

        taskAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                taskDisplayTexts
        );
        tasksListView.setAdapter(taskAdapter);

        tasksListView.setOnItemClickListener((parent, view, position, id) -> {
            // This is the key UX improvement
            // 1: user taps a task
            // 2: selected task ID gets filled automatically
            // 3: user no longer needs to type IDs manually
            if (position >= taskObjects.size()) {
                return;
            }

            Task selectedTask = taskObjects.get(position);
            taskIdEditText.setText(String.valueOf(selectedTask.getId()));

            Toast.makeText(
                    TaskActivity.this,
                    "Selected task: " + selectedTask.getTitle(),
                    Toast.LENGTH_SHORT
            ).show();
        });

        projectId = getIntent().getIntExtra("project_id", -1);
        projectName = getIntent().getStringExtra("project_name");

        projectTitleTextView.setText(projectName != null ? projectName : "Unknown Project");

        if (projectId == -1) {
            Toast.makeText(this, "Invalid project ID.", Toast.LENGTH_SHORT).show();
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
        // ensures that after create/update, the visible list stays accurate
        ApiService apiService = ApiClient.getApiService();

        apiService.getTasksForProject(projectId).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    taskObjects.clear();
                    taskDisplayTexts.clear();

                    List<Task> tasks = response.body();
                    taskObjects.addAll(tasks);

                    for (Task task : tasks) {
                        String displayText =
                                "ID: " + task.getId() +
                                " | " + task.getTitle() +
                                "\nStatus: " + task.getStatus() +
                                "\nDescription: " + task.getDescription();
                        taskDisplayTexts.add(displayText);
                    }

                    if (taskDisplayTexts.isEmpty()) {
                        taskDisplayTexts.add("No tasks found for this project.");
                    }

                    taskAdapter.notifyDataSetChanged();
                } else {
                    taskObjects.clear();
                    taskDisplayTexts.clear();
                    taskDisplayTexts.add("Failed to load tasks. Response code: " + response.code());
                    taskAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                taskObjects.clear();
                taskDisplayTexts.clear();
                taskDisplayTexts.add("Network error: " + t.getMessage());
                taskAdapter.notifyDataSetChanged();
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
            Toast.makeText(this, "Please tap a task first.", Toast.LENGTH_SHORT).show();
            return;
        }

        int taskId = Integer.parseInt(taskIdText);
        String selectedStatus = statusSpinner.getSelectedItem().toString();

        TaskStatusUpdateRequest request = new TaskStatusUpdateRequest(selectedStatus);
        ApiService apiService = ApiClient.getApiService();

        apiService.updateTaskStatus(taskId, request).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(TaskActivity.this, "Task status updated.", Toast.LENGTH_SHORT).show();
                    taskIdEditText.setText("");
                    loadTasks();
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
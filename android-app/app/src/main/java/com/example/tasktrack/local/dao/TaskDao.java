package com.example.tasktrack.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tasktrack.local.entity.TaskEntity;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM tasks WHERE projectId = :projectId ORDER BY id ASC")
    List<TaskEntity> getTasksForProject(int projectId);

    @Insert
    long insertTask(TaskEntity task);

    @Query("UPDATE tasks SET status = :status WHERE id = :taskId")
    void updateTaskStatus(int taskId, String status);
}
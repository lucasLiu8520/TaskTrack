package com.example.tasktrack.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tasktrack.local.entity.ProjectEntity;

import java.util.List;

@Dao
public interface ProjectDao {

    @Query("SELECT * FROM projects ORDER BY id ASC")
    List<ProjectEntity> getAllProjects();

    @Insert
    long insertProject(ProjectEntity project);
}
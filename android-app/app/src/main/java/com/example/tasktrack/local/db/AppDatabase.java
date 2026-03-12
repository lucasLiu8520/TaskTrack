package com.example.tasktrack.local.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.tasktrack.local.dao.ProjectDao;
import com.example.tasktrack.local.dao.TaskDao;
import com.example.tasktrack.local.entity.ProjectEntity;
import com.example.tasktrack.local.entity.TaskEntity;

@Database(entities = {ProjectEntity.class, TaskEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProjectDao projectDao();
    public abstract TaskDao taskDao();
}
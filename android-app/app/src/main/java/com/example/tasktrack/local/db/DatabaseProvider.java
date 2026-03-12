package com.example.tasktrack.local.db;

import android.content.Context;

import androidx.room.Room;

public class DatabaseProvider {

    private static AppDatabase databaseInstance;

    public static AppDatabase getDatabase(Context context) {
        if (databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "tasktrack_database"
            ).build();
        }
        return databaseInstance;
    }
}
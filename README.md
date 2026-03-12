# COSC310 Task Management App

A task management application built as a software engineering project.  
The project includes:

- a **FastAPI backend**
- an **Android client built in Android Studio**
- a **dual-mode Android architecture**
  - **REMOTE mode**: Android app connects to the FastAPI backend
  - **LOCAL mode**: Android app runs independently using Room local storage

This project was designed to demonstrate software engineering practices including requirements analysis, architecture design, modular implementation, API integration, Android development, and maintainability.

---

## Features

### Project Features
- Create projects
- View project list
- Open a selected project

### Task Features
- View tasks for a selected project
- Create tasks
- Update task status (`TODO`, `IN_PROGRESS`, `DONE`)

### Navigation
- Navigate from project list to task screen
- Return to the previous screen using Android back navigation

### Data Modes
- **REMOTE mode** uses FastAPI + Retrofit
- **LOCAL mode** uses Room for standalone Android execution

---

## Project Modes

### 1. REMOTE Mode
In REMOTE mode, the Android app connects to the FastAPI backend.

Use this mode to demonstrate:
- client-server architecture
- REST API integration
- backend-driven workflows

In this mode, the backend must be started manually.

### 2. LOCAL Mode
In LOCAL mode, the Android app stores all data locally using Room.

Use this mode to demonstrate:
- standalone Android execution
- persistent local storage
- no external backend dependency

This mode is useful for testing on another computer where running the Python backend separately is inconvenient.

---

## Current Mode Configuration

The Android app mode is controlled in:

```java
android-app/app/src/main/java/com/example/tasktrack/config/AppConfig.java

Set
public static final AppMode CURRENT_MODE = AppMode.REMOTE;
for backend-connected mode, 

or:
public static final AppMode CURRENT_MODE = AppMode.LOCAL;
for standalone local mode.

Technologies Used
Backend
Python
FastAPI
Uvicorn
Android
Java
Android Studio
Retrofit

Room
SQLite (through Room)

Development Tools
Git
GitHub
VS Code
Android Studio

cosc310-task-app/
├── backend/                  # FastAPI backend
├── android-app/              # Android Studio project
├── docs/                     # Requirements, architecture, UML
│   ├── requirements.md
│   ├── architecture.md
│   └── uml/
├── README.md
└── .gitignore

Running the Project
Option A — Run in LOCAL mode (Standalone Android)
This is the easiest way to run the app independently in Android Studio.

Steps
Open the Android project in Android Studio
Set AppConfig.CURRENT_MODE to AppMode.LOCAL
Run the app on an emulator or Android device
Create projects and tasks directly in the app

Notes
No Python backend is required
Data is stored locally in the app database
Data persists across normal app restarts

Option B — Run in REMOTE mode (Backend + Android)
Use this mode to demonstrate backend integration.

Backend steps
Open the project root in VS Code
Activate the Python virtual environment
Start the backend:
uvicorn backend.main:app --reload
Optional: open FastAPI docs at:
http://127.0.0.1:8000/docs

Android steps
Open the Android project in Android Studio
Set AppConfig.CURRENT_MODE to AppMode.REMOTE
Run the app on an emulator
Make sure the emulator can access:
http://10.0.2.2:8000/

Notes
REMOTE mode depends on the backend being started manually
Current backend storage is in-memory
In REMOTE mode, backend data resets when the FastAPI server restarts

Backend Development Helpers

The FastAPI backend includes development helper endpoints:
POST /dev/reset — clear all in-memory backend data
POST /dev/seed — load sample demo data
These are helpful when testing REMOTE mode.

Example User Flow

LOCAL mode
Launch app
Create a project
Open the project
Create tasks
Update task status
Restart app and confirm data persists

REMOTE mode
Start FastAPI backend
Launch app
Load projects from backend
Open a project
Create tasks
Update task status

Future Improvements

Better task row UI with per-task status controls
Delete project/task functionality
More polished task editing
Optional startup screen for choosing LOCAL vs REMOTE mode
Persistent backend database instead of in-memory FastAPI storage
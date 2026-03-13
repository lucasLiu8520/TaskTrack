# COSC310 Task Management App

A task management application developed as part of a **Software Engineering project**.

The system consists of:

- **FastAPI backend**
- **Android client built in Android Studio**
- **Dual-mode architecture**

The Android application can run in two modes:

- **REMOTE mode** — Android connects to the FastAPI backend
- **LOCAL mode** — Android runs independently using Room local storage

The project demonstrates software engineering concepts including:

- requirements specification
- layered architecture
- Android development
- REST API integration
- repository pattern
- local persistence
- modular design

---

# System Architecture

The application supports two execution modes.

### REMOTE Mode
Android UI
↓
Repository Layer
↓
Retrofit API Client
↓
FastAPI Backend
↓
Backend Storage

### LOCAL Mode
Android UI
↓
Repository Layer
↓
Room Database
↓
SQLite Local Storage


This design allows the **same Android UI** to operate with either a **remote backend** or a **local database**.

---

# Features

## Project Management

- Create projects
- View project list
- Open a selected project

## Task Management

- View tasks for a selected project
- Create tasks
- Update task status

Task statuses:
TODO
IN_PROGRESS
DONE

## Navigation

- Navigate from project list to task screen
- Return to the previous screen using Android back navigation

## Data Modes

- **REMOTE mode** uses FastAPI + Retrofit
- **LOCAL mode** uses Room database for standalone Android execution

---

# Project Modes

## 1. REMOTE Mode

In REMOTE mode, the Android app connects to the FastAPI backend.

This demonstrates:

- client-server architecture
- REST API communication
- backend-driven data management

In this mode the backend must be started manually.

---

## 2. LOCAL Mode

In LOCAL mode, the Android app runs independently.

All data is stored locally using **Room**.

This demonstrates:

- standalone Android execution
- local persistence
- offline functionality

LOCAL mode is useful when running the project on another machine where starting the backend is inconvenient.

---

# Current Mode Configuration

The application mode is configured in:
android-app/app/src/main/java/com/example/tasktrack/config/AppConfig.java

Set the current mode:
public static final AppMode CURRENT_MODE = AppMode.REMOTE;
or
public static final AppMode CURRENT_MODE = AppMode.LOCAL;

# Technologies Used

## Backend
Python
FastAPI
Uvicorn

## Android
Java
Android Studio
Retrofit
Room Database
RecyclerView

## Database
SQLite (via Room)

## Development Tools
Git
GitHub
VS Code
Android Studio

---

# Project Structure
cosc310-task-app/
│
├── backend/                  # FastAPI backend
│   ├── main.py
│   ├── schemas.py
│   ├── storage.py
│   └── requirements.txt
│
├── android-app/              # Android Studio project
│
├── docs/                     # Documentation
│   ├── requirements.md
│   ├── architecture.md
│   └── uml/
│
├── README.md
└── .gitignore

# Running the Project
## Option A — Run in LOCAL Mode (Recommended)

This allows the Android app to run independently.

Steps

1. Open the Android project in Android Studio

2. Set: 
AppConfig.CURRENT_MODE = AppMode.LOCAL

3. Run the app on an emulator or Android device

4. Create projects and tasks directly in the app

Notes

1. No backend is required

2. Data is stored locally

3. Data persists across normal app restarts

---

## Option B — Run in REMOTE Mode

Use this mode to demonstrate backend integration.
Backend Setup

Open the project root in VS Code.

Activate the Python virtual environment and run:
uvicorn backend.main:app --reload

Optional: open FastAPI API docs:
http://127.0.0.1:8000/docs

Android Setup

1. Open the Android project in Android Studio

2. Set:
AppConfig.CURRENT_MODE = AppMode.REMOTE

3. Run the app on the Android emulator

The emulator connects to:
http://10.0.2.2:8000

Notes

1. The backend must be started manually

2. Backend storage is currently in-memory

3. Data resets when the FastAPI server restarts

---

# Backend Development Helpers

The backend includes helper endpoints for testing.
POST /dev/reset
Clears all in-memory backend data.

POST /dev/seed
Loads sample demo data.

These endpoints are useful when demonstrating REMOTE mode.

# Example User Flow
## LOCAL Mode

Launch the app

Create a project

Open the project

Create tasks

Update task status

Restart the app and confirm data persists

---

## REMOTE Mode

Start the FastAPI backend

Launch the Android app

Load projects from the backend

Open a project

Create tasks

Update task status

---

# Future Improvements

Possible enhancements include:

improved task row UI

delete project and task functionality

task editing

startup screen for choosing LOCAL vs REMOTE mode

persistent backend database

user authentication

multi-user support

cloud deployment

---

# Documentation
Detailed documentation is available in the docs folder

# Summary

The Task Management App demonstrates a clean layered architecture with a flexible data layer that supports both client-server and standalone mobile execution.

The repository pattern allows the Android UI to operate with either a FastAPI backend or a local Room database, making the system modular, testable, and extensible.
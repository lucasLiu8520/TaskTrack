# System Architecture
Task Management Application

---

## 1. Architecture Overview

The system supports two operating configurations:

1. **REMOTE mode**  
    The Android client communicates with a FastAPI backend server through REST APIs.
    Data flow:

    Android Client → HTTP API → FastAPI Backend → Database

    This configuration simulates a typical client-server mobile application.

2. **LOCAL mode**  
    The Android client runs fully standalone using a local database.

    Data flow:

    Android Client → Room Database → SQLite

    This mode allows the application to:

        run without starting the backend server

        persist data locally

        simplify testing and demonstrations 
---

## Architectural Style

The system follows a layered architecture combined with a repository pattern.

Key architectural characteristics:

    Separation of concerns

    pluggable data sources

    client-server compatibility

    extensible backend services

The repository layer allows the Android UI to work with either:

    a remote backend

    a local Room database

without changing the UI logic.

## 2. High-Level System Architecture

### REMOTE Mode

Android UI
    ↓
Repository Layer
    ↓
Local Repository
    ↓
Room Database
    ↓
SQLite Local Storage

### LOCAL Mode

Android UI
    ↓
Repository Layer
    ↓
Local Repository
    ↓
Room Database
    ↓
SQLite Local Storage


## UML Class Diagram

The class diagram below illustrates the structure of the Android client, repository layer, remote API integration, and local Room database components.

(uml/class-diagram.png)
---

## 3. Backend Architecture

The backend server is implemented using FastAPI and provides a RESTful API for managing projects and tasks.

Responsibilities:

    manage projects and tasks

    enforce business logic

    validate API input

    persist application data

The backend follows a four-layer structure.


---

### 3.1 API Routes Layer

The Routes Layer defines the public REST API.

Responsibilities:

    receive HTTP requests

    validate request payloads using schemas

    return HTTP responses

    delegate business logic to the service layer

Example endpoints:
GET    /projects
POST   /projects
GET    /projects/{project_id}

GET    /projects/{project_id}/tasks
POST   /projects/{project_id}/tasks

GET    /tasks/{task_id}
PATCH  /tasks/{task_id}/status

---

### 3.2 Service Layer

The Service Layer contains the core application logic.

Responsibilities:

    enforce business rules

    validate operations

    coordinate interactions between routes and the database

Example operations:

    ensure a project exists before creating a task

    verify task status values

    update task status safely

    enforce consistent task relationships

Separating this logic prevents business rules from being embedded inside API routes.
---

### 3.3 Data Access Layer

The Data Access Layer (DAL) is responsible for interacting with the database.

Responsibilities:

    perform database queries

    create new records

    update existing records

    retrieve entities

Entities managed by the DAL:

    Project

    Task
---

### 3.4 Database Layer

The backend uses SQLite for persistent storage.

Reasons for selecting SQLite:

    lightweight

    minimal configuration

    suitable for development and prototype systems

    simple integration with Python

Database tables:
Projects
| Field      | Type     |
| ---------- | -------- |
| id         | integer  |
| name       | string   |
| created_at | datetime |

Tasks
| Field       | Type    |
| ----------- | ------- |
| id          | integer |
| project_id  | integer |
| title       | string  |
| description | string  |
| status      | string  |

Relationship:
Project 1 --- * Task

A project can contain multiple tasks.

---

## 4. Android Data Architecture

The Android client uses a repository-based architecture to separate the UI from data sources.

Responsibilities of the repository layer:

    isolate Activities from direct data access

    provide a unified API for UI components

    allow switching between remote and local data sources

Main repository abstractions:
- ProjectRepository
- TaskRepository

These define the operations used by the UI.

Example operations:

    getProjects()

    getTasks(projectId)

    createTask()

    updateTaskStatus()

Implementations:
- RemoteProjectRepository
- RemoteTaskRepository
- LocalProjectRepository
- LocalTaskRepository
---

## 5. Android Application Components

The Android client contains the following main components:
    UI Activities
      
    Repository Layer
      
    Data Source
           
    Remote API   
    
    Local Database
---

## 6. UI Screens

The application contains three main screens.

## 6.1 Project List Screen

Displays all projects available in the system.

Functions:

    retrieve project list

    display projects using RecyclerView

    allow users to select a project
---

## 6.2 Task List Screen

Displays tasks belonging to the selected project.

Functions:

    view tasks

    open task details

    update task status

---

## 6.3 Create Task Screen

Allows users to create a new task.

    Fields:

    task title

    task description
---

## 7. API Communication

User Action
   ↓
Android Activity
   ↓
Repository
   ↓
Retrofit HTTP Client
   ↓
FastAPI API Route
   ↓
Service Layer
   ↓
Database Query
   ↓
Response Returned
   ↓
Displayed in UI

---

## 8. Local Persistence Architecture

In LOCAL mode, the Android app stores data using Room.

Main local database components:
- ProjectEntity
- TaskEntity
- ProjectDao
- TaskDao
- AppDatabase
- DatabaseProvider

This allows the app to:
- run independently of the backend
- persist data across normal app restarts
- support standalone testing in Android Studio

---

## 9. Error Handling

The system returns clear error responses for invalid requests.

Examples:

- project not found
- task not found
- invalid task status

Error responses include HTTP status codes and descriptive messages.

---

## 10. Extensibility

The architecture is designed to support future expansion.

Possible future improvements include:

- user authentication
- multi-user support
- task assignment
- comments on tasks
- web client
- iOS client
- cloud deployment

The modular separation between backend and client allows these features to be added without major architectural changes.

---

## 11. Technology Stack

Backend:
Python
FastAPI
SQLite


Android Client:
Android Studio
Java or Kotlin
Retrofit
RecyclerView
Room Database

Development Tools:
Git
GitHub
VS Code
Android Studio

---

## 12. Summary

The Task Management Application uses a layered client-server architecture with a repository-based Android data layer.

The design supports both:

    REMOTE mode using a FastAPI backend

    LOCAL mode using a Room database

This approach enables:

    flexible deployment

    simplified testing

    future extensibility

while maintaining a clean separation between the user interface, application logic, and data storage layers.
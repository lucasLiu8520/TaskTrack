from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from backend.schemas import (
    ProjectCreate,
    Project,
    TaskCreate,
    Task,
    TaskStatusUpdate,
    TaskStatus,
)
from backend import storage

app = FastAPI(title="Task Management API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.get("/")
def read_root():
    return {
        "message": "Task Management API is running",
        "note": "Data is stored in memory and resets when the server restarts."
    }


@app.get("/health")
def health_check():
    return {"status": "ok"}


@app.post("/dev/reset")
## Quickly clear everything
def reset_data():
    storage.projects.clear()
    storage.tasks.clear()
    storage.project_id_counter = 1
    storage.task_id_counter = 1
    return {"message": "All in-memory data has been reset."}


@app.post("/dev/seed")
## Quickly repopulate the backend with sample content
def seed_data():
    storage.projects.clear()
    storage.tasks.clear()
    storage.project_id_counter = 1
    storage.task_id_counter = 1

    project1 = Project(
        id=storage.project_id_counter,
        name="Demo Project",
        description="Demo project for Android integration"
    )
    storage.projects.append(project1)
    storage.project_id_counter += 1

    project2 = Project(
        id=storage.project_id_counter,
        name="Personal Tasks",
        description="General personal task list"
    )
    storage.projects.append(project2)
    storage.project_id_counter += 1

    task1 = Task(
        id=storage.task_id_counter,
        title="Build Android UI",
        description="Create project and task screens",
        status=TaskStatus.IN_PROGRESS,
        project_id=1
    )
    storage.tasks.append(task1)
    storage.task_id_counter += 1

    task2 = Task(
        id=storage.task_id_counter,
        title="Test API integration",
        description="Verify Android can load projects and tasks",
        status=TaskStatus.TODO,
        project_id=1
    )
    storage.tasks.append(task2)
    storage.task_id_counter += 1

    task3 = Task(
        id=storage.task_id_counter,
        title="Buy groceries",
        description="Milk, eggs, fruit",
        status=TaskStatus.DONE,
        project_id=2
    )
    storage.tasks.append(task3)
    storage.task_id_counter += 1

    return {"message": "Sample in-memory data has been seeded."}


@app.get("/projects", response_model=list[Project])
def get_projects():
    return storage.projects


@app.post("/projects", response_model=Project)
def create_project(project_data: ProjectCreate):
    project = Project(
        id=storage.project_id_counter,
        name=project_data.name,
        description=project_data.description,
    )
    storage.projects.append(project)
    storage.project_id_counter += 1
    return project


@app.get("/projects/{project_id}", response_model=Project)
def get_project(project_id: int):
    for project in storage.projects:
        if project.id == project_id:
            return project
    raise HTTPException(status_code=404, detail="Project not found")


@app.get("/projects/{project_id}/tasks", response_model=list[Task])
def get_tasks_for_project(project_id: int):
    project_exists = any(project.id == project_id for project in storage.projects)
    if not project_exists:
        raise HTTPException(status_code=404, detail="Project not found")

    return [task for task in storage.tasks if task.project_id == project_id]


@app.post("/projects/{project_id}/tasks", response_model=Task)
def create_task(project_id: int, task_data: TaskCreate):
    project_exists = any(project.id == project_id for project in storage.projects)
    if not project_exists:
        raise HTTPException(status_code=404, detail="Project not found")

    task = Task(
        id=storage.task_id_counter,
        title=task_data.title,
        description=task_data.description,
        status=TaskStatus.TODO,
        project_id=project_id,
    )
    storage.tasks.append(task)
    storage.task_id_counter += 1
    return task


@app.get("/tasks/{task_id}", response_model=Task)
def get_task(task_id: int):
    for task in storage.tasks:
        if task.id == task_id:
            return task
    raise HTTPException(status_code=404, detail="Task not found")


@app.patch("/tasks/{task_id}/status", response_model=Task)
def update_task_status(task_id: int, status_update: TaskStatusUpdate):
    for index, task in enumerate(storage.tasks):
        if task.id == task_id:
            updated_task = Task(
                id=task.id,
                title=task.title,
                description=task.description,
                status=status_update.status,
                project_id=task.project_id,
            )
            storage.tasks[index] = updated_task
            return updated_task

    raise HTTPException(status_code=404, detail="Task not found")
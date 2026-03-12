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

app = FastAPI(title="COSC310 Task Management API")

# Allow frontend clients such as Android emulator / local tools to access the API
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.get("/")
def read_root():
    return {"message": "COSC310 Task Management API is running"}


@app.get("/health")
def health_check():
    return {"status": "ok"}


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
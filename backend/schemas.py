# Defines:

# Project

# Task

# TaskStatus

# input/output shapes

from enum import Enum
from pydantic import BaseModel


class TaskStatus(str, Enum):
    TODO = "TODO"
    IN_PROGRESS = "IN_PROGRESS"
    DONE = "DONE"


class ProjectCreate(BaseModel):
    name: str
    description: str


class Project(BaseModel):
    id: int
    name: str
    description: str


class TaskCreate(BaseModel):
    title: str
    description: str

# The client sends: title, description
# The backend will assign: task ID, default status, project ID

class Task(BaseModel):
    id: int
    title: str
    description: str
    status: TaskStatus
    project_id: int


class TaskStatusUpdate(BaseModel):
    status: TaskStatus
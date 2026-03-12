from fastapi import FastAPI

app = FastAPI(title="COSC310 Task Management API")
# This creates the FastAPI application object.
# It is the core backend app instance that Uvicorn will run.

@app.get("/")
# This defines a GET endpoint at the root URL.

def read_root():
    return {"message": "COSC310 Task Management API is running"}


@app.get("/health")
def health_check():
    return {"status": "ok"}
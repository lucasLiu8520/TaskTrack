## Development Note

The current backend stores data in memory for simplicity.  
This means projects and tasks reset whenever the FastAPI server restarts.

For easier testing, the backend provides local development helper endpoints:

- `POST /dev/reset` — clears all in-memory data
- `POST /dev/seed` — loads sample demo data
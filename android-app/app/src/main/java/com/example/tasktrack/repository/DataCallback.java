package com.example.tasktrack.repository;

public interface DataCallback<T> {
    void onSuccess(T result);
    void onError(String errorMessage);
}
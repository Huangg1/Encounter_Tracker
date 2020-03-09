package com.example.offlineencountertracker.Room;

public interface AsyncCb<T> {
    void handleResponse(T object);
    void handleFault(Exception e);
}

package com.example.offlineencountertracker.Room;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {EncItem.class, EncCreaItem.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract Dao getDao();
}

package com.example.offlineencountertracker.Room;

import android.content.Context;

import androidx.room.Room;

public class Connections {
    private static Connections instance;
    private Database database;

    private Connections(Context context){
        database = Room.databaseBuilder(context, Database.class, "db_enc").build();
    }

    public static Connections getInstance(Context context){
        synchronized(Connections.class){
            if(instance == null){
                instance = new Connections(context);
            }
            return instance;
        }
    }

    public Database getDatabase(){
        return database;
    }
}

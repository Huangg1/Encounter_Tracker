package com.example.offlineencountertracker.Room;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    @Insert
    void insert(EncItem encounter);

    @Insert
    void insertCrea(EncCreaItem encounter);

    @Delete
    void delete(EncItem encounter);

    @Delete
    void deleteCrea(EncCreaItem encounter);

    @Update
    void update(EncItem encounter);

    @Update
    void updateCrea(EncCreaItem encounter);

    @Query("SELECT * FROM EncItem ORDER BY name")
    List<EncItem> getAllEnc();

    @Query("SELECT * FROM EncCreaItem ORDER BY name")
    List<EncCreaItem> getAllEncCrea();

    @Query("SELECT * FROM EncItem WHERE name = :name")
    EncItem getEncById(String name);

    @Query("SELECT * FROM EncCreaItem WHERE id = :id")
    EncCreaItem getEncCreaById(String id);
}

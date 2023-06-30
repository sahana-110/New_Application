package com.example.new_application;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FacilityEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FacilityDao facilityDao();
}




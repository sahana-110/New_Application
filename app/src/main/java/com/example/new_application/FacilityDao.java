package com.example.new_application;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FacilityDao {
    @Query("SELECT * FROM facilities")
    List<FacilityEntity> getAllFacilities();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFacilities(List<FacilityEntity> facilities);

    @Query("DELETE FROM facilities")
    void deleteAllFacilities();
}

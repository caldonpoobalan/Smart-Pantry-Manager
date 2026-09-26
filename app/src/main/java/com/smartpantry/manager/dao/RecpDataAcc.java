package com.smartpantry.manager.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.smartpantry.manager.model.RecpEntity;

import java.util.List;

// dao for recipes
@Dao
public interface RecpDataAcc {

    // gets all recipes in catalog
    @Query("SELECT * FROM recp_tbl ORDER BY recp_nm ASC")
    List<RecpEntity> getAllRecp();

    // gets recipe by id
    @Query("SELECT * FROM recp_tbl WHERE recp_id = :recpId")
    RecpEntity getRecpById(int recpId);

    // insert multiple recipes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllRecp(List<RecpEntity> recpLst);

    // insert single recipe
    @Insert
    long insertRecp(RecpEntity recpEnt);

    // gets count of recipes
    @Query("SELECT COUNT(*) FROM recp_tbl")
    int countRecp();
}

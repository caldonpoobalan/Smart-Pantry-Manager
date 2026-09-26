package com.smartpantry.manager.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.smartpantry.manager.model.RecpIngredEntity;

import java.util.List;

// dao for recipe ingredients
@Dao
public interface RecpIngredDataAcc {

    // gets ingredients for a recipe
    @Query("SELECT * FROM recp_ingred_tbl WHERE recp_id_ref = :recpIdRef")
    List<RecpIngredEntity> getIngredByRecpId(int recpIdRef);

    // gets all ingredients
    @Query("SELECT * FROM recp_ingred_tbl")
    List<RecpIngredEntity> getAllIngred();

    // insert multiple ingredients
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllIngred(List<RecpIngredEntity> ingLst);
}

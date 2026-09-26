package com.smartpantry.manager.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.smartpantry.manager.model.StockEntity;

import java.util.List;

// dao for pantry stock
@Dao
public interface StockDataAcc {

    // gets all stock items sorted by name
    @Query("SELECT * FROM stock_tbl ORDER BY itm_nm ASC")
    List<StockEntity> getAllStock();

    // insert or replace stock item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStock(StockEntity stkItm);

    // update existing stock item
    @Update
    void updateStock(StockEntity stkItm);

    // delete stock item
    @Delete
    void deleteStock(StockEntity stkItm);

    // delete stock item using its id
    @Query("DELETE FROM stock_tbl WHERE row_id = :rowId")
    void deleteStockById(int rowId);

    // find single stock item by id
    @Query("SELECT * FROM stock_tbl WHERE row_id = :rowId")
    StockEntity getStockById(int rowId);
}

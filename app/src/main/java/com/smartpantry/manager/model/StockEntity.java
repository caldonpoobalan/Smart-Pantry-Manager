package com.smartpantry.manager.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// table for pantry stock items
@Entity(tableName = "stock_tbl")
public class StockEntity {

    // id
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "row_id")
    private int rowId;

    // item name
    @NonNull
    @ColumnInfo(name = "itm_nm")
    private String itmNm;

    // quantity
    @ColumnInfo(name = "qty_val")
    private double qtyVal;

    // unit (for pcs, kg, g, ml, etc)
    @ColumnInfo(name = "unt_lbl")
    private String untLbl;

    // expiry date timestamp, it can be null cause its optional
    @ColumnInfo(name = "exp_date_ms")
    private Long expDateMs;

    public StockEntity(@NonNull String itmNm, double qtyVal, String untLbl, Long expDateMs) {
        this.itmNm = itmNm;
        this.qtyVal = qtyVal;
        this.untLbl = untLbl;
        this.expDateMs = expDateMs;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    @NonNull
    public String getItmNm() {
        return itmNm;
    }

    public void setItmNm(@NonNull String itmNm) {
        this.itmNm = itmNm;
    }

    public double getQtyVal() {
        return qtyVal;
    }

    public void setQtyVal(double qtyVal) {
        this.qtyVal = qtyVal;
    }

    public String getUntLbl() {
        return untLbl;
    }

    public void setUntLbl(String untLbl) {
        this.untLbl = untLbl;
    }

    public Long getExpDateMs() {
        return expDateMs;
    }

    public void setExpDateMs(Long expDateMs) {
        this.expDateMs = expDateMs;
    }
}

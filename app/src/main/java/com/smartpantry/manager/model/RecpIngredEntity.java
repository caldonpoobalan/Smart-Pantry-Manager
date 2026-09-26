package com.smartpantry.manager.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

// table for recipe ingredients, links to recp_tbl
@Entity(
    tableName = "recp_ingred_tbl",
    foreignKeys = @ForeignKey(
        entity = RecpEntity.class,
        parentColumns = "recp_id",
        childColumns = "recp_id_ref",
        onDelete = ForeignKey.CASCADE
    )
)
public class RecpIngredEntity {

    // ingredient row id
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ing_row_id")
    private int ingRowId;

    // recipe foreign key
    @ColumnInfo(name = "recp_id_ref", index = true)
    private int recpIdRef;

    // ingredient name
    @NonNull
    @ColumnInfo(name = "ing_nm")
    private String ingNm;

    // required amount
    @ColumnInfo(name = "req_qty")
    private double reqQty;

    // measurement unit
    @ColumnInfo(name = "unt_meas")
    private String untMeas;

    public RecpIngredEntity(int recpIdRef, @NonNull String ingNm, double reqQty, String untMeas) {
        this.recpIdRef = recpIdRef;
        this.ingNm = ingNm;
        this.reqQty = reqQty;
        this.untMeas = untMeas;
    }

    public int getIngRowId() {
        return ingRowId;
    }

    public void setIngRowId(int ingRowId) {
        this.ingRowId = ingRowId;
    }

    public int getRecpIdRef() {
        return recpIdRef;
    }

    public void setRecpIdRef(int recpIdRef) {
        this.recpIdRef = recpIdRef;
    }

    @NonNull
    public String getIngNm() {
        return ingNm;
    }

    public void setIngNm(@NonNull String ingNm) {
        this.ingNm = ingNm;
    }

    public double getReqQty() {
        return reqQty;
    }

    public void setReqQty(double reqQty) {
        this.reqQty = reqQty;
    }

    public String getUntMeas() {
        return untMeas;
    }

    public void setUntMeas(String untMeas) {
        this.untMeas = untMeas;
    }
}

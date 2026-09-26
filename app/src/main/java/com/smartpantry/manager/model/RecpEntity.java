package com.smartpantry.manager.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// table for recipe catalog
@Entity(tableName = "recp_tbl")
public class RecpEntity {

    // recipe id
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recp_id")
    private int recpId;

    // recipe title
    @NonNull
    @ColumnInfo(name = "recp_nm")
    private String recpNm;

    // short summary
    @ColumnInfo(name = "recp_desc")
    private String recpDesc;

    // instructions to make it
    @ColumnInfo(name = "prep_step_txt")
    private String prepStepTxt;

    public RecpEntity(@NonNull String recpNm, String recpDesc, String prepStepTxt) {
        this.recpNm = recpNm;
        this.recpDesc = recpDesc;
        this.prepStepTxt = prepStepTxt;
    }

    public int getRecpId() {
        return recpId;
    }

    public void setRecpId(int recpId) {
        this.recpId = recpId;
    }

    @NonNull
    public String getRecpNm() {
        return recpNm;
    }

    public void setRecpNm(@NonNull String recpNm) {
        this.recpNm = recpNm;
    }

    public String getRecpDesc() {
        return recpDesc;
    }

    public void setRecpDesc(String recpDesc) {
        this.recpDesc = recpDesc;
    }

    public String getPrepStepTxt() {
        return prepStepTxt;
    }

    public void setPrepStepTxt(String prepStepTxt) {
        this.prepStepTxt = prepStepTxt;
    }
}

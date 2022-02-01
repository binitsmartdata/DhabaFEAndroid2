
package com.transport.mall.model;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class From {

    @SerializedName("fname_en")
    @Expose
    private String fnameEn;
    @SerializedName("_id")
    @Expose
    @ColumnInfo(name = "from_id")
    private String id;

    public String getFnameEn() {
        return fnameEn;
    }

    public void setFnameEn(String fnameEn) {
        this.fnameEn = fnameEn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}

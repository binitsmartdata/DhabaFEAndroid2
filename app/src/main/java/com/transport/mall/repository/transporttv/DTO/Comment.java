package com.transport.mall.repository.transporttv.DTO;

import com.google.gson.annotations.SerializedName;

public class Comment {

    @SerializedName("videoId")
    public String videoId;

    @SerializedName("comment")
    public String comment;

//    @SerializedName("fname_en")
//    public String fname_en;
//    @SerializedName("lname_en")
//    public String lname_en;
    @SerializedName("isAdminComment")
    public boolean isAdminComment = false;
}

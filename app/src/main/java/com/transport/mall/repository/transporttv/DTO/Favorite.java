package com.transport.mall.repository.transporttv.DTO;

import com.google.gson.annotations.SerializedName;

public class Favorite {

    @SerializedName("userId")
    public String userId;

    @SerializedName("videoId")
    public String videoId;

    @SerializedName("isFavorite")
    public boolean isFavorite;
}

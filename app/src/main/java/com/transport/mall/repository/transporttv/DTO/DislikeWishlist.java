package com.transport.mall.repository.transporttv.DTO;

import com.google.gson.annotations.SerializedName;

public class DislikeWishlist {
    @SerializedName("userId")
    public String userId;

    @SerializedName("videoId")
    public String videoId;

//    @SerializedName("isLiked")
//    public boolean isLiked;

    @SerializedName("isDisliked")
    public boolean isDisliked;
}

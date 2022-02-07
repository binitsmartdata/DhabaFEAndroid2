package com.transport.mall.repository.transporttv.DTO;

import com.google.gson.annotations.SerializedName;

public class Language {

    @SerializedName("id")
    public String id;

    @SerializedName("lang")
    public String lang;

    @SerializedName("device_notification_token")
    public String device_notification_token;
    @SerializedName("device_type")
    public String device_type;

}
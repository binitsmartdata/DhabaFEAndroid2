package com.transport.mall.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DhabaModelMain : Serializable {
    @SerializedName("dhaba")
    var dhabaModel: DhabaModel? = null

    @SerializedName("owner")
    var ownerModel: DhabaOwnerModel? = null

    @SerializedName("foodAmenities")
    var foodAmenitiesModel: FoodAmenitiesModel? = null

    @SerializedName("bankDetails")
    var bankDetailsModel: BankDetailsModel? = null

    @SerializedName("parkingAmenities")
    var parkingAmenitiesModel: ParkingAmenitiesModel? = null

    @SerializedName("sleepingAmenities")
    var sleepingAmenitiesModel: SleepingAmenitiesModel? = null

    @SerializedName("washroomAmenities")
    var washroomAmenitiesModel: WashroomAmenitiesModel? = null

    @SerializedName("securityAmenities")
    var securityAmenitiesModel: SecurityAmenitiesModel? = null

    @SerializedName("lightAmenities")
    var lightAmenitiesModel: LightAmenitiesModel? = null

    @SerializedName("otherAmenities")
    var otherAmenitiesModel: OtherAmenitiesModel? = null
}
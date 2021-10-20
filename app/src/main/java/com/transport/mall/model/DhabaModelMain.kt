package com.transport.mall.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class DhabaModelMain : Serializable {
    @SerializedName("dhaba")
    var dhabaModel: DhabaModel? = null

    @SerializedName("owner")
    var ownerModel: UserModel? = null

    @SerializedName("dhabaManagerDeatil")
    var manager: UserModel? = null

    @SerializedName("foodAmenities")
    var foodAmenitiesModel: FoodAmenitiesModel? = null

    @SerializedName("bankDeatil")
    var bankDetailsModel: BankDetailsModel? = null

    @SerializedName("parkingAmenities")
    var parkingAmenitiesModel: ParkingAmenitiesModel? = null

    @SerializedName("sleepingAmenities")
    var sleepingAmenitiesModel: SleepingAmenitiesModel? = null

    @SerializedName("washroomAmenities")
    var washroomAmenitiesModel: WashroomAmenitiesModel? = null

    @SerializedName("securityAmenities")
    var securityAmenitiesModel: SecurityAmenitiesModel? = null

    @SerializedName(value = "lightAmenities", alternate = ["LightAmenities"])
    var lightAmenitiesModel: LightAmenitiesModel? = null

    @SerializedName("otherAmenities")
    var otherAmenitiesModel: OtherAmenitiesModel? = null

    var draftedAtScreen: String? = ""

    enum class DraftScreen {
        DhabaDetailsFragment,
        OwnerDetailsFragment,
        AmenitiesFragment,
        BankDetailsFragment
    }
}
package com.transport.mall.model

import android.content.Context
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import java.io.Serializable

data class AmenityModel(
    val name: LangModel? = null,
    val description: String? = "",
    val isActive: Boolean = true,
    val isDeleted: Boolean = false,
    val _id: String? = ""
) : Serializable {
    override fun toString(): String {
        return name?.en.toString()
    }

    fun getNameByLang(context: Context): String? {
        when (SharedPrefsHelper.getInstance(context).getSelectedLanguage()) {
            "en" -> {
                return name?.en
            }
            "hi" -> {
                return name?.hi
            }
            "pa" -> {
                return name?.pu
            }
        }
        return name?.en
    }

    fun shouldShow(): Boolean {
        return isActive && !isDeleted
    }

    fun isFoodAmenities(): Boolean {
        return _id.equals(FOOD_AMENITIES_ID)
    }

    fun isParkingAmenities(): Boolean {
        return _id.equals(PARKING_AMENITIES_ID)
    }

    fun isSleepingAmenities(): Boolean {
        return _id.equals(SLEEPING_AMENITIES_ID)
    }

    fun isWashroomAmenities(): Boolean {
        return _id.equals(WASHROOM_AMENITIES_ID)
    }

    fun isSecurityAmenities(): Boolean {
        return _id.equals(SECURITY_AMENITIES_ID)
    }

    fun isLightAmenities(): Boolean {
        return _id.equals(LIGHT_AMENITIES_ID)
    }

    fun isOtherAmenities(): Boolean {
        return _id.equals(OTHER_AMENITIES_ID)
    }

    companion object {
        val FOOD_AMENITIES_ID = "618dfdaba537221c4171fe36"
        val PARKING_AMENITIES_ID = "618dfc97a975601bb31691a7"
        val SLEEPING_AMENITIES_ID = "618dfe17a537221c4171fe39"
        val WASHROOM_AMENITIES_ID = "618dfe2fa537221c4171fe3a"
        val SECURITY_AMENITIES_ID = "618e32aba537221c4171fe3b"
        val LIGHT_AMENITIES_ID = "618dfddba537221c4171fe37"
        val OTHER_AMENITIES_ID = "618dfdf2a537221c4171fe38"
    }
}
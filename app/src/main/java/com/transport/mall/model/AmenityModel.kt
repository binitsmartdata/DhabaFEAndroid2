package com.transport.mall.model

import android.content.Context
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import java.io.Serializable

data class AmenityModel(
    val name: LangModel? = null,
    val description: String? = "",
    val isActive: Boolean = true,
    val isDeleted: Boolean = false,
    val _id: String? = "",
    val slug: String? = ""
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
        return slug.equals(FOOD_AMENITIES_SLUG)
    }

    fun isParkingAmenities(): Boolean {
        return slug.equals(PARKING_AMENITIES_SLUG)
    }

    fun isSleepingAmenities(): Boolean {
        return slug.equals(SLEEPING_AMENITIES_SLUG)
    }

    fun isWashroomAmenities(): Boolean {
        return slug.equals(WASHROOM_AMENITIES_SLUG)
    }

    fun isSecurityAmenities(): Boolean {
        return slug.equals(SECURITY_AMENITIES_SLUG)
    }

    fun isLightAmenities(): Boolean {
        return slug.equals(LIGHT_AMENITIES_SLUG)
    }

    fun isOtherAmenities(): Boolean {
        return slug.equals(OTHER_AMENITIES_SLUG)
    }

    companion object {
        val FOOD_AMENITIES_SLUG = "food-amenities"
        val PARKING_AMENITIES_SLUG = "parking-amenities"
        val SLEEPING_AMENITIES_SLUG = "sleeping-amenities"
        val WASHROOM_AMENITIES_SLUG = "washroom-amenities"
        val SECURITY_AMENITIES_SLUG = "security-amenities"
        val LIGHT_AMENITIES_SLUG = "light-amenities"
        val OTHER_AMENITIES_SLUG = "other-amenities"
    }
}
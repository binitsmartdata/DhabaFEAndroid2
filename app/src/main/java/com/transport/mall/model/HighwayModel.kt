package com.transport.mall.model

import java.io.Serializable

data class HighwayModel(
	val highwayNumber: String? = "",
	val km: String? = "",
	val _id: String? = "",
	val discription: String? = "",
	val createdAt: String? = "",
	val updatedAt: String? = ""
) : Serializable {
    override fun toString(): String {
        return highwayNumber!!
    }
}
package com.transport.mall.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SettingsModel(
	@SerializedName("reLoginRequired") var reLoginRequired: Boolean?,
	@SerializedName("_id") var _id: String?,
	@SerializedName("lastSupportedVersion") var lastSupportedVersion: String?,
	@SerializedName("description") var description: String?,
	@SerializedName("createdAt") var createdAt: String?,
	@SerializedName("updatedAt") var updatedAt: String?
) : Serializable
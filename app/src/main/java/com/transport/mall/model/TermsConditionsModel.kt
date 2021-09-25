package com.transport.mall.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TermsConditionsModel(
	@SerializedName("id") var id: String?,

	@SerializedName("title_en") var title_en: String?,

	@SerializedName("title_hi") var title_hi: String?,

	@SerializedName("title_pu") var title_pu: String?,

	@SerializedName("content_en") var content_en: String?,

	@SerializedName("content_hi") var content_hi: String?,

	@SerializedName("content_pu") var content_pu: String?
) : Serializable
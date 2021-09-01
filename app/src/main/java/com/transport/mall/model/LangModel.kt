package com.transport.mall.model

import com.google.gson.annotations.SerializedName

data class LangModel(
	@SerializedName("en") val en: String,
	@SerializedName("hi") val hi: String,
	@SerializedName("pu") val pu: String
)
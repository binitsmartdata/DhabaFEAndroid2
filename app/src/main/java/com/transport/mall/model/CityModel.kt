package com.transport.mall.model

import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
	tableName = "cities",
	indices = [Index(
		value = ["_id"],
		unique = true
	)]
)
data class CityModel(
	@PrimaryKey(autoGenerate = true) var uid: Int,

	@ColumnInfo(name = "slug")
	@SerializedName("slug") var slug: String,

	@ColumnInfo(name = "countryCode")
	@SerializedName("countryCode") var countryCode: Int,

	@ColumnInfo(name = "stateCode")
	@SerializedName("stateCode") var stateCode: Int,

	@ColumnInfo(name = "cityCode")
	@SerializedName("cityCode") var cityCode: Int,

	@ColumnInfo(name = "isDeleted")
	@SerializedName("isDeleted") var isDeleted: Boolean,

	@ColumnInfo(name = "isActive")
	@SerializedName("isActive") var isActive: Boolean,

	@ColumnInfo(name = "_id")
	@SerializedName("_id") var _id: String,

	@ColumnInfo(name = "createdAt")
	@SerializedName("createdAt") var createdAt: String,

	@ColumnInfo(name = "updatedAt")
	@SerializedName("updatedAt") var updatedAt: String
) {
    @Ignore
    @SerializedName("name")
    var name: LangModel? = null
}
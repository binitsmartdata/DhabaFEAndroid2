package com.transport.mall.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
	tableName = "cities",
	indices = [Index(
		value = ["_id"],
		unique = true
	)]
)
data class CityModel(
	@ColumnInfo(name = "uid")
	@PrimaryKey(autoGenerate = true) val id: Int? = null,

	@ColumnInfo(name = "slug")
	@SerializedName("slug") var slug: String?,

	@ColumnInfo(name = "countryCode")
	@SerializedName("countryCode") var countryCode: String?,

	@ColumnInfo(name = "stateCode")
	@SerializedName("stateCode") var stateCode: String?,

	@ColumnInfo(name = "cityCode")
	@SerializedName("cityCode") var cityCode: String?,

	@ColumnInfo(name = "isDeleted")
	@SerializedName("isDeleted") var isDeleted: Boolean?,

	@ColumnInfo(name = "isActive")
	@SerializedName("isActive") var isActive: Boolean?,

	@ColumnInfo(name = "_id")
	@SerializedName("_id") var _id: String?,

	@ColumnInfo(name = "createdAt")
	@SerializedName("createdAt") var createdAt: String?,

	@ColumnInfo(name = "updatedAt")
	@SerializedName("updatedAt") var updatedAt: String?,

	@ColumnInfo(name = "name_en")
	@SerializedName("name_en") var name_en: String?
) : Serializable {
    @Ignore
    @SerializedName("name")
    var name: LangModel? = null

    @Ignore
    var isChecked: Boolean = false

    override fun toString(): String {
        return name_en.toString()
    }
}
import com.google.gson.annotations.SerializedName
import com.transport.mall.model.user.LangModel
import java.io.Serializable

/*
Copyright (c) 2021 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class CityModel(
	@SerializedName("slug") val slug: String,
	@SerializedName("countryCode") val countryCode: Int,
	@SerializedName("stateCode") val stateCode: Int,
	@SerializedName("cityCode") val cityCode: Int,
	@SerializedName("isDeleted") val isDeleted: Boolean,
	@SerializedName("isActive") val isActive: Boolean,
	@SerializedName("_id") val _id: String,
	@SerializedName("name") val name: LangModel,
	@SerializedName("createdAt") val createdAt: String,
	@SerializedName("updatedAt") val updatedAt: String
) : Serializable
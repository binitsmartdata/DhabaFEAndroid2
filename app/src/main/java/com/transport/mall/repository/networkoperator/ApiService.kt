package com.transport.mall.repository.networkoperator


import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.InternalDataListModel
import com.transport.mall.database.InternalDocsListModel
import com.transport.mall.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") deviceId: String
    ): Response<ApiResponseModel<UserModel>>

    @GET("city/getAllCities")
    suspend fun getAllCities(
        @Header("Authorization") token: String,
        @Query("count") count: String,
        @Query("search") search: String,
        @Query("sortBy") sortBy: String,
        @Query("page") page: String,
        @Query("sort") sort: String,
        @Query("status") status: String
    ): Response<ApiResponseModel<InternalDataListModel<ArrayList<CityAndStateModel>>>>

    //limit=10&page=1
    @GET("dhaba/getAllDhabaList")
    suspend fun getAllDhabaList(
        @Query("limit") limit: String,
        @Query("page") page: String
    ): Response<ApiResponseModel<InternalDocsListModel<ArrayList<DhabaModel>>>>

    @FormUrlEncoded
    @POST("city/getCityByState")
    suspend fun getCitiesByState(
        @Field("state_id") status: String
    ): Response<ApiResponseModel<ArrayList<CityAndStateModel>>>

    @GET("state/getAllStates")
    suspend fun getAllStates(
        @Header("Authorization") token: String,
        @Query("count") count: String,
        @Query("search") search: String,
        @Query("sortBy") sortBy: String,
        @Query("page") page: String,
        @Query("sort") sort: String,
        @Query("status") status: String
    ): Response<ApiResponseModel<InternalDataListModel<ArrayList<CityAndStateModel>>>>

    @Multipart
    @POST("dhaba/addDhaba")
    suspend fun uploadDhabaDetails(
        @Part("name") name: RequestBody,
        @Part("address") address: RequestBody,
        @Part("landmark") landmark: RequestBody,
        @Part("area") area: RequestBody,
        @Part("highway") highway: RequestBody,
        @Part("state") state: RequestBody,
        @Part("city") city: RequestBody,
        @Part("pincode") pincode: RequestBody,
        @Part("location") location: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("propertyStatus") propertyStatus: RequestBody,
        @Part images: MultipartBody.Part,
        @Part videos: MultipartBody.Part,
        @Part("createdBy") createdBy: RequestBody,
        @Part("updatedBy") updatedBy: RequestBody
    ): Response<ApiResponseModel<DhabaModel>>

    @Multipart
    @POST("dhaba/addOwner")
    suspend fun addOwner(
        @Part("dhaba_id") dhaba_id: RequestBody,
        @Part("ownerName") ownerName: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("email") email: RequestBody,
        @Part("address") address: RequestBody,
        @Part("panNumber") panNumber: RequestBody,
        @Part("adharCard") adharCard: RequestBody,
        @Part ownerPic: MultipartBody.Part,
        @Part idproofFront: MultipartBody.Part,
        @Part idproofBack: MultipartBody.Part
    ): Response<ApiResponseModel<DhabaOwnerModel>>

    @Multipart
    @POST("dhaba/addFoodAmenities")
    suspend fun addFoodAmenities(
        @Part("service_id") service_id: RequestBody,
        @Part("module_id") module_id: RequestBody,
        @Part("dhaba_id") dhaba_id: RequestBody,
        @Part("foodAt100") foodAt100: RequestBody,
        @Part("roCleanWater") roCleanWater: RequestBody,
        @Part("normalWater") normalWater: RequestBody,
        @Part("food") food: RequestBody,
        @Part("foodLisence") foodLisence: RequestBody,
        @Part foodLisenceFile: MultipartBody.Part,
        @Part images: Array<MultipartBody.Part?>
    ): Response<ApiResponseModel<FoodAmenitiesModel>>
}
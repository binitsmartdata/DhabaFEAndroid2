package com.transport.mall.repository.networkoperator


import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.InternalDataListModel
import com.transport.mall.model.CityAndStateModel
import com.transport.mall.model.UserModel
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
    @POST("/api/Accounts/editaccount")
    fun editUser(
            @Header("Authorization") authorization: String?,
            @Part("file\"; filename=\"pp.png\" ") file: RequestBody?,
            @Part("FirstName") fname: RequestBody?,
            @Part("Id") id: RequestBody?
    ): Response<ApiResponseModel<InternalDataListModel<ArrayList<CityAndStateModel>>>>
}
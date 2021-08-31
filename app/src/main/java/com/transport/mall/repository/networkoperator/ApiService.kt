package com.transport.mall.repository.networkoperator


import CityModel
import com.transport.mall.model.ApiResponseModel
import com.transport.mall.model.user.UserModel
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") deviceId: String
    ): Response<ApiResponseModel<UserModel>>

    @GET("/api/v1/city/getAllCities")
    suspend fun getAllCities(
        @Header("Authorization") token: String,
        @Query("count") count: String,
        @Query("search") search: String,
        @Query("sortBy") sortBy: String,
        @Query("page") page: String,
        @Query("sort") sort: String,
        @Query("status") status: String
    ): Response<ApiResponseModel<CityModel>>
}
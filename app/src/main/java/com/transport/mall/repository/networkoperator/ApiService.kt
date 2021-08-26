package com.transport.mall.repository.networkoperator


import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET("GetCompanyForSetupAsync")
    suspend fun getCompanyDetails(): String

    @GET("Items/GetItemByID")
    suspend fun getItemById2(
        @Query("id") id: String,
        @Query("deviceId") deviceId: String,
        @Header("X-ZUMO-AUTH") token: String
    ): Response<ResponseBody>

    @GET("Items/GetItemByID")
    fun getItemById(
        @Query("id") id: String,
        @Query("deviceId") deviceId: String,
        @Header("X-ZUMO-AUTH") token: String
    ): Observable<String>
}
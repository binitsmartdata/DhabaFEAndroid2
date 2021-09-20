package com.transport.mall.repository.networkoperator


import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.InternalDataListModel
import com.transport.mall.database.InternalDocsListModel
import com.transport.mall.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
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
    ): Response<ApiResponseModel<InternalDataListModel<ArrayList<CityModel>>>>

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
    ): Response<ApiResponseModel<ArrayList<CityModel>>>

    @GET("state/getAllStates")
    suspend fun getAllStates(
        @Header("Authorization") token: String,
        @Query("count") count: String,
        @Query("search") search: String,
        @Query("sortBy") sortBy: String,
        @Query("page") page: String,
        @Query("sort") sort: String,
        @Query("status") status: String
    ): Response<ApiResponseModel<InternalDataListModel<ArrayList<StateModel>>>>

    @GET("dhaba/getAllHighway")
    suspend fun getAllHighway(): Response<ApiResponseModel<ArrayList<HighwayModel>>>

    @GET("dhaba/getDhabaByID")
    suspend fun getDhabaByID(@Query("id") id: String): Response<ApiResponseModel<DhabaModelMain>>

    @Multipart
    @POST("dhaba/addDhaba")
    suspend fun uploadDhabaDetails(
        @Part("owner_id") owner_id: RequestBody,
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
        @Part("status") status: RequestBody,
        @Part images: MultipartBody.Part?,
        @Part videos: MultipartBody.Part?,
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
        @Part ownerPic: MultipartBody.Part?,
        @Part idproofFront: MultipartBody.Part?,
        @Part idproofBack: MultipartBody.Part?
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
        @Part foodLisenceFile: MultipartBody.Part?,
        @Part images: Array<MultipartBody.Part?>
    ): Response<ApiResponseModel<FoodAmenitiesModel>>

    @Multipart
    @POST("dhaba/addParkingAmenities")
    suspend fun addParkingAmenities(
        @Part("service_id") service_id: RequestBody,
        @Part("module_id") module_id: RequestBody,
        @Part("dhaba_id") dhaba_id: RequestBody,
        @Part("concreteParking") concreteParking: RequestBody,
        @Part("flatHardParking") flatHardParking: RequestBody,
        @Part("kachaFlatParking") kachaFlatParking: RequestBody,
        @Part("parkingSpace") parkingSpace: RequestBody,
        @Part images: Array<MultipartBody.Part?>
    ): Response<ApiResponseModel<ParkingAmenitiesModel>>

    @Multipart
    @POST("dhaba/addSleepingAmenities")
    suspend fun addSleepingAmenities(
        @Part("service_id") service_id: RequestBody,
        @Part("module_id") module_id: RequestBody,
        @Part("dhaba_id") dhaba_id: RequestBody,
        @Part("sleeping") sleeping: RequestBody,
        @Part("noOfBeds") noOfBeds: RequestBody,
        @Part("fan") fan: RequestBody,
        @Part("enclosed") enclosed: RequestBody,
        @Part("open") open: RequestBody,
        @Part("hotWater") hotWater: RequestBody,
        @Part images: MultipartBody.Part
    ): Response<ApiResponseModel<SleepingAmenitiesModel>>

    @Multipart
    @POST("dhaba/addWashroomAmenities")
    suspend fun addWashroomAmenities(
        @Part("service_id") service_id: RequestBody,
        @Part("module_id") module_id: RequestBody,
        @Part("dhaba_id") dhaba_id: RequestBody,
        @Part("washroomStatus") washroomStatus: RequestBody,
        @Part("water") water: RequestBody,
        @Part("cleaner") cleaner: RequestBody,
        @Part images: MultipartBody.Part
    ): Response<ApiResponseModel<WashroomAmenitiesModel>>

    @Multipart
    @POST("dhaba/addsecurityAmenities")
    suspend fun addSecurityAmenities(
        @Part("service_id") service_id: RequestBody,
        @Part("module_id") module_id: RequestBody,
        @Part("dhaba_id") dhaba_id: RequestBody,
        @Part("dayGuard") dayGuard: RequestBody,
        @Part("nightGuard") nightGuard: RequestBody,
        @Part("policVerification") policVerification: RequestBody,
        @Part verificationImg: MultipartBody.Part,
        @Part("indoorCamera") indoorCamera: RequestBody,
        @Part indoorCameraImage: Array<MultipartBody.Part?>,
        @Part("outdoorCamera") outdoorCamera: RequestBody,
        @Part outdoorCameraImage: Array<MultipartBody.Part?>
    ): Response<ApiResponseModel<SecurityAmenitiesModel>>

    @Multipart
    @POST("dhaba/addotherAmenities")
    suspend fun addOtherAmenities(
        @Part("service_id") service_id: RequestBody,
        @Part("module_id") module_id: RequestBody,
        @Part("dhaba_id") dhaba_id: RequestBody,
        @Part("mechanicShop") mechanicShop: RequestBody,
        @Part("mechanicShopDay") mechanicShopDay: RequestBody,
        @Part("punctureshop") punctureshop: RequestBody,
        @Part("punctureshopDay") punctureshopDay: RequestBody,
        @Part("dailyutilityshop") dailyutilityshop: RequestBody,
        @Part("dailyutilityshopDay") dailyutilityshopDay: RequestBody,
        @Part("barber") barber: RequestBody,
        @Part barberImages: MultipartBody.Part
    ): Response<ApiResponseModel<OtherAmenitiesModel>>

    @Multipart
    @POST("dhaba/addLightAmenities")
    suspend fun addLightAmenities(
        @Part("service_id") service_id: RequestBody,
        @Part("module_id") module_id: RequestBody,
        @Part("dhaba_id") dhaba_id: RequestBody,
        @Part("towerLight") towerLight: RequestBody,
        @Part towerLightImage: MultipartBody.Part?,
        @Part("bulbLight") bulbLight: RequestBody,
        @Part bulbLightImage: MultipartBody.Part?,
        @Part("electricityBackup") electricityBackup: RequestBody
    ): Response<ApiResponseModel<LightAmenitiesModel>>

    @Multipart
    @POST("bank/addBankDetail")
    suspend fun addBankDetail(
        @Part("user_id") service_id: RequestBody,
        @Part("bankName") bankName: RequestBody,
        @Part("gstNumber") gstNumber: RequestBody,
        @Part("ifscCode") ifscCode: RequestBody,
        @Part("accountName") accountName: RequestBody,
        @Part("panNumber") panNumber: RequestBody,
        @Part panPhoto: MultipartBody.Part?
    ): Response<ApiResponseModel<BankDetailsModel>>

    @Multipart
    @POST("common/s3imgUpload")
    suspend fun uploadImg(
        @Part images: MultipartBody.Part?
    ): Response<ResponseBody>
}
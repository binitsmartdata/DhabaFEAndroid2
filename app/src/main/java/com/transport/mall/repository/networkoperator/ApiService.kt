package com.transport.mall.repository.networkoperator


import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.DhabaTimingModelParent
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

    @FormUrlEncoded
    @POST("user/ownerLogin")
    suspend fun ownerLogin(
        @Field("mobilePrefix") mobilePrefix: String,
        @Field("mobile") mobile: String
    ): Response<ApiResponseModel<UserModel>>

    @FormUrlEncoded
    @POST("user/resendOtp")
    suspend fun resendOtp(
        @Field("mobilePrefix") mobilePrefix: String,
        @Field("mobile") mobile: String
    ): Response<ApiResponseModel<UserModel>>

    @FormUrlEncoded
    @POST("user/ownerSignUp")
    suspend fun signup(
        @Field("name") name: String,
        @Field("mobile") mobile: String,
        @Field("mobilePrefix") mobilePrefix: String,
        @Field("email") email: String,
    ): Response<ApiResponseModel<UserModel>>

    @FormUrlEncoded
    @POST("user/checkOtp")
    suspend fun checkOtp(
        @Field("_id") id: String,
        @Field("otp") otp: String
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

    @GET("bank/getAllBankList")
    suspend fun getAllBankList(): Response<ApiResponseModel<ArrayList<BankNamesModel>>>

    //For terms and condition change slug
    //terms_and_condition || privacy_policy
    @GET("staticPage/getPageContent")
    suspend fun getTermsAndConditions(@Query("slug") slug: String?): Response<ApiResponseModel<ApiResponseModel<TermsConditionsModel>>>

    //limit=10&page=1
    @GET("dhaba/getAllDhabaList")
    suspend fun getAllDhabaList(
        @Header("Authorization") token: String,
        @Query("count") count: String,
        @Query("page") page: String,
        @Query("status") status: String?,
        @Query("searchCity") searchCity: String?,
        @Query("searchState") searchState: String?,
        @Query("searchHighway") searchHighway: String?,
        @Query("searchPincode") searchPincode: String?,
        @Query("search") search: String?
    ): Response<ApiResponseModel<InternalDocsListModel<ArrayList<DhabaModelMain>>>>

    //limit=10&page=1
    @GET("dhaba/getAllOwnerDhabaList")
    suspend fun getAllOwnerDhabaList(
        @Header("Authorization") token: String,
        @Query("count") count: String,
        @Query("page") page: String,
        @Query("status") status: String?,
        @Query("searchCity") searchCity: String?,
        @Query("searchState") searchState: String?,
        @Query("searchHighway") searchHighway: String?,
        @Query("searchPincode") searchPincode: String?,
        @Query("search") search: String?
    ): Response<ApiResponseModel<InternalDocsListModel<ArrayList<DhabaModelMain>>>>

    @FormUrlEncoded
    @POST("city/getCityByState")
    suspend fun getCitiesByState(
        @Field("state_id") status: String
    ): Response<ApiResponseModel<ArrayList<CityModel>>>

    @FormUrlEncoded
    @POST("user/addDhabaManager")
    suspend fun addDhabaManager(
        @Field("name") name: String,
        @Field("mobilePrefix") mobilePrefix: String,
        @Field("mobile") mobile: String,
        @Field("email") email: String,
        @Field("dhabaId") dhabaId: String
    ): Response<ApiResponseModel<UserModel>>

    @FormUrlEncoded
    @POST("dhaba/assignManager")
    suspend fun assignManager(
        @Field("_id") dhabaId: String,
        @Field("dhabaManager_id") dhabaManager_id: String
    ): Response<ResponseBody>

    @FormUrlEncoded
    @POST("dhaba/updateDhabaStatus")
    suspend fun updateDhabaStatus(
        @Field("_id") _id: String,
        @Field("blockDay") blockDay: String,
        @Field("blockMonth") blockMonth: String,
        @Field("isActive") isActive: String,
        @Field("isDraft") isDraft: String,
        @Field("status") status: String
    ): Response<ApiResponseModel<DhabaModel>>

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

    @GET("user/getUserByRole")
    suspend fun getUserByRole(
        @Query("limit") limit: String,
        @Query("page") page: String,
        @Query("role") role: String,
        @Query("searchName") searchName: String?
    ): Response<ApiResponseModel<InternalDocsListModel<ArrayList<UserModel>>>>

    @FormUrlEncoded
    @POST("user/getManagersByOwnerId")
    suspend fun getManagersByOwnerId(
        @Field("ownerId") ownerId: String
    ): Response<ApiResponseModel<InternalDataListModel<ArrayList<UserModel>>>>

    @Multipart
    @POST("dhaba/addDhaba")
    suspend fun addDhaba(
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
        @Part("propertyStatus") propertyStatus: RequestBody?,
        @Part("status") status: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part images: MultipartBody.Part?,
        @Part videos: MultipartBody.Part?,
        @Part("createdBy") createdBy: RequestBody,
        @Part("updatedBy") updatedBy: RequestBody,
        @Part("executive_id") executive_id: RequestBody?
    ): Response<ApiResponseModel<DhabaModel>>

    @Multipart
    @POST("dhaba/updateDhaba")
    suspend fun updateDhaba(
        @Part("_id") _id: RequestBody,
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
        @Part("propertyStatus") propertyStatus: RequestBody?,
        @Part("status") status: RequestBody?,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part images: MultipartBody.Part?,
        @Part videos: MultipartBody.Part?,
        @Part("createdBy") createdBy: RequestBody,
        @Part("updatedBy") updatedBy: RequestBody,
        @Part("executive_id") executive_id: RequestBody?
    ): Response<ApiResponseModel<DhabaModel>>

    @Multipart
    @POST("dhaba/addOwner")
    suspend fun addOwner(
        @Part("dhaba_id") dhaba_id: RequestBody,
        @Part("ownerName") ownerName: RequestBody,
        @Part("mobilePrefix") mobilePrefix: RequestBody,
        @Part("alternativeMobilePrefix") alternativeMobilePrefix: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("email") email: RequestBody,
//        @Part("address") address: RequestBody,
        @Part("panNumber") panNumber: RequestBody,
        @Part("adharCard") adharCard: RequestBody,
        @Part("alternativeContactperson") alternativeContactperson: RequestBody,
        @Part("alternatePhone") alternatePhone: RequestBody,
        @Part("alternateDesignation") alternateDesignation: RequestBody,
//        @Part("latitude") latitude: RequestBody,
//        @Part("longitude") longitude: RequestBody,
        @Part ownerPic: MultipartBody.Part?,
        @Part idproofFront: MultipartBody.Part?,
        @Part idproofBack: MultipartBody.Part?
    ): Response<ApiResponseModel<UserModel>>

    @Multipart
    @POST("dhaba/updateOwner")
    suspend fun updateOwner(
        @Part("_id") _id: RequestBody,
        @Part("ownerName") ownerName: RequestBody,
        @Part("mobilePrefix") mobilePrefix: RequestBody,
        @Part("alternativeMobilePrefix") alternativeMobilePrefix: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("email") email: RequestBody,
//        @Part("address") address: RequestBody,
        @Part("panNumber") panNumber: RequestBody,
        @Part("aadharNumber") adharCard: RequestBody,
        @Part("alternativeContactperson") alternativeContactperson: RequestBody,
        @Part("alternatePhone") alternatePhone: RequestBody,
        @Part("alternateDesignation") alternateDesignation: RequestBody,
//        @Part("latitude") latitude: RequestBody,
//        @Part("longitude") longitude: RequestBody,
        @Part ownerPic: MultipartBody.Part?,
        @Part idproofFront: MultipartBody.Part?,
        @Part idproofBack: MultipartBody.Part?
    ): Response<ApiResponseModel<UserModel>>

    @Multipart
    @POST("user/updateUserProfile")
    suspend fun updateUserProfile(
        @Part("_id") _id: RequestBody,
        @Part("name") fname: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("mobilePrefix") mobilePrefix: RequestBody,
        @Part profileImage: MultipartBody.Part?
    ): Response<ApiResponseModel<UserModel>>

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
        @Part images: Array<MultipartBody.Part?>?
    ): Response<ApiResponseModel<FoodAmenitiesModel>>

    @Multipart
    @POST("dhaba/updateFoodAmenities")
    suspend fun updateFoodAmenities(
        @Part("_id") _id: RequestBody,
        @Part("service_id") service_id: RequestBody,
        @Part("module_id") module_id: RequestBody,
        @Part("dhaba_id") dhaba_id: RequestBody,
        @Part("foodAt100") foodAt100: RequestBody,
        @Part("roCleanWater") roCleanWater: RequestBody,
        @Part("normalWater") normalWater: RequestBody,
        @Part("food") food: RequestBody,
        @Part("foodLisence") foodLisence: RequestBody,
        @Part foodLisenceFile: MultipartBody.Part?,
        @Part images: Array<MultipartBody.Part?>?
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
        @Part images: Array<MultipartBody.Part?>?
    ): Response<ApiResponseModel<ParkingAmenitiesModel>>

    @Multipart
    @POST("dhaba/updateParkingAmenities")
    suspend fun updateParkingAmenities(
        @Part("_id") _id: RequestBody,
        @Part("service_id") service_id: RequestBody,
        @Part("module_id") module_id: RequestBody,
        @Part("dhaba_id") dhaba_id: RequestBody,
        @Part("concreteParking") concreteParking: RequestBody,
        @Part("flatHardParking") flatHardParking: RequestBody,
        @Part("kachaFlatParking") kachaFlatParking: RequestBody,
        @Part("parkingSpace") parkingSpace: RequestBody,
        @Part images: Array<MultipartBody.Part?>?
    ): Response<ApiResponseModel<ParkingAmenitiesModel>>

    @FormUrlEncoded
    @POST("dhaba/delFoodImg")
    suspend fun delFoodImg(
        @Field("_id") amenityId: String,
        @Field("imgId") imgId: String
    ): Response<ApiResponseModel<*>>

    @FormUrlEncoded
    @POST("dhaba/delWashroomImg")
    suspend fun delWashroomImg(
        @Field("_id") amenityId: String,
        @Field("imgId") imgId: String
    ): Response<ApiResponseModel<*>>

    @FormUrlEncoded
    @POST("dhaba/delBarberImg")
    suspend fun delBarberImg(
        @Field("_id") amenityId: String,
        @Field("imgId") imgId: String
    ): Response<ApiResponseModel<*>>

    @FormUrlEncoded
    @POST("dhaba/delSecurityImg")
    suspend fun delSecurityImg(
        @Field("_id") amenityId: String,
        @Field("indoorCameraImageId") indoorCameraImageId: String?,
        @Field("outdoorCameraImageId") outdoorCameraImageId: String?
    ): Response<ApiResponseModel<*>>

    @FormUrlEncoded
    @POST("dhaba/delParkingImg")
    suspend fun delParkingImg(
        @Field("_id") amenityId: String,
        @Field("imgId") imgId: String
    ): Response<ApiResponseModel<*>>

    @Multipart
    @POST("dhaba/addSleepingAmenities")
    suspend fun addSleepingAmenities(
        @Part("service_id") service_id: RequestBody,
        @Part("module_id") module_id: RequestBody,
        @Part("dhaba_id") dhaba_id: RequestBody,
        @Part("sleeping") sleeping: RequestBody,
        @Part("noOfBeds") noOfBeds: RequestBody,
        @Part("fan") fan: RequestBody,
        @Part("cooler") cooler: RequestBody,
        @Part("enclosed") enclosed: RequestBody,
        @Part("open") open: RequestBody,
        @Part("hotWater") hotWater: RequestBody,
        @Part images: MultipartBody.Part?
    ): Response<ApiResponseModel<SleepingAmenitiesModel>>

    @Multipart
    @POST("dhaba/updateSleepingAmenities")
    suspend fun updateSleepingAmenities(
        @Part("_id") _id: RequestBody,
        @Part("service_id") service_id: RequestBody,
        @Part("module_id") module_id: RequestBody,
        @Part("dhaba_id") dhaba_id: RequestBody,
        @Part("sleeping") sleeping: RequestBody,
        @Part("noOfBeds") noOfBeds: RequestBody,
        @Part("fan") fan: RequestBody,
        @Part("cooler") cooler: RequestBody,
        @Part("enclosed") enclosed: RequestBody,
        @Part("open") open: RequestBody,
        @Part("hotWater") hotWater: RequestBody,
        @Part images: MultipartBody.Part?
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
        @Part images: Array<MultipartBody.Part?>?
    ): Response<ApiResponseModel<WashroomAmenitiesModel>>

    @Multipart
    @POST("dhaba/updatewashroomAmenities")
    suspend fun updatewashroomAmenities(
        @Part("_id") _id: RequestBody,
        @Part("service_id") service_id: RequestBody,
        @Part("module_id") module_id: RequestBody,
        @Part("dhaba_id") dhaba_id: RequestBody,
        @Part("washroomStatus") washroomStatus: RequestBody,
        @Part("water") water: RequestBody,
        @Part("cleaner") cleaner: RequestBody,
        @Part images: Array<MultipartBody.Part?>?
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
        @Part verificationImg: MultipartBody.Part?,
        @Part("indoorCamera") indoorCamera: RequestBody,
        @Part indoorCameraImage: Array<MultipartBody.Part?>?,
        @Part("outdoorCamera") outdoorCamera: RequestBody,
        @Part outdoorCameraImage: Array<MultipartBody.Part?>?
    ): Response<ApiResponseModel<SecurityAmenitiesModel>>

    @Multipart
    @POST("dhaba/updatesecurityAmenities")
    suspend fun updateSecurityAmenities(
        @Part("_id") _id: RequestBody,
        @Part("service_id") service_id: RequestBody,
        @Part("module_id") module_id: RequestBody,
        @Part("dhaba_id") dhaba_id: RequestBody,
        @Part("dayGuard") dayGuard: RequestBody,
        @Part("nightGuard") nightGuard: RequestBody,
        @Part("policVerification") policVerification: RequestBody,
        @Part verificationImg: MultipartBody.Part?,
        @Part("indoorCamera") indoorCamera: RequestBody,
        @Part indoorCameraImage: Array<MultipartBody.Part?>?,
        @Part("outdoorCamera") outdoorCamera: RequestBody,
        @Part outdoorCameraImage: Array<MultipartBody.Part?>?
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
        @Part barberImages: Array<MultipartBody.Part?>?
    ): Response<ApiResponseModel<OtherAmenitiesModel>>

    @Multipart
    @POST("dhaba/updateOtherAmenities")
    suspend fun updateOtherAmenities(
        @Part("_id") _id: RequestBody,
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
        @Part barberImages: Array<MultipartBody.Part?>?
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
    @POST("dhaba/updateLightAmenities")
    suspend fun updateLightAmenities(
        @Part("_id") _id: RequestBody,
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
        @Part("user_id") user_id: RequestBody,
        @Part("bankName") bankName: RequestBody,
        @Part("gstNumber") gstNumber: RequestBody,
        @Part("accountNumber") accountNumber: RequestBody,
        @Part("ifscCode") ifscCode: RequestBody,
        @Part("accountName") accountName: RequestBody,
        @Part("panNumber") panNumber: RequestBody,
        @Part panPhoto: MultipartBody.Part?
    ): Response<ApiResponseModel<BankDetailsModel>>

    @Multipart
    @POST("bank/updateBankDetail")
    suspend fun updateBankDetail(
        @Part("_id") _id: RequestBody,
        @Part("user_id") user_id: RequestBody,
        @Part("bankName") bankName: RequestBody,
        @Part("gstNumber") gstNumber: RequestBody,
        @Part("accountNumber") accountNumber: RequestBody,
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

    @POST("dhaba/addDhabaTimeing")
    suspend fun addDhabaTimeing(@Body model: DhabaTimingModelParent): Response<DhabaTimingModelParent>
}
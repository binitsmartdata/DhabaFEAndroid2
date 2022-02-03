package com.smartdata.transportmall.api

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.GsonBuilder
import com.transport.mall.BuildConfig
import com.transport.mall.repository.transporttv.ApiServices
import com.transport.mall.ui.home.helpline.chat.Const
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    const val BASE_URL = Const.BASE_URL
    private var retrofit: Retrofit? = null
    private var retrofitDhaba: Retrofit? = null
    private fun getRetrofitInstance(mContext: Context): Retrofit? {
        if (retrofit == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClient = OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(20000, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.MINUTES)
                .cache(null)
                .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    val original = chain.request()
                    val request: Request.Builder = original.newBuilder()
                        .addHeader("Connection", "close")
                        .addHeader("Client-Service", "frontend-client")
                        .addHeader("Auth-Key", "simplerestapi")
                        .method(original.method, original.body)
                    val res = chain.proceed(request.build())
                    if (!res.isSuccessful) {
                        val body = res.peekBody(Long.MAX_VALUE)
                        FirebaseCrashlytics.getInstance()
                            .setCustomKey("request", res.request.toString())
                        FirebaseCrashlytics.getInstance()
                            .setCustomKey("request_url", res.request.url.toString())
                        SharedPrefsHelper.getInstance(mContext).getUserData().let {
                            FirebaseCrashlytics.getInstance()
                                .setCustomKey("userId", it?._id ?: "")
                            FirebaseCrashlytics.getInstance()
                                .setCustomKey("fname", it?.ownerName ?: "")
                        }
                        FirebaseCrashlytics.getInstance().log("Api exceptions")
                        body.string().let {
                            FirebaseCrashlytics.getInstance()
                                .setCustomKey(
                                    "api error",
                                    it
                                )
                            FirebaseCrashlytics.getInstance()
                                .recordException(
                                    Throwable(it)
                                )
                        }
                    }
                    res
                })
            if (BuildConfig.DEBUG) {
                okHttpClient.addInterceptor(interceptor)
            }

            val gson = GsonBuilder() //                    .setLenient()
                .create()
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient.build())
                .build()
        }
        return retrofit
    }

    private fun getDhabaRetrofitInstance(mContext: Context): Retrofit? {
        if (retrofitDhaba == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClient = OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(10000, TimeUnit.SECONDS)
                .readTimeout(20000, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.MINUTES)
                .cache(null)
                .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    val original = chain.request()
                    val request: Request.Builder = original.newBuilder()
                        .addHeader("Connection", "close")
                        .addHeader("Client-Service", "frontend-client")
                        .addHeader("Auth-Key", "simplerestapi")
                        .method(original.method, original.body)
                    chain.proceed(request.build())
                })
            if (BuildConfig.DEBUG) {
                okHttpClient.addInterceptor(interceptor)
            }

            val gson = GsonBuilder() //                    .setLenient()
                .create()
            retrofitDhaba = Retrofit.Builder()
                .baseUrl(Const.BASE_URL_DHABA)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient.build())
                .build()
        }
        return retrofitDhaba
    }

    fun setNull() {
        retrofit = null
    }

    /**
     * Get API Service
     *
     * @return API Service
     */
    @JvmStatic
    fun getApiService(mContext: Context): ApiServices {
        return getRetrofitInstance(mContext)!!.create(ApiServices::class.java)
    }
}
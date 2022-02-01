package com.smartdata.transportmall.api

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import retrofit2.Retrofit
import com.smartdata.transportmall.api.ApiClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.smartdata.transportmall.BuildConfig
import retrofit2.converter.gson.GsonConverterFactory
import com.smartdata.transportmall.api.ApiServices
import com.smartdata.transportmall.interfaces.Const
import com.smartdata.transportmall.retrofit.App
import okhttp3.Interceptor
import okhttp3.Request
import timber.log.Timber
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
                        App.getUser?.let {
                            FirebaseCrashlytics.getInstance()
                                .setCustomKey("userId", it?.id ?: "")
                            FirebaseCrashlytics.getInstance()
                                .setCustomKey("fname", it?.fnameEn ?: "")
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

    @JvmStatic
    fun getDhabaApiService(mContext: Context): ApiServicesDhaba {
        return getDhabaRetrofitInstance(mContext)!!.create(ApiServicesDhaba::class.java)
    }
}
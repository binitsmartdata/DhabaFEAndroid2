package com.transport.mall.repository.networkoperator


import android.util.Log
import com.transport.mall.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class NetworkAdapter {
    companion object {
        var cInstance: NetworkAdapter? = null
        var apiService: ApiService? = null

        /* Static 'instance' method */
        fun getInstance(): NetworkAdapter {
            if (cInstance == null) {
                cInstance =
                    NetworkAdapter()
                cInstance?.setupRetrofitClient()
                return cInstance!!
            }
            cInstance?.setupRetrofitClient()
            return cInstance!!
        }

    }

    fun setupRetrofitClient() {
        try {
            val HEADER_INTERCEPTOR = Interceptor { chain ->
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json")
                requestBuilder.header("Accept", "application/json")
                requestBuilder.method(original.method, original.body)

                val request = requestBuilder.build()
                chain.proceed(request)
            }


            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor)
                .addInterceptor(HEADER_INTERCEPTOR)
                .cache(null)
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES).build()


            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            apiService = retrofit.create(ApiService::class.java)
        } catch (e: Exception) {
            Log.e("NetworkAdapter.kt :::", e.toString())
            throw RuntimeException(e)
        }
    }

    fun getNetworkServices(): ApiService? {
        return apiService
    }
}
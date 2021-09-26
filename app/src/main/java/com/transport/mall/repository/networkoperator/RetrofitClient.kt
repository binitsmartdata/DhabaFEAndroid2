package com.transport.mall.repository.networkoperator

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Created by parambir.singh on 14/02/18.
 */
class RetrofitClient {
    companion object {
        private lateinit var retrofit: Retrofit

        var interceptor = HttpLoggingInterceptor()

        fun getClient(baseUrl: String): Retrofit {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val gson = GsonBuilder().setLenient().create()
            retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okClient(interceptor))
                    .build()
            return retrofit
        }

        fun okClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
            val httpClient = OkHttpClient.Builder()

            httpClient.addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val requestBuilder = chain.request().newBuilder()
                    requestBuilder.header("Content-Type", "application/json")
                    requestBuilder.header("Accept", "application/json")
                    return chain.proceed(requestBuilder.build())
                }
            })
            httpClient.addInterceptor(loggingInterceptor)

            httpClient.connectTimeout(30, TimeUnit.MINUTES)
            httpClient.readTimeout(30, TimeUnit.MINUTES)

            return httpClient.build()
        }
    }
}
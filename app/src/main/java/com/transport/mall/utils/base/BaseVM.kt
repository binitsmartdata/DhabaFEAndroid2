package com.transport.mall.utils.base

import android.app.Application
import android.content.Context
import android.net.ParseException
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.stream.MalformedJsonException
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.model.DhabaModelMain
import com.transport.mall.model.PhotosModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.repository.networkoperator.ApiService
import com.transport.mall.repository.networkoperator.NetworkAdapter
import com.transport.mall.repository.networkoperator.ResponseCodes
import com.transport.mall.utils.common.GenericCallBack
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.File
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

open class BaseVM(context: Application) : AndroidViewModel(context) {

    var showProgressDialog: MutableLiveData<Boolean>? = null

    fun toggleProgressDialog(): MutableLiveData<Boolean>? {
        showProgressDialog = null
        showProgressDialog = MutableLiveData()
        return showProgressDialog
    }

    fun showProgressDialog(context: Application) {
        GlobalUtils.showProgressDialog(context)
    }

    fun hideProgressDialog() {
        GlobalUtils.hideProgressDialog()
    }

    fun isValidEmail(str: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    fun getApiService(): ApiService? {
        return NetworkAdapter.getInstance().getNetworkServices()
    }

    suspend fun <T> getResponse(request: suspend () -> Response<T>?): ApiResult<T> {
        return try {
            val result = request.invoke()
            if (result?.isSuccessful!!) {
                return ApiResult.success(result.body())
            } else {
                return ApiResult.error(result.message(), result.errorBody())
            }
        } catch (e: Throwable) {
            ApiResult.error(getCorrectErrorMessage(e), null)
        }
    }

    fun getCorrectErrorMessage(t: Throwable): String {
        var error_code: Int = 0
        try {
            if (t is SocketTimeoutException) {
                error_code = ResponseCodes.TIMEOUT_EXCEPTION
            } else if (t is TimeoutException) {
                error_code = ResponseCodes.TIMEOUT_EXCEPTION
            } else if (t is ClassCastException) {
                error_code = ResponseCodes.MODEL_TYPE_CAST_EXCEPTION
            } else if (t is MalformedJsonException) {
                error_code = ResponseCodes.MODEL_TYPE_CAST_EXCEPTION
            } else if (t is ParseException) {
                error_code = ResponseCodes.MODEL_TYPE_CAST_EXCEPTION
            } else if (t is UnknownHostException) {
                error_code = ResponseCodes.URL_CONNECTION_ERROR
            } else if (t is ConnectException) {
                error_code = ResponseCodes.INTERNET_NOT_AVAILABLE
            } else {
                val errorMessage = (t as HttpException).response()?.errorBody()!!.string()
                val responseCode = t.response()?.code()
                responseCode?.let {
                    error_code = it
                }
                return errorMessage
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            error_code = ResponseCodes.UNKNOWN_ERROR
        } finally {
            if (error_code == 0) {
                return t.message.toString()
            }
        }
        return ResponseCodes.logErrorMessage(error_code)
    }

    suspend fun <T> executeApi(input: Response<T>?): Flow<ApiResult<T>> {
        return flow {
            try {
                emit(ApiResult.loading())
                emit(getResponse(request = { input }))
            } catch (e: Exception) {
                Log.e("ERROR", "::::::::::::::::::::::::: SERVER ERROR: executeApi ::::::::::::::::::::::::")
                emit(ApiResult.error(e.toString(), null))
            }
        }.flowOn(Dispatchers.Main)
    }

    fun getPrefs(context: Application): SharedPrefsHelper {
        return SharedPrefsHelper.getInstance(context)
    }

    fun getAccessToken(context: Application): String {
        return SharedPrefsHelper.getInstance(context).getUserData().accessToken
    }

    fun getMultipartImagesList(
        imageList: ArrayList<PhotosModel>,
        parameterName: String
    ): Array<MultipartBody.Part?>? {
        if (imageList.isEmpty()) {
            return null
        } else {
            val surveyImagesParts = arrayOfNulls<MultipartBody.Part>(imageList.size)
            for (index in 0 until imageList.size) {
                surveyImagesParts[index] = getMultipartImageFile(imageList.get(index).path, parameterName)
            }
            return surveyImagesParts
        }
    }

    fun getMultipartVideoFile(path: String, parameterName: String): MultipartBody.Part? {
        if (path == null || path.isEmpty() || path.contains("http")) {
            return null
        } else {
            val requestFile: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), File(path))
            val body =
                MultipartBody.Part.createFormData(parameterName, File(path).getName(), requestFile)
            return body
        }
    }

    fun getMultipartImageFile(path: String, parameterName: String): MultipartBody.Part? {
        if (path == null || path.isEmpty() || path.contains("http")) {
            return null
        } else {
            val requestFile: RequestBody =
                RequestBody.create(MediaType.parse("multipart/form-data"), File(path))
            val body =
                MultipartBody.Part.createFormData(parameterName, File(path).getName(), requestFile)
            return body
        }
    }

    fun showToastInCenter(context: Context?, msg: String) {
        try {
            val toast = Toast.makeText(
                context,
                msg,
                Toast.LENGTH_LONG
            )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        } catch (e: Exception) {
            val toast = Toast.makeText(context, msg.replace("\"".toRegex(), ""), Toast.LENGTH_LONG)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    fun getDhabaById(dhabaId: String, callBack: GenericCallBack<ApiResponseModel<DhabaModelMain>>) {
        GlobalScope.launch(Dispatchers.Main) {
            executeApi(
                getApiService()?.getDhabaByID(dhabaId)
            ).collect {
                when (it.status) {
                    ApiResult.Status.LOADING -> {

                    }
                    ApiResult.Status.ERROR -> {
                        try {
                            callBack.onResponse(Gson().fromJson(it.error?.string(), ApiResponseModel::class.java) as ApiResponseModel<DhabaModelMain>?)
                        } catch (e: Exception) {
                            callBack.onResponse(ApiResponseModel(0, it.message!!, null))
                        }
                    }
                    ApiResult.Status.SUCCESS -> {
                        callBack.onResponse(it.data)
                    }
                }
            }
        }
    }

}
package com.transport.mall.utils.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.transport.mall.model.PhotosModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.repository.networkoperator.ApiService
import com.transport.mall.repository.networkoperator.NetworkAdapter
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

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
            ApiResult.error(e.message, null)
        }
    }

    suspend fun <T> executeApi(input: Response<T>?): Flow<ApiResult<T>> {
        return flow {
            emit(ApiResult.loading())
            emit(getResponse(request = { input }))
        }.flowOn(Dispatchers.IO)
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
    ): Array<MultipartBody.Part?> {
        val surveyImagesParts = arrayOfNulls<MultipartBody.Part>(
            imageList.size
        )

        for (index in 0 until imageList.size) {
            val file = File(imageList.get(index).path)
            val surveyBody = RequestBody.create(MediaType.parse("image/*"), file)
            surveyImagesParts[index] =
                MultipartBody.Part.createFormData(parameterName, file.name, surveyBody)
        }
        return surveyImagesParts
    }

    fun getMultipartVideoFile(path: String, parameterName: String): MultipartBody.Part? {
        if (path == null || path.isEmpty()) {
            return null
        } else {
            return MultipartBody.Part.createFormData(
                parameterName,
                File(path).getName(),
                RequestBody.create(
                    MediaType.parse("video/*"), path
                )
            )
        }
    }

    fun getMultipartImageFile(path: String, parameterName: String): MultipartBody.Part? {
        if (path == null || path.isEmpty()) {
            return null
        } else {
            return MultipartBody.Part.createFormData(
                parameterName,
                File(path).getName(),
                RequestBody.create(
                    MediaType.parse("image/*"), path
                )
            )
        }
    }

}
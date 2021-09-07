package com.transport.mall.utils.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.transport.mall.database.ApiResponseModel
import com.transport.mall.database.InternalDataListModel
import com.transport.mall.model.CityAndStateModel
import com.transport.mall.repository.networkoperator.ApiResult
import com.transport.mall.repository.networkoperator.NetworkAdapter
import com.transport.mall.utils.common.GlobalUtils
import com.transport.mall.utils.common.localstorage.SharedPrefsHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

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

    fun getPrefs(context: Application): SharedPrefsHelper {
        return SharedPrefsHelper.getInstance(context)
    }

    fun getAccessToken(context: Application): String {
        return SharedPrefsHelper.getInstance(context).getUserData().accessToken
    }

    suspend fun getAllCities(app: Application): Flow<ApiResult<ApiResponseModel<InternalDataListModel<ArrayList<CityAndStateModel>>>>> {
        return flow {
            emit(ApiResult.loading())
            emit(
                getResponse(
                    request = {
                        NetworkAdapter.getInstance().getNetworkServices()?.getAllCities(
                            getAccessToken(app),
                            "999",
                            "", "", "1", "ASC", "true"
                        )
                    }
                )
            )
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getCitiesByState(stateId: String): Flow<ApiResult<ApiResponseModel<ArrayList<CityAndStateModel>>>> {
        return flow {
            emit(ApiResult.loading())
            emit(
                getResponse(
                    request = {
                        NetworkAdapter.getInstance().getNetworkServices()?.getCitiesByState(stateId)
                    }
                )
            )
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAllStates(app: Application): Flow<ApiResult<ApiResponseModel<InternalDataListModel<ArrayList<CityAndStateModel>>>>> {
        return flow {
            emit(ApiResult.loading())
            emit(
                getResponse(
                    request = {
                        NetworkAdapter.getInstance().getNetworkServices()?.getAllStates(
                            getAccessToken(app),
                            "999",
                            "", "", "1", "ASC", "true"
                        )
                    }
                )
            )
        }.flowOn(Dispatchers.IO)
    }
}
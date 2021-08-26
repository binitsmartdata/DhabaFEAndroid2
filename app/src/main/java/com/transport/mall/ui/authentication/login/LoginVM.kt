package com.transport.mall.ui.authentication.login

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.healthiex.naha.repository.networkoperator.Result
import com.transport.mall.repository.networkoperator.NetworkAdapter
import com.transport.mall.utils.base.BaseVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

/**
 * Created by Vishal Sharma on 2019-12-06.
 */
class LoginVM(application: Application) : BaseVM(application) {
    val emailObservable = ObservableField<String>()
    val passwordObservable = ObservableField<String>()

    var errorResponse: MutableLiveData<String>? = null
    var progressObserver: MutableLiveData<Boolean>? = null


    var app: Application? = null

    init {
        app = application
    }

    fun observerError(): MutableLiveData<String>? {
        errorResponse = null
        errorResponse = MutableLiveData()
        return errorResponse
    }

    fun observerProgress(): MutableLiveData<Boolean>? {
        progressObserver = null
        progressObserver = MutableLiveData()
        return progressObserver
    }

/*
    fun doLoginProcess() {
        Log.e("doLoginProcess", "-------------------")
        var email = ""
        var password = ""
        emailObservable.get()?.let {
            email = it
        }

        passwordObservable.get()?.let {
            password = it
        }

        when (email.isNotEmpty() && password.isNotEmpty()) {
            true -> {
                GlobalScope.launch(Dispatchers.Main) {
                    getItemById2().collect {
                        when (it.status) {
                            Result.Status.LOADING -> {
                                progressObservable.set(true)
                            }
                            Result.Status.ERROR -> {
                                progressObservable.set(false)
                                it.message
                            }
                            Result.Status.SUCCESS -> {
                                progressObservable.set(false)
                                it.data
                            }
                        }
                    }
                }
                */
/*val intent = Intent(
                    app?.applicationContext,
                    HomeActivity::class.java
                )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                app?.applicationContext?.startActivity(
                    intent
                )*//*

            }
            else -> {
                when {
                    email.isEmpty() -> errorResponse?.value = "Email field cannot be empty"
                    password.isEmpty() -> errorResponse?.value = "Password field cannot be empty"
                }
            }
        }
        progressObserver?.value = true
    }
*/

    suspend fun getItemById2(): Flow<Result<ResponseBody>> {
        return flow {
            emit(Result.loading())
            emit(
                getResponse(
                    request = {
                        NetworkAdapter.getInstance().getNetworkServices()?.getItemById2(
                            "2388",
                            "503093073187838034316115",
                            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEZXZpY2VfNTAzMDkzMDczMTg3ODM4MDM0MyIsImdyb3VwcyI6IjA4MzBjMGFkLTVjYjItNDkzZi04MDg3LTY5OWY5ODdhNDYyMiIsIndlYnNpdGUiOiJTbGluZ3Nob3QiLCJyb2xlIjoibWVyY2hhbnQsQU9CIiwidmVyIjoiMyIsImlzcyI6Imh0dHBzOi8vZG9nYXRld2F5djItZGV2LmF6dXJld2Vic2l0ZXMubmV0LyIsImF1ZCI6Imh0dHBzOi8vZG9nYXRld2F5djItZGV2LmF6dXJld2Vic2l0ZXMubmV0LyIsImV4cCI6MTY2MTIyODg4MywibmJmIjoxNjI5NjkyODgzfQ.xzKeGyibop0ViEmo8pH92dqTrjs1A8ZFZq_HgG5jNRA"
                        )
                    }
                )
            )
        }.flowOn(Dispatchers.IO)
    }
}
package com.transport.mall.ui.home.helpline.chat

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.smartdata.transportmall.BottomNavigation.chat.WorkConstants
import com.smartdata.transportmall.api.ApiClient
import com.transport.mall.database.AppDatabase
import com.transport.mall.database.ChatDao
import com.transport.mall.model.ChatMessagesListModel
import com.transport.mall.repository.ProgressRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MediaMessageWork(val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams), ProgressRequestBody.UploadCallbacks {
    private var executor: ExecutorService? = null
    private var action: String? = null
    private var chatDao: ChatDao? = null
    private lateinit var model: ChatMessagesListModel
    override suspend fun doWork(): Result =withContext(Dispatchers.IO){
        action = inputData.getString(WorkConstants.ARG_KEY_ACTION)
        val modelString = inputData.getString(WorkConstants.ARG_KEY_CHAT_MODEL)
        model = Gson().fromJson(modelString, ChatMessagesListModel::class.java)
        when (action) {
//            WorkConstants.ACTION_RECEIVE_MEDIA -> return downloadMedia(
//                file_name_path,
//                message_id,
//                chat_convo_id,
//                media_type!!
//            )
            WorkConstants.ACTION_UPDATE_PICTURE -> return@withContext uploadTask(model)
        }
        return@withContext Result.failure()
    }

    private suspend fun uploadTask(model: ChatMessagesListModel): Result =withContext(Dispatchers.IO) {

        Log.e("--chat activity--",model.typeUri.toString())
        val dataType: RequestBody? =
            model.type?.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        if (runAttemptCount > 6) {
            return@withContext Result.failure()
        }

        var chatImage: MultipartBody.Part? = null
        var fileBody: ProgressRequestBody? = null
        if (model.type?.equals("pdf", ignoreCase = true) == false) {
            if (model.typeUri != null) {
                val file3 = File(model.typeUri ?: "")
                fileBody =
                    ProgressRequestBody(
                        file3,
                        "multipart/form-data".toMediaTypeOrNull(),
                        model.mobileTimeStamp ?: 0L,
                        this@MediaMessageWork
                    )
                chatImage = MultipartBody.Part.createFormData("filename", file3.name, fileBody)
            }
        } else {
            if (model.typeUri != null) {
                val file3 = File(model.typeUri ?: "")
                fileBody =
                    ProgressRequestBody(
                        file3,
                        "application/pdf".toMediaTypeOrNull(),
                        model.mobileTimeStamp ?: 0L,
                        this@MediaMessageWork
                    )
                chatImage = MultipartBody.Part.createFormData("filename", file3.name, fileBody)
            }
        }

        val call = ApiClient.getApiService(context).imgChat(dataType, chatImage)
        val response: Response<ResponseBody?>? = call.execute()
        try {
            if (response?.code() == 200) {
                val JSON_STRING = response.body()?.string()
                val jsonObject = JSONObject(JSON_STRING ?: "")
                val ImgUrl =
                    Objects.requireNonNull(jsonObject.optJSONObject("data"))
                        .optString("messageToshow")
                val type = Objects.requireNonNull(jsonObject.optJSONObject("data"))
                    .optString("type")
                val msg = Objects.requireNonNull(jsonObject.optJSONObject("data"))
                    .optString("messageTosave")
                executor?.execute {
                    model.typeUri = msg
                    model.message = ImgUrl
                    chatDao?.insert(model)
                }

                setProgressAsync(
                    Data.Builder()
                        .putDouble(PROGRESS, 100.toDouble())
                        .build()
                )
                return@withContext Result.success()
            } else {
                return@withContext Result.retry()
            }
        } catch (e: Exception) {
            return@withContext Result.retry()
        }
    }



    private fun createdAtNowString(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val calendar = Calendar.getInstance()
        val date = calendar.time
        val dayStr = formatter.format(date.time)
        return dayStr
    }

    companion object {
        const val PROGRESS = "PROGRESS"
        private val TAG = MediaMessageWork::class.java.simpleName
    }

    init {
        val db = AppDatabase.getInstance(applicationContext)
        chatDao = db?.chatDao()
        executor = Executors.newSingleThreadExecutor()
        setProgressAsync(
            Data.Builder()
                .putDouble(PROGRESS, 0.0)
                .build()
        )
    }

    override fun onProgressUpdate(percentage: Int, mobileTimeStamp: Long) {
        Log.e("--chat activity--","onProgressUpdate $percentage  $mobileTimeStamp")
        setProgressAsync(
            Data.Builder()
                .putLong("mobileTimeStamp", mobileTimeStamp)
                .putDouble(PROGRESS, percentage.toDouble())
                .build()
        )
    }

    override fun onProgressError(mobileTimeStamp: Long) {

    }

    override fun onProgressFinish(mobileTimeStamp: Long) {
        setProgressAsync(
            Data.Builder()
                .putDouble(PROGRESS, 100.toDouble())
                .build()
        )
    }
}
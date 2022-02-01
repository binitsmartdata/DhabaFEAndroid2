package com.smartdata.transportmall.BottomNavigation.chat

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class ProgressRequestBody(
    private val mFile: File,
    private val content_type: MediaType?,
    private val mobileTimeStamp:Long,
    private val mListener: UploadCallbacks
) : RequestBody() {
    private val mPath: String? = null
    override fun contentType(): MediaType? {
        return content_type
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mFile.length()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val fileLength = mFile.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val `in` = FileInputStream(mFile)
        var uploaded: Long = 0
        val handler = Handler(Looper.getMainLooper())
        try {
            var read: Int
            val handler = Handler(Looper.getMainLooper())
            while (`in`.read(buffer).also { read = it } != -1) {

                handler.post(ProgressUpdater(uploaded, fileLength))
                // update progress on UI thread
                uploaded += read.toLong()
                sink.write(buffer, 0, read)
            }
        }catch (e:Exception){
            handler.postDelayed( {
                mListener.onProgressError(mobileTimeStamp)
            },1000)
        } finally {
            `in`.close()
            handler.postDelayed( {
                mListener.onProgressFinish(mobileTimeStamp)
            },2000)
        }
    }

    private inner class ProgressUpdater(private val mUploaded: Long, private val mTotal: Long) :
        Runnable {
        override fun run() {
            mListener.onProgressUpdate((100 * mUploaded / mTotal).toInt(),mobileTimeStamp)
        }
    }

    interface UploadCallbacks {
        fun onProgressUpdate(percentage: Int,mobileTimeStamp: Long)
        fun onProgressError(mobileTimeStamp: Long)
        fun onProgressFinish(mobileTimeStamp: Long)
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048 *10
    }
}
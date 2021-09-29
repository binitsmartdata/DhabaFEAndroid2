package com.transport.mall.utils.common

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.format.DateUtils
import android.util.Log
import com.abedelazizshe.lightcompressorlibrary.CompressionListener
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor
import com.abedelazizshe.lightcompressorlibrary.VideoQuality
import com.abedelazizshe.lightcompressorlibrary.config.Configuration
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.*
import java.text.DecimalFormat
import kotlin.math.pow

object VideoUtils {
    fun getVideoThumbnail(
        context: Context,
        videoUri: Uri,
        imageWidth: Int,
        imageHeight: Int
    ): Bitmap? {
        var bitmap: Bitmap? = null
        if (Build.VERSION.SDK_INT < 27) {
            val picturePath = getPath(context, videoUri!!)
            Log.i("getVideoThumbnail :::", "picturePath $picturePath")
            if (picturePath == null) {
                val mMMR = MediaMetadataRetriever()

                mMMR.setDataSource(context, videoUri)
                bitmap = mMMR.frameAtTime
            } else {
                bitmap = ThumbnailUtils.createVideoThumbnail(
                    picturePath!!,
                    MediaStore.Images.Thumbnails.MINI_KIND
                )
            }
        } else {
            val mMMR = MediaMetadataRetriever()
            mMMR.setDataSource(context, videoUri)
            bitmap = mMMR.getScaledFrameAtTime(
                -1,
                MediaMetadataRetriever.OPTION_CLOSEST_SYNC,
                imageWidth,
                imageHeight
            )
        }

        return bitmap
    }

    fun saveVideoToAppScopeStorage(context: Context, videoUri: Uri?, mimeType: String?): File? {
        if (videoUri == null || mimeType == null) {
            return null
        }

        val fileName = "capturedVideo.${mimeType.substring(mimeType.indexOf("/") + 1)}"

        val inputStream = context.contentResolver.openInputStream(videoUri)
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), fileName)
        file.deleteOnExit()
        file.createNewFile()
        val out = FileOutputStream(file)
        val bos = BufferedOutputStream(out)

        val buf = ByteArray(1024)
        inputStream?.read(buf)
        do {
            bos.write(buf)
        } while (inputStream?.read(buf) !== -1)

        //out.close()
        bos.close()
        inputStream.close()

        return file
    }

    fun getPath(context: Context, uri: Uri): String? {

        // check here to KITKAT or new version
        //val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (/*isKitKat && */DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return (Environment.getExternalStorageDirectory().toString() + "/"
                            + split[1])
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(
                    context, contentUri, selection,
                    selectionArgs
                )
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    /**
     * Get the value of the data column for this Uri. This is <span id="IL_AD2" class="IL_AD">useful</span> for MediaStore Uris, and other file-based
     * ContentProviders.
     *
     * @param context
     * The context.
     * @param uri
     * The Uri to query.
     * @param selection
     * (Optional) Filter used in the query.
     * @param selectionArgs
     * (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(
        context: Context, uri: Uri?,
        selection: String?, selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.getContentResolver().query(
                uri!!, projection,
                selection, selectionArgs, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            if (cursor != null) cursor.close()
        }
        return null
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri
            .authority
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri
            .authority
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri
            .authority
    }

    /**
     * @param uri
     * The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri
            .authority
    }

    @SuppressLint("SetTextI18n")
    fun processVideo(
        uri: Uri?,
        context: Context,
        callBack: GenericCallBackTwoParams<Long, File?>
    ) {
        var streamableFile: File? = null
        uri?.let {
            GlobalScope.launch {
                // run in background as it can take a long time if the video is big,
                // this implementation is not the best way to do it,
                // todo(abed): improve threading
                val job = async { getMediaPath(context, uri) }
                val path = job.await()

                val desFile = saveVideoFile(path, context)
                streamableFile = saveVideoFile(path, context)

                var playableVideoPath = if (streamableFile != null) streamableFile!!.path
                else path

                desFile?.let {
                    var time = 0L
                    VideoCompressor.start(
                        context = context,
                        srcUri = uri,
                        // srcPath = path,
                        destPath = desFile.path,
                        streamableFile = streamableFile?.path,
                        listener = object : CompressionListener {
                            override fun onProgress(percent: Float) {
                                //Update UI
                                if (percent <= 100 && percent.toInt() % 5 == 0)
                                    Log.d("${percent.toLong()}%", percent.toInt().toString())
                                (context as Activity).runOnUiThread {
//                                    progress.text = "${percent.toLong()}%"
//                                    progressBar.progress = percent.toInt()

                                    callBack.onResponse(percent.toLong(), null)
                                }
                            }

                            override fun onStart() {
                                time = System.currentTimeMillis()
                                /*progress.visibility = View.VISIBLE
                                progressBar.visibility = View.VISIBLE
                                originalSize.text =
                                    "Original size: ${getFileSize(File(path).length())}"
                                progress.text = ""
                                progressBar.progress = 0*/
                            }

                            override fun onSuccess() {
                                val newSizeValue =
                                    if (streamableFile != null) streamableFile!!.length()
                                    else desFile.length()

                                Log.d(
                                    "success :::",
                                    "Size after compression: ${getFileSize(newSizeValue)}"
                                )

                                time = System.currentTimeMillis() - time
                                Log.d(
                                    "DURATION :::",
                                    "Duration: ${DateUtils.formatElapsedTime(time / 1000)}"
                                )

                                callBack.onResponse(
                                    100,
                                    if (streamableFile != null) streamableFile else desFile
                                )

/*
                                Looper.myLooper()?.let {
                                    Handler(it).postDelayed({
                                        progress.visibility = View.GONE
                                        progressBar.visibility = View.GONE
                                    }, 50)
                                }
*/
                            }

                            override fun onFailure(failureMessage: String) {
//                                progress.text = failureMessage
                                Log.wtf("failureMessage", failureMessage)
                                callBack.onResponse(100, null)
                            }

                            override fun onCancelled() {
                                Log.wtf("TAG", "compression has been cancelled")
                                // make UI changes, cleanup, etc
                            }
                        },
                        configureWith = Configuration(
                            quality = VideoQuality.VERY_LOW,
                            frameRate = 24,
                            isMinBitrateCheckEnabled = true,
                        )
                    )
                }
            }
        }
    }

    fun getFileSize(size: Long): String {
        if (size <= 0)
            return "0"

        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()

        return DecimalFormat("#,##0.#").format(
            size / 1024.0.pow(digitGroups.toDouble())
        ) + " " + units[digitGroups]
    }

    fun getMediaPath(context: Context, uri: Uri): String {

        val resolver = context.contentResolver
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        var cursor: Cursor? = null
        try {
            cursor = resolver.query(uri, projection, null, null, null)
            return if (cursor != null) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                cursor.moveToFirst()
                cursor.getString(columnIndex)

            } else ""

        } catch (e: Exception) {
            resolver.let {
                val filePath = (context.applicationInfo.dataDir + File.separator
                        + System.currentTimeMillis())
                val file = File(filePath)

                resolver.openInputStream(uri)?.use { inputStream ->
                    FileOutputStream(file).use { outputStream ->
                        val buf = ByteArray(4096)
                        var len: Int
                        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(
                            buf,
                            0,
                            len
                        )
                    }
                }
                return file.absolutePath
            }
        } finally {
            cursor?.close()
        }
    }

    @Suppress("DEPRECATION")
    fun saveVideoFile(filePath: String?, applicationContext: Context): File? {
        filePath?.let {
            val videoFile = File(filePath)
            val videoFileName = "${System.currentTimeMillis()}_${videoFile.name}"
            val folderName = Environment.DIRECTORY_MOVIES
            if (Build.VERSION.SDK_INT >= 30) {

                val values = ContentValues().apply {
                    put(
                        MediaStore.Images.Media.DISPLAY_NAME,
                        videoFileName
                    )
                    put(MediaStore.Images.Media.MIME_TYPE, "video/mp4")
                    put(MediaStore.Images.Media.RELATIVE_PATH, folderName)
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }

                val collection =
                    MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

                val fileUri = applicationContext.contentResolver.insert(collection, values)

                fileUri?.let {
                    applicationContext.contentResolver.openFileDescriptor(fileUri, "rw")
                        .use { descriptor ->
                            descriptor?.let {
                                FileOutputStream(descriptor.fileDescriptor).use { out ->
                                    FileInputStream(videoFile).use { inputStream ->
                                        val buf = ByteArray(4096)
                                        while (true) {
                                            val sz = inputStream.read(buf)
                                            if (sz <= 0) break
                                            out.write(buf, 0, sz)
                                        }
                                    }
                                }
                            }
                        }

                    values.clear()
                    values.put(MediaStore.Video.Media.IS_PENDING, 0)
                    applicationContext.contentResolver.update(fileUri, values, null, null)

                    return File(getMediaPath(applicationContext, fileUri))
                }
            } else {
                val downloadsPath =
                    applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                val desFile = File(downloadsPath, videoFileName)

                if (desFile.exists())
                    desFile.delete()

                try {
                    desFile.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                return desFile
            }
        }
        return null
    }
}
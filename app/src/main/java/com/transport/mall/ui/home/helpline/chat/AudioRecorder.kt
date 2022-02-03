package com.transport.mall.ui.home.helpline.chat

import android.media.MediaRecorder
import java.io.IOException
import java.lang.Exception
import java.lang.IllegalStateException
import kotlin.Throws

class AudioRecorder {
    private var mediaRecorder: MediaRecorder? = null
    fun initMediaRecorder() {
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
    }

    @Throws(IOException::class, IllegalStateException::class)
    fun start(filePath: String?) {
        if (mediaRecorder == null) {
            initMediaRecorder()
        }
        mediaRecorder!!.setOutputFile(filePath)
        mediaRecorder!!.prepare()
        mediaRecorder!!.start()
    }

    fun stop() {
        try {
            mediaRecorder!!.stop()
            destroyMediaRecorder()
        } catch (e: Exception) {
            e.printStackTrace()
            destroyMediaRecorder()
            //            mediaRecorder=null;
        }
    }

    fun destroyMediaRecorder() {
        mediaRecorder!!.release()
        mediaRecorder = null
    }
}
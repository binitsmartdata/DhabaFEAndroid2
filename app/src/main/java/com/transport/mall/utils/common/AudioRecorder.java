package com.transport.mall.utils.common;

import android.media.MediaRecorder;

import java.io.IOException;

public class AudioRecorder {
    private MediaRecorder mediaRecorder;


    public void initMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
    }

    public void start(String filePath) throws IOException ,IllegalStateException {
        if (mediaRecorder == null) {
            initMediaRecorder();
        }

        mediaRecorder.setOutputFile(filePath);
        mediaRecorder.prepare();
        mediaRecorder.start();
    }

    public void stop() {
        try {
            mediaRecorder.stop();
            destroyMediaRecorder();
        } catch (Exception e) {
            e.printStackTrace();
            destroyMediaRecorder();
//            mediaRecorder=null;
        }

    }

    public void destroyMediaRecorder() {
        mediaRecorder.release();
        mediaRecorder = null;
    }

}

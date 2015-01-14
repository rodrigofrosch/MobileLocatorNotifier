package com.fd.mobilelocatornotifier;

import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;
import java.util.Date;


/**
 * Created by frog on 08/01/15.
 */
public class AudioRecorder {

    private static final String TAG = "AudioRecorder";
    private MediaRecorder mRecorder = null;
    private static String mFileName = null;


    public String onRecord(boolean start) {
        if (start) {
            return startRecording();
        } else {
            return stopRecording();
        }
    }

    private String startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        mRecorder.setOutputFile(fileNameRandom());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }

        mRecorder.start();
        return "";
    }

    private String fileNameRandom() {
        Date date = new Date();
        mFileName = "rec-" + Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += String.valueOf(date.getTimezoneOffset()) +".3gp";
        return mFileName;
    }

    private String stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        return mFileName;
    }

}

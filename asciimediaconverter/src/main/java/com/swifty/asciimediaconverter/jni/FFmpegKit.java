package com.swifty.asciimediaconverter.jni;

import android.util.Log;

import static com.swifty.asciimediaconverter.Constant.TAG;

public class FFmpegKit {
    static {
        System.loadLibrary("ffmpeg");
        System.loadLibrary("ffmpeginvoke");
    }

    public static int execute(String[] commands) throws Exception {
        int ret = run(commands);
        Log.d(TAG, String.valueOf(ret));
        return ret;
    }

    private native static int run(String[] commands);
}
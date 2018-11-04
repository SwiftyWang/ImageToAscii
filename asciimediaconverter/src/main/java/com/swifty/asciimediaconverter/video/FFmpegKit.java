package com.swifty.asciimediaconverter.video;

import android.util.Log;

public class FFmpegKit {
    static {
        System.loadLibrary("ffmpeg");
        System.loadLibrary("ffmpeginvoke");
    }

    public static int execute(String[] commands) throws Exception {
        int ret = run(commands);
        Log.d("FFmpegKit jni return", String.valueOf(ret));
        return ret;
    }

    private native static int run(String[] commands);
}
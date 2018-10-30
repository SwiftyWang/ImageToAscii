package com.swifty.asciimediaconverter.video;

public class FFmpegKit {
    static {
        System.loadLibrary("ffmpeg");
        System.loadLibrary("ffmpeginvoke");
    }

    public static int execute(String[] commands) {
        return run(commands);
    }

    private native static int run(String[] commands);
}
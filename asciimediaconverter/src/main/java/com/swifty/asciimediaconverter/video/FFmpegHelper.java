package com.swifty.asciimediaconverter.video;

import android.util.Log;

import com.swifty.asciimediaconverter.jni.FFmpegKit;

import java.util.ArrayList;

/**
 * Created by Swifty Wang on 27/11/2018.
 */
class FFmpegHelper {

    private static final String TAG = FFmpegHelper.class.getSimpleName();

    boolean images2Video(String imagesPathRex, String outputPath, int fps, int width, int height, float speedX) throws Exception {
        ArrayList<String> _commands = new ArrayList<>();
        _commands.add("ffmpeg");
        _commands.add("-f");
        _commands.add("image2");
        _commands.add("-framerate");
        _commands.add(String.valueOf(fps));
        _commands.add("-r");
        _commands.add(String.valueOf((int) (fps * speedX)));
        if (width != -1 && height != -1) {
            _commands.add("-s");
            _commands.add(width + "x" + height);
        }
        _commands.add("-i");
        _commands.add(imagesPathRex);
        _commands.add(outputPath);

        String[] commands = new String[_commands.size()];
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < _commands.size(); i++) {
            commands[i] = _commands.get(i);
            builder
                    .append(" ")
                    .append(commands[i]);
        }
        Log.d(TAG, "ffmpeg command:" + builder + "-" + commands.length);
        int ret = FFmpegKit.execute(commands);
        return ret == 0;
    }

}

package com.swifty.asciimediaconverter.video;

import android.graphics.Bitmap;

import com.swifty.asciimediaconverter.base.ConvertResponse;

/**
 * Created by Swifty Wang on 30/10/2018.
 */
public class VideoConvertResponse implements ConvertResponse<VideoConvertResponse.CompleteModel> {

    private CompleteModel mCompleteModel;
    private boolean mIsComplete;
    private float mProgress;

    private Bitmap mCurrentFrame;

    VideoConvertResponse(boolean isComplete, float progress, Bitmap currentFrame, CompleteModel completeModel) {
        mCompleteModel = completeModel;
        mIsComplete = isComplete;
        mProgress = progress;
        mCurrentFrame = currentFrame;
    }

    public boolean isComplete() {
        return mIsComplete;
    }

    public float getProgress() {
        return mProgress;
    }

    public Bitmap getCurrentFrame() {
        return mCurrentFrame;
    }

    @Override
    public CompleteModel getResponse() {
        return mCompleteModel;
    }

    public static class CompleteModel {
        public String filePath;
    }

}

package com.swifty.asciimediaconverter.video;

import android.content.Context;

import com.swifty.asciimediaconverter.base.ConvertRequest;

/**
 * Created by Swifty Wang on 30/10/2018.
 */
public class VideoConvertRequest extends ConvertRequest {
    private final Context mContext;
    private String mDesFolder;
    private int mFps = 5;
    private ConvertedFileType mConvertedFileType = ConvertedFileType.GIF;
    // hardcode it to 7, seems this value cannot be changed.
    private int mSampleSize = 7;

    private VideoConvertRequest(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public String getDesFolder() {
        return mDesFolder;
    }

    public int getFps() {
        return mFps;
    }

    public int getSampleSize() {
        return mSampleSize;
    }

    public ConvertedFileType getConvertedFileType() {
        return mConvertedFileType;
    }

    public enum ConvertedFileType {
        GIF("gif"),
        MP4("mp4"),
        AVI("avi");

        private final String mExtension;

        ConvertedFileType(String extension) {
            mExtension = extension;
        }

        public String getExtension() {
            return mExtension;
        }
    }

    public static class Builder extends ConvertRequest.Builder<VideoConvertRequest, Builder> {

        public Builder(Context context) {
            super(new VideoConvertRequest(context));
        }

        public Builder setFps(int fps) {
            mConvertRequest.mFps = fps;
            return this;
        }

        public Builder setDesFolder(String desFolder) {
            mConvertRequest.mDesFolder = desFolder;
            return this;
        }

        public Builder setConvertedFileType(ConvertedFileType convertedFileType) {
            mConvertRequest.mConvertedFileType = convertedFileType;
            return this;
        }
    }
}

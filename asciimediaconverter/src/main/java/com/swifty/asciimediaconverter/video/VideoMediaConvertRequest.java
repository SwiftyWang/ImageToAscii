package com.swifty.asciimediaconverter.video;

import android.content.Context;
import android.text.TextUtils;

import com.swifty.asciimediaconverter.base.MediaConvertRequest;

/**
 * Created by Swifty Wang on 30/10/2018.
 */
public final class VideoMediaConvertRequest extends MediaConvertRequest {
    private final Context mContext;
    private String mDesFolder;
    private int mFps = 5;
    private ConvertedFileType mConvertedFileType = ConvertedFileType.GIF;
    // hardcode it to 7, seems this value cannot be changed.
    private int mSampleSize = 7;
    private float mSpeed = 1;
    private int mWidth = -1;
    private int mHeight = -1;

    private VideoMediaConvertRequest(Context context) {
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

    public float getSpeed() {
        return mSpeed;
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
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

    public static class Builder extends MediaConvertRequest.Builder<VideoMediaConvertRequest, Builder> {

        public Builder(Context context) {
            super(new VideoMediaConvertRequest(context));
        }

        /**
         * Set the sampling rate of the input video.
         * For example: fps = 10 means record 10 frames per second of the input video.
         * Default fps is 5
         *
         * @param fps the video fps
         * @return the builder self
         */
        public Builder setFps(int fps) {
            mConvertRequest.mFps = fps;
            return this;
        }

        /**
         * @param desFolder destination folder to save the result
         * @return the builder self
         */
        public Builder setDesFolder(String desFolder) {
            mConvertRequest.mDesFolder = desFolder;
            return this;
        }

        /**
         * set the converted output media type
         * default is {@link ConvertedFileType#GIF}
         *
         * @param convertedFileType the converted output media type
         * @return the builder self
         */
        public Builder setConvertedFileType(ConvertedFileType convertedFileType) {
            mConvertRequest.mConvertedFileType = convertedFileType;
            return this;
        }

        /**
         * Set the speed of the output media.
         * e.g. speed = 1.5 means output speed it 1.5x of the input video. speed = 1 means same speed as input video.
         * the default value is 1
         *
         * @param speed the speed of the output media.
         * @return the builder self
         */
        public Builder setSpeed(float speed) {
            mConvertRequest.mSpeed = speed;
            return this;
        }

        /**
         * @param widthPx  the output video width, dimension: pixel
         * @param heightPx the output video height, dimension: pixel
         * @return the builder self
         */
        public Builder setSize(int widthPx, int heightPx) {
            mConvertRequest.mWidth = widthPx;
            mConvertRequest.mHeight = heightPx;
            return this;
        }

        @Override
        public VideoMediaConvertRequest build() {
            if (TextUtils.isEmpty(mConvertRequest.mDesFolder)) {
                throw new IllegalArgumentException("the des folder must not be null!");
            }
            return super.build();
        }
    }
}

package com.swifty.asciimediaconverter.image;

import android.content.Context;
import android.graphics.Bitmap;

import com.swifty.asciimediaconverter.base.ConvertRequest;

/**
 * Created by Swifty Wang on 30/10/2018.
 */
public final class ImageConvertRequest extends ConvertRequest {

    private final Context mContext;
    private int mSampleSize = 7;

    private Bitmap mBitmap;

    private ImageConvertRequest(Context context) {
        mContext = context;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public int getSampleSize() {
        return mSampleSize;
    }

    public Context getContext() {
        return mContext;
    }

    public static class Builder extends ConvertRequest.Builder<ImageConvertRequest, Builder> {

        public Builder(Context context) {
            super(new ImageConvertRequest(context));
        }

        public Builder setSampleSize(int sampleSize) {
            mConvertRequest.mSampleSize = sampleSize;
            return this;
        }

        public Builder setOriginBitmap(Bitmap bitmap) {
            mConvertRequest.mBitmap = bitmap;
            return this;
        }
    }
}

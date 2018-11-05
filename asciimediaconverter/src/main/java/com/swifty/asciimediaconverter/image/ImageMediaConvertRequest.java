package com.swifty.asciimediaconverter.image;

import android.content.Context;
import android.graphics.Bitmap;

import com.swifty.asciimediaconverter.base.MediaConvertRequest;

/**
 * Created by Swifty Wang on 30/10/2018.
 */
public final class ImageMediaConvertRequest extends MediaConvertRequest {

    private final Context mContext;

    // hardcode it to 7, seems this value cannot be changed.
    private int mSampleSize = 7;

    private Bitmap mBitmap;


    private ImageMediaConvertRequest(Context context) {
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

    public static class Builder extends MediaConvertRequest.Builder<ImageMediaConvertRequest, Builder> {

        public Builder(Context context) {
            super(new ImageMediaConvertRequest(context));
        }

        public Builder setOriginBitmap(Bitmap bitmap) {
            mConvertRequest.mBitmap = bitmap;
            return this;
        }
    }
}

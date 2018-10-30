package com.swifty.asciimediaconverter.image;

import android.graphics.Bitmap;

import com.swifty.asciimediaconverter.base.ConvertResponse;

/**
 * Created by Swifty Wang on 30/10/2018.
 */
public final class ImageConvertResponse implements ConvertResponse<Bitmap> {
    private Bitmap mBitmap;

    public ImageConvertResponse(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    public Bitmap getResponse() {
        return mBitmap;
    }
}

package com.swifty.asciimediaconverter.image;

import io.reactivex.Single;


/**
 * Created by Swifty Wang on 30/10/2018.
 */
public interface ImageConverter {
    Single<ImageConvertResponse> convertRx(ImageConvertRequest convertRequest);

    ImageConvertResponse convertSync(ImageConvertRequest convertRequest);
}

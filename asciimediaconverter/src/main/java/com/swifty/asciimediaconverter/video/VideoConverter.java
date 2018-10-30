package com.swifty.asciimediaconverter.video;

import io.reactivex.Observable;

/**
 * Created by Swifty Wang on 30/10/2018.
 */
public interface VideoConverter {

    Observable<VideoConvertResponse> convertRx(VideoConvertRequest convertRequest);
}

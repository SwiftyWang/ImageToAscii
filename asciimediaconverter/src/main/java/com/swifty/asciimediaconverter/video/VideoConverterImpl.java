package com.swifty.asciimediaconverter.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.swifty.asciimediaconverter.Utils;
import com.swifty.asciimediaconverter.image.ImageConvertResponse;
import com.swifty.asciimediaconverter.image.ImageConverter;
import com.swifty.asciimediaconverter.image.ImageConverterImpl;
import com.swifty.asciimediaconverter.image.ImageMediaConvertRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

import static com.swifty.asciimediaconverter.Constant.TAG;

/**
 * Created by Swifty Wang on 30/10/2018.
 */
public class VideoConverterImpl implements VideoConverter {
    private static final int MAX_IMAGE_COUNT_NUMBER_OF_DIGITS = 5;
    private static final String TEMP_IMAGE_NAME = ".temp_%0" + MAX_IMAGE_COUNT_NUMBER_OF_DIGITS + "d.png";

    @Override
    public Observable<VideoConvertResponse> convertRx(final VideoMediaConvertRequest convertRequest) {
        Observable<VideoConvertResponse> observable = Observable.create(new ObservableOnSubscribe<VideoConvertResponse>() {
            @Override
            public void subscribe(ObservableEmitter<VideoConvertResponse> emitter) throws Exception {
                int fps = convertRequest.getFps();
                Context context = convertRequest.getContext();
                String desFolder = convertRequest.getDesFolder();
                if (!desFolder.endsWith("/")) {
                    desFolder = desFolder.concat("/");
                }

                // decode video to images and convert. the progress will react to 0.9 if finish this step
                MediaDecoder mediaDecoder = new MediaDecoder(convertRequest.getFilePath());
                int encodeTotalCount = 0;
                String videoFileLength = mediaDecoder.getVideoFileLength();
                if (videoFileLength != null) {
                    try {
                        int length = Integer.parseInt(videoFileLength);
                        encodeTotalCount = length / (1000 / fps);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                String tempPicFolder = desFolder + ".pic/";
                // clear temp folder first
                Utils.deleteDir(tempPicFolder);
                Utils.mkdirs(tempPicFolder);
                if (String.valueOf(encodeTotalCount).length() > MAX_IMAGE_COUNT_NUMBER_OF_DIGITS) {
                    throw new IllegalArgumentException("the input video is too long!");
                }

                // create a ImageConvert for later use
                ImageConverter imageConverter = new ImageConverterImpl();

                for (int i = 0; i < encodeTotalCount; i++) {
                    Log.i(TAG, "start process" + i);
                    Bitmap bitmap = mediaDecoder.decodeFrame(i * (1000 / fps));
                    if (bitmap == null) {
                        continue;
                    }
                    ImageMediaConvertRequest.Builder builder = new ImageMediaConvertRequest.Builder(context);
                    builder
                            .setOriginBitmap(bitmap)
                            .setEnableColor(convertRequest.isEnableColor());
                    ImageConvertResponse imageConvertResponse = imageConverter.convertSync(builder.build());
                    Log.i(TAG, "end process" + i);

                    VideoConvertResponse videoConvertResponse = new VideoConvertResponse(false, (float) ((float) (i + 1) / encodeTotalCount * 0.9), imageConvertResponse.getResponse(), null);
                    emitter.onNext(videoConvertResponse);
                    FileOutputStream fos;
                    try {
                        String formatSuffix = String.format(Locale.getDefault(), TEMP_IMAGE_NAME, i);
                        fos = new FileOutputStream(tempPicFolder + formatSuffix, false);
                        imageConvertResponse.getResponse().compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // encode images to video
                String desVideoPath = generateDesVideoPath(desFolder, convertRequest);
                FFmpegHelper fFmpegHelper = new FFmpegHelper();
                boolean success = fFmpegHelper
                        .images2Video(tempPicFolder + TEMP_IMAGE_NAME, desVideoPath,
                                fps, convertRequest.getWidth(), convertRequest.getHeight(), convertRequest.getSpeed());
                Utils.deleteDir(tempPicFolder);
                if (success) {
                    VideoConvertResponse.CompleteModel completeModel = new VideoConvertResponse.CompleteModel();
                    completeModel.filePath = desVideoPath;
                    VideoConvertResponse videoConvertResponse = new VideoConvertResponse(true, 1, null, completeModel);
                    emitter.onNext(videoConvertResponse);
                    emitter.onComplete();
                } else {
                    emitter.onError(new RuntimeException("FFmpeg return error with error code:" + success));
                }
            }
        }).subscribeOn(Schedulers.computation());
        return observable;
    }

    private boolean ffmpegMerge(String picsPath, String desPath, int fps) throws Exception {
        FFmpegHelper fFmpegHelper = new FFmpegHelper();
        return fFmpegHelper.images2Video(picsPath, desPath, fps, -1, -1, 1);
    }

    private String generateDesVideoPath(String desFolder, VideoMediaConvertRequest convertRequest) {
        File file = new File(convertRequest.getFilePath());
        String fileName;
        if (file.exists()) {
            fileName = file.getName();
        } else {
            DateFormat dateFormat = DateFormat.getDateTimeInstance();
            fileName = dateFormat.format(Calendar.getInstance().getTime());
        }
        int i = fileName.lastIndexOf(".");
        if (i != -1 && i != 0) {
            fileName = fileName.substring(0, i);
        }
        final String videoName = fileName + "." + convertRequest.getConvertedFileType().getExtension();
        return desFolder + videoName;
    }
}

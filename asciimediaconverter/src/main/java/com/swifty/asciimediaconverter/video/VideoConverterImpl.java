package com.swifty.asciimediaconverter.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.swifty.asciimediaconverter.Utils;
import com.swifty.asciimediaconverter.image.ImageConvertRequest;
import com.swifty.asciimediaconverter.image.ImageConvertResponse;
import com.swifty.asciimediaconverter.image.ImageConverter;
import com.swifty.asciimediaconverter.image.ImageConverterImpl;

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

/**
 * Created by Swifty Wang on 30/10/2018.
 */
public class VideoConverterImpl implements VideoConverter {
    private static final String TEMP_IMAGE_PREFIX = ".temp_";

    @Override
    public Observable<VideoConvertResponse> convertRx(final VideoConvertRequest convertRequest) {
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
                String tempPicFolder = desFolder + "/.pic/";
                Utils.mkdirs(tempPicFolder);
                for (int i = 0; i < encodeTotalCount; i++) {
                    Log.i("icv", "第" + i + "张解码开始");
                    Bitmap bitmap = mediaDecoder.decodeFrame(i * (1000 / fps));
                    Log.i("icv", "第" + i + "张解码结束");
                    if (bitmap == null) {
                        continue;
                    }
                    Log.i("icv", "第" + i + "张转换开始");
                    ImageConverter imageConverter = new ImageConverterImpl();
                    ImageConvertRequest.Builder builder = new ImageConvertRequest.Builder(context);
                    builder.setOriginBitmap(bitmap);
                    ImageConvertResponse imageConvertResponse = imageConverter.convertSync(builder.build());
                    Log.i("icv", "第" + i + "张转换结束");

                    VideoConvertResponse videoConvertResponse = new VideoConvertResponse(false, (float) ((float) (i + 1) / encodeTotalCount * 0.9), imageConvertResponse.getResponse(), null);
                    emitter.onNext(videoConvertResponse);
                    FileOutputStream fos;
                    try {
                        String format = String.format(Locale.getDefault(), "%05d", i);
                        fos = new FileOutputStream(tempPicFolder + TEMP_IMAGE_PREFIX + format + ".png", false);
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
                String desVideoPath = ffmpegMerge(tempPicFolder, convertRequest);

                VideoConvertResponse.CompleteModel completeModel = new VideoConvertResponse.CompleteModel();
                completeModel.filePath = desVideoPath;
                VideoConvertResponse videoConvertResponse = new VideoConvertResponse(true, 1, null, completeModel);
                Utils.deleteDir(tempPicFolder);
                emitter.onNext(videoConvertResponse);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.computation());
        return observable;
    }

    private String ffmpegMerge(String picFolder, VideoConvertRequest videoConvertRequest) {
        int fps = videoConvertRequest.getFps();
        VideoConvertRequest.ConvertedFileType convertedFileType = videoConvertRequest.getConvertedFileType();
        String desFolder = videoConvertRequest.getDesFolder();
        File file = new File(videoConvertRequest.getFilePath());
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
        final String videoName = fileName + "." + convertedFileType.getExtension();
        String desVideoPath = desFolder + videoName;
        String[] commands = FFmpegCommandCentre.concatVideo(picFolder, desVideoPath, String.valueOf(fps));
        FFmpegKit.execute(commands);
        return desVideoPath;
    }
}
